package org.anc.togo

import java.security.MessageDigest;
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.zip.ZipException;

import org.anc.conf.AnnotationConfig
import org.anc.conf.AnnotationType
import org.anc.io.ANCFileFilter
import org.anc.io.SuffixFilter
import org.anc.togo.db.Corpus
import org.anc.togo.db.*
import org.anc.togo.constants.*
import org.anc.tool.api.IProcessor
import org.anc.tool.api.ProcessorException
import org.xces.graf.io.dom.ResourceHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProcessingService {
    static transactional = true

    // Disable while debugging.
    public static final boolean SEND_EMAIL = true

    private static final Logger log = LoggerFactory.getLogger(ProcessingService)

    def corporaMap = [:]
    def processorMap = [:]

    def xmlService
    def nltkService
    def conllService
    def wordsmithService
    def rdfService
    def uimaService

    /**
     * Default constructor populates the corporaMap and processorMap
     */
    public ProcessingService() {
        // Load properties file
        log.info("Initializing the ProcessingService")
        loadCorporaMap()
//        log.info(corporaMap)

        log.info("Loading the processor map.")
        loadProcessorMap()
    }

    /**
     * Loads the corpora configuration properties file to create a corporaMap,
     * which maps corpus name to location of corpus root & resource header
     *
     * Ex: [MASC:/var/corpora/MASC-3, MASC-header:/var/corpora/MASC-3/resource-header.xml]
     */
    //change
    private void loadCorporaMap() {

        Corpus.list().each {
            String corpusName = it.name;
            String corpusRoot = it.root;
            String corpusHeader = corpusRoot + "/resource-header.xml"
            corporaMap[corpusName] = corpusRoot
            corporaMap[corpusName + "-header"] = corpusHeader
        }


        println "corporaMap: " + corporaMap
    }


    /**
     * Populates the processorMap by iterating over the jars in the processor
     * folder specified in conf/{username}/org.anc.togo.constants.PathConstants
     * and looking for classes implementing the {@link IProcessor} class.
     * The processor name is then mapped onto its class, which can then be
     * instantiated as needed.
     *
     * @return
     */
    private void loadProcessorMap() {

        // Iterate over jars to get processor classes:
        log.info("Loading processor map.")
        File lib = new File(Globals.PATH.PROCESSORS)
        log.info("Processors directory is " + lib.getAbsolutePath())
        if (!lib.exists()) {
            log.error("Unable to find the processors directory: " + lib.getPath())
            return
        }

        def jarList = lib.listFiles(new SuffixFilter(".jar"))
        if (jarList == null || jarList.size() == 0) {
            log.error("No processors found in " + lib.getPath())
            return
        }

        def urlList = []
        for (File f : jarList) {
            urlList << f.toURI().toURL()
        }
        log.info("There are " + jarList.size() + " processor jars.")
        // Get class loader
        ClassLoader parentLoader = Thread.currentThread().getContextClassLoader()
        if (parentLoader == null) {
            log.warn("Falling back to parent loader.")
            parentLoader = ProcessingService.class.getClassLoader()
        }
        URLClassLoader loader = new URLClassLoader((URL[]) urlList.toArray(), parentLoader)

        // Iterate over jars in lib, looking for classes implementing IProcessor
        for (File jar : jarList) {
            log.info("Scanning jar file {}", jar.path)

            JarFile jarFile = new JarFile(jar)
            Enumeration<JarEntry> e = jarFile.entries()
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement()
                String name = entry.getName()
                if (name.endsWith(".class")) {
                    name = fix(name)

                    // Hack to prevent the class loader from loading weird files that would throw errors
                    // TODO find a workaround for this
                    if (name.endsWith("Processor")) {
                        log.debug("Found Processor class: {}", name)
                        try {
                            Class<?> theClass = loader.loadClass(name, true)
                            log.debug('Class loaded.')
                            for (Class<?> iface : theClass.getInterfaces()) {
                                log.trace('Checking interface {}', iface.name)
                                if (iface == IProcessor) {
                                    // Use processor database to look up processor name by class name
                                    // Note: substring(6) eliminates the "class " part of the string
                                    String processorName = Processor.findByClassName(theClass.toString().substring(6)).name
                                    // Add processor to map
                                    processorMap[processorName] = theClass
                                    log.info("Mapped ${processorName} to ${theClass.name}")
                                }
                            }
                        }
                        catch (java.lang.NoClassDefFoundError ex) {
                            log.error("Ignoring (not unexpected) NoClassDefFoundError: " + ex.getMessage());
                        }
                        catch (Exception ex) {
                            log.error("Ignoring unexpected Exception: " + ex.getMessage());
                        }
                        catch (RuntimeException ex) {
                            log.error("Caught RuntimeException", ex)
                        }
                    }
                }
            }
        }
        log.info('Loaded {} processors', processorMap.size())

    }

    /**
     * "Fixes" the class name so the class loader can retrieve it.
     */
    def fix(String s) {
        s = s.replaceAll('/', '.')
        s = s.replaceAll('$', '.')
        // Truncate the ".class"
        int n = s.length() - 7
        return s.substring(0, n)
    }

    /**
     * Loads UI descriptors
     */
    def getDescriptors = {
        log.warn("The getDescriptors method should be deprecated.")
        def descriptors = []
        descriptors << xmlService.getUIDescriptor()
        descriptors << nltkService.getUIDescriptor()
        descriptors << wordsmithService.getUIDescriptor()
        descriptors << conllService.getUIDescriptor()
        descriptors << uimaService.getUIDescriptor()
//      descriptors << rdfService.getUIDescriptor()
        return descriptors
    }

    /**
     * Begins processing based on params received from UI.
     *
     * @param config Passed in by application controller.
     */
    void start(Map config) {
        // Get processor from map, and instantiate new instance
        String processorName = config.get("processor")
        log.debug("Processor name is " + processorName)
        Class<IProcessor> processorClass = processorMap[processorName]
        if (!processorClass) {
            throw new ProcessorException("No processor found for ${processorName}")
        }
        IProcessor processor = (IProcessor) processorClass.newInstance()
        // Get corpus info
        String corpusName = config.get("corpus")
        String headerRoot = corporaMap[corpusName + "-header"]
        println "headerroot:   ${headerRoot}"
        File headerFile = new File(headerRoot)
        if (!headerFile.exists()) {
            String message = "Resource header not found: " + headerFile.getPath()
            log.error(message)
            println message
            throw new ProcessorException(message)
        }
        log.info("Resource header is " + headerFile.getPath())
        ResourceHeader header = new ResourceHeader(headerFile)
        processor.setResourceHeader(header)
        log.info("got processor: " + processor.class.toString())

        /*
         * Configure processor
         */
        def types = config.get("types")
        log.info("types: ${types}")
        String options = config.get("options")
        AnnotationConfig ac = new AnnotationConfig()
        types.each {
            log.info("Adding annotation type " + it)
            ac.add(AnnotationType.getValueOf(it))
        }
        log.info("Setting annotation types.")
        processor.setAnnotationTypes(ac)

        log.info("options: ${options}")

        switch (processorName) {

        /*
         * XML processor
         */
            case Processors.XML:
                // Set overlap handling
                if (options.contains("overlap=Discard")) {
                    options = "discard" //GrafReader.OVERLAP_DISCARD
                } else if (options.contains("overlap=Nest")) {
                    options = "overlap" //GrafReader.OVERLAP_NEST
                } else {
                    options = "milestone" //GrafReader.OVERLAP_MILESTONE
                }
                processor.setOptions(options)
                break

        /*
         * Other processors, if special handling necessary
         */
            case Processors.NLTK:
            case Processors.CONLL:
            case Processors.WORDSMITH:
                break
        }

        processor.initialize()

        // Initialize directory vars
        String corpusRoot = corporaMap[corpusName]
        log.info("corpusRoot: ${corpusRoot}")
        String key = ((String) config.get("key")).replace(":", "-")
        log.info("key: ${key}")
        File cache = new File(Globals.PATH.CACHE)
        if (!cache.exists()) {
            log.warn("Missing cache directory: " + cache.getAbsolutePath())
            if (!cache.mkdirs()) {
                log.error("Unable to create cache directory " + cache.getAbsolutePath())
                throw new ProcessorException("Unable to create the cache directory.")
            }
        }
        File outputDirectory = new File(cache, key)

        // Construct list of input directories by iterating through list of
        // directories (i.e., written and/or spoken) and appending to corpus root
        def inputDirectories = []
        config.get("directories").each { dirName ->
            // Need to get path from directory name. This requires matching
            // the directory to the correct corpus
            println "Directory: $dirName \t Corpus name: $corpusName"
            def c = Directory.createCriteria()
            def results = c.list {
                like('did', dirName)
                corpus {
                    like('name', corpusName)
                }
            }
            Directory dirMatch = results[0]

            if (dirMatch) {
                String textClass = dirMatch.textClass.toLowerCase()

                inputDirectories << new File(dirMatch.path)
            } else {
                throw new ProcessorException("Directory not found in database")
            }
        }

        // Check before we begin processing
        inputDirectories.each { File it ->
            if (!it.isDirectory()) {
                throw new ProcessorException("Input location is not a directory. " + it.path)
            }
        }
        if (!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
            throw new ProcessorException("Output location is not a directory: " + outputDirectory.getPath())
        }

        // Begin worker thread
        log.info("Processing service starting worker thread.")
        new Thread(new Worker(inputDirectories, outputDirectory, processor, config, Globals.PATH.DOWNLOADS)).start()
    }

    /**
     * Sends an email notifying specified recipient that his/her download is ready.
     *
     * @param recipient
     * @param filename
     */
    static boolean sendNotificationEmail(String recipient, String filename) {
        EmailSender sender = new EmailSender()
        String subject = "ANC2Go Download Ready"
        String message = "Your ANC2Go-generated annotations are ready for download.\nYou can download the files using this link: http://anc.org/downloads/ANC2Go/${filename}"
        sender.postMail(recipient, subject, message)
        return true
    }
}

class Worker implements Runnable {
    List inputDirectories
    File outputDirectory
    IProcessor processor
    Map config
    File downloadRoot
    Logger log = LoggerFactory.getLogger(Worker.class)

    public Worker(List inputDirectories, File outputDirectory, IProcessor processor, Map config, String downloadRoot) {
        this.inputDirectories = inputDirectories
        this.outputDirectory = outputDirectory
        this.processor = processor
        this.config = config
        this.downloadRoot = new File(downloadRoot)
    }

    public void processDirectory(File dir) {
        dir.eachFileMatch ~/.*\.hdr/, { file ->
            // Begin processing
            try {
                println "processing file: " + file
                processFile(file, outputDirectory)
            }
            catch (Exception e) {
                log.error("Unable to process file" + file.getPath(), e)
                println(e)
            }
        }
        // Recurse through subdirectories to catch subgenres
        dir.eachDirRecurse { subDirectory ->
            processDirectory(subDirectory)
        }
    }

    public void run() {
        log.info("Worker thread using processor ${config.processor} on ${config.corpus}")

        // Check if cache exists for given config
        String key = config.key
        String filename = config.filename



        File zipFile = new File(downloadRoot, filename)

        // TODO change back to actual program logic after testing
        // Q: What is the "actual program logic" we should be changing to?!?
        // A: The !zipFile.exists() test.
        if (true) //(!zipFile.exists())
        {
            for (File inputDirectory : inputDirectories) {
                processDirectory(inputDirectory)
            }

            // Zip converted files and add to cache
            log.info("zipping converted files to {}", zipFile.path)

            try {
                DirectoryCompressor.zip(outputDirectory, zipFile)
            }
            catch (ZipException e) {
                log.error("Error: directory must contain at least one file to be zipped.")
            }
        }

        // Clean up cache
        outputDirectory.deleteDir()

        // List job as ready
        Job job
        Job.withTransaction() {
            job = Job.findByKey(config.key)
            if (job) {
                job.ready = true
                job.save()
            }
            log.info("Job ${job.id} is ready.")
        }

        // Now that processing is done, get list of all relevant job requests
        // (i.e., job requests with matching key)
        def jobRequests = JobRequest.findAllByJob(job)
        log.info("number of job requests: {}", jobRequests.size())
        println "num requests: " + jobRequests.size()

        if (jobRequests) {
            jobRequests.each { jobRequest ->
                log.info("attempting to post mail for email {}", jobRequest.email)
                String recipient = jobRequest.email


                if (ProcessingService.SEND_EMAIL) {
                    log.debug("Sending email is enabled.")
                    ProcessingService.sendNotificationEmail(recipient, zipFile.getName())
                           log.info("email sent successfully to address {}", recipient)
                } else {
                    log.info("Email not sent due to debugging option")
                }
                ((JobRequest) jobRequest).delete()
            }
        }

        log.info("Processing finished.")
        println "Processing finished"
    }

    void processFile(File input, File outDir) throws ProcessorException {
        if (input.isDirectory()) {
            File newOut = new File(outDir, input.getName())
            if (!newOut.exists()) {
                if (!newOut.mkdirs()) {
                    throw new ProcessorException("Unable to create output directory ${newOut.getPath()}")
                }
            }
            input.listFiles(new ANCFileFilter(true)).each { processFile(it, newOut) }
        } else {
            String filename = input.getName().replace(".hdr", ".${processor.getFileExtension()}")
            File outfile = new File(outDir, filename)
            log.info("Processing " + input.getPath())

            try {
                processor.process(input, outfile)
            }
            catch (Exception e) {
                log.error("Error processing file.", e)
            }
            finally {
                // If file is empty, attempt to delete
                if (outfile.length() == 0) {
                    log.info("Attempting to delete empty file: {}", outfile.getPath())
                    if (!outfile.delete()) {
                        outfile.deleteOnExit()
                    }
                }
            }
        }
    }
}