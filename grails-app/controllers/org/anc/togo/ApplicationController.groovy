package org.anc.togo

import ch.qos.logback.classic.Logger
import grails.converters.*
import org.anc.togo.api.IProcessorService
import org.anc.togo.api.ProcessorException
import org.anc.togo.constants.Globals;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Directory
import org.anc.togo.db.Job
import org.anc.togo.db.JobRequest
import org.anc.togo.db.Processor
import org.slf4j.LoggerFactory

class ApplicationController {

   private Logger log = LoggerFactory.getLogger(ApplicationController)

   def processingService = new ProcessingService()
   
   def debug = {
	
	   [
		   cache:Globals.PATH.CACHE,
		   downloads:Globals.PATH.DOWNLOADS,
		   work:Globals.PATH.WORK
	   ]   
   }
   
   def index = {
      def corpora = []
      def labels = []
      def selectedCorpusName = params.corpusName
      def selectedCorpus

      Corpus.list().sort() each {
         corpora.add(it)
         labels.add(it.toString())
      }

      //selected corpus is taken from the URL
      //defaults to the first if there isn't anything
      if(selectedCorpusName==null)
      {
         selectedCorpusName = corpora[0].name.toUpperCase()
         selectedCorpus = corpora[0]
      }
      else
      {
         selectedCorpusName = selectedCorpusName.toUpperCase()
         selectedCorpus = findCorpusByName(selectedCorpusName,corpora)
      }
	  
      def descriptors = processingService.getDescriptors()
      def selectedProcessor = "XML"

      [
        corpus:selectedCorpus,
        corpusName:selectedCorpusName,
        selectedProcessor:selectedProcessor,
        processingService: processingService,
        corpora:corpora,
        labels:labels,
        processors:Processor.list(),
        descriptors:descriptors,
        email:Globals.SMTP.DEFAULT_ADDRESS,
        version:grailsApplication.metadata['app.version']
	  ] 
	}

   def findCorpusByName(name,corpora) {
      for(c in corpora) {
         if(c.name.equals(name)) {
            return c
         }
      }
      log.error("Unable to find a corpus named {}", name)
      return corpora[0]
   }

   def options = {
      
   }
	
   def test = {
      def corpora = []
      def labels = []
      Corpus.list().each {
         corpora.add(it)
         labels.add(it.toString())
      }      

      [
        corpora:corpora, 
        labels:labels, 
        processors:Processor.list(), 
        descriptors:processingService.getDescriptors(),
        email:Globals.SMTP.DEFAULT_ADDRESS
     ]   
   }
   
   def getJson = {
      if (!params.corpus)
      {
         return [error:'No corpus specified.'] as JSON
      }
      
      String procName = params.processor ?: 'XML'     

      Corpus corpus = Corpus.findByName(params.corpus)
      if (!corpus)
      {
         return [error: 'Corpus not found.'] as JSON
      }
      Processor processor = Processor.findByName(procName)
      if (!processor)
      {
         return [error:'Processor not found.'] as JSON
      }

      render corpus.types.intersect(processor.types) as JSON
   }
   

   def submit = {
      StringBuilder key = new StringBuilder()

      String corpusName = params.corpusName
      String procName = params.selectedProcessor

      log.info("Submitting job for ${corpusName} by ${procName}")

      key << corpusName
      key << procName
      
      def types = []
      def options = [:]
      def directories = []

      params.each { param,value ->
         if (param.startsWith("F_"))
         {
            List list = param.tokenize("_")
            def corpus = list.get(1)
            def proc = list.get(2)
            String type = list.get(3)
            if (corpusName == corpus)
            { 
               if (procName == proc)
               {
                  if (type == "tokens")
                  {
                     if (value != "notoken")
                     {
                        if(value.startsWith("f."))
                        {
                           types << value
                        }
                        else
                        {
                           types << "f."+value
                        }
                     }
                  }
                  else if (type == "option")
                  {
                     def name = list.get(4)
                     options[name] = value
                  }
                  else
                  {
                     if (type != "notoken")
                     {
                        if(type.startsWith("f."))
                        {
                           types << type
                        }
                        else
                        {
                           types << "f."+type
                        }
                     }
                  }
               }
               else  if (param == "F_OANC_travel_guides_dir") {
                  def dirName = "travel_guides"
                  directories << dirName
               }
			   else if (param.endsWith('dir'))
			   {
				   def dirName = param.tokenize('_')[2]
				   directories << dirName
			   }
            }
         }
         
      }
      
      key << types.join(",")
      key << directories.join(",")
      key << options.values().join(",")

      // Flag to be set when the worker thread is started.  If the worker does not start then
      // the failure page is displayed to the user.
      boolean started = true
      String errorMessage = null

      // Look up job by key. If null, it hasn't been started yet; instantiate and begin processing 
      def job = Job.findByKey(key.toString())
      String filename = "Unknown"
      if (job) {
         filename = Util.getZipFilename(job)
         log.info("Job for ${filename} is already currently processing or has already been finished during this session.")
         if (job.ready) {

            // TODO The download directory needs to be parameterized into a configuration file somewhere.
            File testFile = new File("/home/www/anc/downloads/ANC2Go/$filename")
            if (testFile.exists()) {
               log.info("job has already completed; just send email.")
               String recipient = params.email
               ProcessingService.sendNotificationEmail(recipient, filename)
               log.info("email sent successfully to address ${recipient}")
            }
            else {
               log.warn("Job is ready but the file is missing.")
               job.delete(flush: true)
               job = null
            }
         }
         else {
            // job has not yet completed; create new job request, and ProcessingService will take care of email
            log.info("Creating a job request to email ${params.email1} when ${filename} is ready")
            new JobRequest(email:params.email1, job:job, ready:false).save()
         }
      }
      // This can't simply be an 'else' to the above 'if' statement as the value
      // of job may have changed if the output file is missing.
      if(!job)
      {
         log.info("creating new job with string ${key.toString()}")
         job = new Job(key:key.toString(), path:'/path', ready:false)
         job.save()
         filename = Util.getZipFilename(job)
         def map = [ corpus:corpusName, processor:procName, types:types, options:options, directories:directories, email:params.email1, params:params, key:key.toString(), filename: filename ]
         log.info("Starting job for ${params.email} to generate ${filename}")
         new JobRequest(email:params.email, job:job).save(flush: true, failOnError: true)
		 try {
            processingService.start(map)
         }
         catch (ProcessorException e) {
            errorMessage = e.getMessage()
            started = false
         }
         catch (Exception e) {
            errorMessage = e.getMessage()
            e.printStackTrace()
            started = false
         }
      }

      if (started) {
         render(view: "success", model: [filename: filename])
      }
      else {
         log.error("There was a problem starting the job.")
         log.error("Message: {$errorMessage}", errorMessage)
         render(view:"failure", model: [ error: errorMessage ])
      }
      
      [jobs:Job.list(), requests:JobRequest.list()]
   }
   
   private List getProcessors()
   {
      def names = []
      Processor.list().each {
         names.add(it.name)
      }
      return names
   }
}
