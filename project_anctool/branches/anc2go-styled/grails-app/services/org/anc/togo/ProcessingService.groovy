package org.anc.togo

import java.security.MessageDigest;
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.zip.ZipException;

import org.anc.conf.AnnotationConfig
import org.anc.conf.AnnotationType
import org.anc.io.ANCFileFilter
import org.anc.io.SuffixFilter
import org.anc.togo.db.*
import org.anc.togo.constants.*
import org.anc.tool.api.IProcessor
import org.anc.tool.api.ProcessorException
import org.xces.graf.io.dom.ResourceHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProcessingService
{
   static transactional = true
   
   // Disable while debugging.
   private static final boolean SEND_EMAIL = false
   
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
   public ProcessingService()
   {
      // Load properties file
      log.info("Initializing the ProcessingService")
      println ("--------------------##########initializing processing service")
      log.info("Loading the corporaMap")
      loadCorporaMap()
      log.info(corporaMap)

      log.info("Loading the processor map.")
      loadProcessorMap()
   }
   
   /**
    * Loads the corpora configuration properties file to create a corporaMap,
    * which maps corpus name to location of corpus root & resource header
    * 
    * Ex: [MASC:/var/corpora/MASC-3, MASC-header:/var/corpora/MASC-3/resource-header.xml]
    */
   private void loadCorporaMap()
   {
//      if (corporaMap) return
      
     log.info("Using corpora directory " + Globals.PATH.CORPORA_CONF);
	 println "Using corpora directfory " + Globals.PATH.CORPORA_CONF
     File corporaDir = new File(Globals.PATH.CORPORA_CONF);
//     println "Using corpora directory " + Globals.PATH.CORPORA_CONF
     
     if (!corporaDir.exists())
     {
        log.error("Corpora configuration directory not found.")
		println "Corpora configuration directory not found."
        return
     }
     
      corporaDir.eachFileMatch(~/.*\.properties/) { propFile ->
         loadCorpusProperties(propFile)
      }
     println "corporaMap: " + corporaMap
   }
   
   /**
    * Helper method to load a properties file corresponding to a corporaMap.
    * 
    * TODO Update this to load from custom corpus script
    * @param propFile
    */
   private void loadCorpusProperties(File propFile)
   {
//      println "loading properties file " + propFile.path
	  println "Loading prop file ${propFile.path}"
      log.debug("Loading properties file ${propFile.path}")
      Properties corpusProp = new Properties()
      corpusProp.load(new FileInputStream(propFile))
      String corpusName = corpusProp.getProperty("name")
      String corpusRoot = corpusProp.getProperty("root")
      String corpusHeader = corpusProp.getProperty("header")
      println "corpusRoot: $corpusName"
      println "corpusRoot: $corpusRoot"
      corporaMap[corpusName] = corpusRoot
      corporaMap[corpusName + "-header"] = corpusHeader
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
   private void loadProcessorMap()
   {
      //if (processorMap) return
      
      // Iterate over jars to get processor classes:
      log.info("Loading processor map.")
      println "Loading processor map."
      File lib = new File(Globals.PATH.PROCESSORS)
      log.info("Processors directory is " + lib.getAbsolutePath())
	  println "Processors directory is " + lib.getAbsolutePath()
      if (!lib.exists())
      {
         log.error("Unable to find the processors directory: " + lib.getPath())
         println "Unable to find the processors directory: " + lib.getPath()
         return
      }
     
      def jarList = lib.listFiles(new SuffixFilter(".jar"))
      if (jarList == null || jarList.size() == 0)
      {
         log.error("No processors found in " + lib.getPath())
         println "No processors found in " + lib.getPath()
         return
      }
     
      def urlList = []
      for (File f : jarList) {
         urlList << f.toURI().toURL()
      }
      log.debug("There are " + jarList.size() + " processor jars.")
      println "There are " + jarList.size() + " processor jars."
      // Get class loader
      ClassLoader parentLoader = Thread.currentThread().getContextClassLoader()
      if (parentLoader == null)
      {
         log.warn("Falling back to parent loader.")
         println "Falling back to parent loader."
         parentloader = ProcessManagerService.class.getClassLoader()
      }
      URLClassLoader loader = new URLClassLoader((URL[]) urlList.toArray(), parentLoader)
      
      // Iterate over jars in lib, looking for classes implementing IProcessor
      for (File jar : jarList)
      {
         JarFile jarFile = new JarFile(jar)
//         println "checking out jar in location ${jar.absolutePath}"
         Enumeration<JarEntry> e = jarFile.entries()
         while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement()
            String name = entry.getName()
            if (name.endsWith(".class")) {
               name = fix(name)
               
               // Hack to prevent the class loader from loading weird files that would throw errors
               // TODO find a workaround for this
               if (name.endsWith("Processor"))
               {
                  try {
                     Class<?> theClass = loader.loadClass(name, true)
                     for (Class<?> iface : theClass.getInterfaces()) {
//                     println "iface: ${iface}"
                        if (iface == IProcessor) {
                           println "processor class: $name"
                           // Use processor database to look up processor name by class name
                           // Note: substring(6) eliminates the "class " part of the string
                           String processorName = Processor.findByClassName(theClass.toString().substring(6)).name
                           println "Processor name: $processorName"
                           // Add processor to map
                           processorMap[processorName] = theClass
                        println("Added processor with name ${processorName}")
                              }
                           }
                  }
                  catch (Exception ex) {
                     log.error("Ignoring exception ", ex)
                     println "ignoring exception"
                  }
                  catch (RuntimeException ex) {
                     log.error("Caught RuntimeException")
                     println "caught runtime exception"
                  }
               }
            }
         }
      }
      println "pmap: " + processorMap
   }

   /**
    * "Fixes" the class name so the class loader can retrieve it.
    */
   def fix(String s)  {
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
   void start(Map config)
   {
      // Get processor from map, and instantiate new instance
      String processorName = config.get("processor")
      log.debug("Processor name is " + processorName)
      println "Proc name is : " + processorName
      Class<IProcessor> processorClass = processorMap[processorName]
      IProcessor processor = (IProcessor) processorClass.newInstance()
      // Get corpus info
      String corpusName = config.get("corpus")
      String headerRoot = corporaMap[corpusName + "-header"]
	  println "headerroot:   ${headerRoot}"
      File headerFile = new File(headerRoot)
      if (!headerFile.exists())
      {
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
      if (!cache.exists())
      {
         log.warn("Missing cache directory: " + cache.getAbsolutePath())
         if (!cache.mkdirs())
         {
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
//         println "Directory: $dirName \t Corpus name: $corpusName"
         def c = Directory.createCriteria()
         def results = c.list {
            like('did', dirName)
            corpus {
               like('name',corpusName)
            }
         }
         Directory dirMatch = results[0]
//		 println 'dirMatch: ' + dirMatch
         if (dirMatch)
         {
//            println "Directory path: ${dirMatch.path}"
            String textClass = dirMatch.textClass.toLowerCase()
            inputDirectories << new File(corpusRoot + '/data/' + textClass + '/' + dirMatch.path)
         }
         else
         {
            throw new ProcessorException("Directory not found in database")
         }
      }
      for (File dir : inputDirectories)
      {
//         println "input directory: " + dir.getAbsolutePath()
      }
      
      
      
      // Old code
//      def inputDirectories = []
//      config.get("directories").each {
//         inputDirectories << new File(corpusRoot+"/data/"+it)
//      }
//      log.info("inputDirectories: " + inputDirectories)
      //

      // Check before we begin processing
      inputDirectories.each {
         if (!it.isDirectory()) {
            throw new ProcessorException("Input location is not a directory.")
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
   static boolean sendNotificationEmail(String recipient, String filename)
   {
      EmailSender sender = new EmailSender()
      String subject = "ANC2Go Download Ready"
      String message = "Your ANC2Go-generated annotations are ready for download.\nYou can download the files using this link: http://anc.org/downloads/${filename}"
      return sender.postMail(recipient, subject, message)
   }
}

class Worker implements Runnable
{
   List inputDirectories
   File outputDirectory
   IProcessor processor
   Map config
   File downloadRoot
   Logger log = LoggerFactory.getLogger(Worker.class)

   public Worker(List inputDirectories, File outputDirectory, IProcessor processor, Map config, String downloadRoot)
   {
      this.inputDirectories = inputDirectories
      this.outputDirectory = outputDirectory
      this.processor = processor
      this.config = config
      this.downloadRoot = new File(downloadRoot)
   }
   
   public void processDirectory(File dir)
   {
      dir.eachFileMatch ~/.*\.hdr/, { file ->
         // Begin processing
         try
         {
            println "processing file: " + file
            processFile(file, outputDirectory)
            }
         catch (Exception e)
         {
            log.error("Unable to process file" + file.getPath(), e)
         }
      }
      // Recurse through subdirectories to catch subgenres
      dir.eachDirRecurse { subDirectory ->
         processDirectory(subDirectory)
      }
   }

   public void run()
   {
      log.info("Worker thread using processor ${config.processor} on ${config.corpus}")

      // Check if cache exists for given config
      String key = config.key
      String filename = key.replace(":", "-").substring(0, key.length() - 1)
	  
	  // Generate hash
	  byte[] bytesOfMessage = key.getBytes("UTF-8")
	  MessageDigest md = MessageDigest.getInstance("MD5")
	  byte[] bytes = md.digest(bytesOfMessage)
	  def hash = String.format("%16x", new BigInteger(1, bytes))
	  println "hash: ${hash}"
	  
	  
	  
      File zipFile = new File(downloadRoot, hash + ".zip")

      // TODO change back to actual program logic after testing
     // What is the "actual program logic" we should be changing to?!?
      if (true) //(!zipFile.exists())
      {
         for(File inputDirectory : inputDirectories)
         {
            processDirectory(inputDirectory)
         }

         // Zip converted files and add to cache
         log.info("zipping converted files")
         
         try
         {
            DirectoryCompressor.zip(outputDirectory, zipFile)
         }
         catch (ZipException e)
         {
            log.error("Error: directory must contain at least one file to be zipped.")
         }
      }

      // Clean up cache
      outputDirectory.deleteDir()

      // List job as ready
      Job job
      Job.withTransaction() {
         job = Job.findByKey(config.key)
         if (job)
         {
            job.ready = true
            job.save()
         }
         log.info("Job ${job.id} is ready.")
      }

      // Now that processing is done, get list of all relevant job requests
      // (i.e., job requests with matching key)
      def jobRequests = JobRequest.findAllByJob(job)
      log.info("number of job requests: {}", jobRequests.size())
      
      if (jobRequests)
      {
         jobRequests.each { jobRequest ->
            log.info("attempting to post mail for email {}", jobRequest.email)
            String recipient = jobRequest.email
            
            
            if (ProcessingService.SEND_EMAIL)
            {
               ProcessingService.sendNotificationEmail(recipient, zipFile.getName())
               //            EmailSender sender = new EmailSender()
               //            String subject = "ANC2Go Download Ready"
               //            String message = "Download ready for job.\nDownload link: http://anc.org/downloads/Verbatim-CAS.zip"
               //            sender.postMail(recipient, subject, message)
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

   void processFile(File input, File outDir) throws ProcessorException
   {
      if (input.isDirectory())
      {
         File newOut = new File(outDir, input.getName())
         if (!newOut.exists())
         {
            if(!newOut.mkdirs())
            {
               throw new ProcessorException("Unable to create output directory ${newOut.getPath()}")
            }
         }
         input.listFiles(new ANCFileFilter(true)).each {  processFile(it, newOut)  }
      }
      else
      {
//         String filename = input.getName().replace(".anc", ".${processor.getFileExtension()}")
		 String filename = input.getName().replace(".hdr", ".${processor.getFileExtension()}")
         File outfile = new File(outDir, filename)
         log.info("Processing " + input.getPath())
         
         try
         {
            processor.process(input, outfile)
         }
         catch (Exception e)
         {
            log.error("Error processing file.", e)
//            log.error(e.getMessage())
//            e.printStackTrace()  
         }
         finally
         {
            // If file is empty, attempt to delete
            if (outfile.length() == 0)
            {
               log.info("Attempting to delete empty file: {}", outfile.getPath())
               if (!outfile.delete())
               {
                  outfile.deleteOnExit()
               }
            }
         }
      }
   }
}