package org.anc.togo

import grails.converters.*
import org.anc.togo.api.IProcessorService
import org.anc.togo.constants.Globals;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Directory
import org.anc.togo.db.Job
import org.anc.togo.db.JobRequest
import org.anc.togo.db.Processor

//import ch.qos.logback.core.joran.conditional.ElseAction;

class ApplicationController {

   def processingService = new ProcessingService()
   
   def debug = {
	
	   [
		   cache:Globals.PATH.CACHE,
		   downloads:Globals.PATH.DOWNLOADS,
		   work:Globals.PATH.WORK
	   ]   
   }
   
   def index = {
      println "Calling application.index"
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

      println "corpusName: " + selectedCorpusName
      println "directories " + selectedCorpus.directories.asList()
      //println "sub dir " + selectedCorpus.directories.first().directories.asList();
	  
      def descriptors = processingService.getDescriptors()
	  println "pros serv: " + processingService
	  println "desript: " + descriptors
      def selectedProcessor = "XML"
	  //def processingService = "org.anc.tool.processor.xml-1.0.0-SNAPSHOT"
	  
      
//      log.info("# Descriptors: ${descriptors.size()}")

      [
        corpus:selectedCorpus,
        corpusName:selectedCorpusName,
        selectedProcessor:selectedProcessor,
        processingService: processingService,
		  corpora:corpora, 
		  labels:labels, 
		  processors:Processor.list(), 
		  descriptors:descriptors,
		  email:Globals.SMTP.DEFAULT_ADDRESS
	  ] 
	}

   def findCorpusByName(name,corpora) {
      for(c in corpora) {
         if(c.name.equals(name)) {
            return c
         }
      }
      println "ERROR"
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
      
//      log.info("# Descriptors: ${descriptors.size()}")
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
      println "Selected corpus is ${params.corpus}"
      
      String procName = params.processor ?: 'XML'     

      Corpus corpus = Corpus.findByName(params.corpus)
      if (!corpus)
      {
         println "Corpus not found."
         return [error: 'Corpus not found.'] as JSON
      }
      Processor processor = Processor.findByName(procName)
      if (!processor)
      {
         println "Processor not found."
         return [error:'Processor not found.'] as JSON
      }
//      def query = "select T.name from AnnotationType T, CorpusTypes C, ProcessorTypes P " +      
//      " where C.type.id = P.type.id and T.id = P.type.id and C.corpus.name = ? and P.processor.name = ?"
//      def types = AnnotationType.executeQuery(query, [params.corpus, processor])
//      println("Returned a " + types.getClass().getName() + " size: " + types.size())
//      types.each { println it }
      //[corpus:params.corpus, processor:procName, result:corpus.types.intersect(processor.types)]
      render corpus.types.intersect(processor.types) as JSON
   }
   

   def submit = {
      log.info("Submitting")
	  //println "params: ${params}"
     //println "corpus: ${params.corpus}"
      StringBuilder key = new StringBuilder()

      String corpusName = params.corpusName
      //String corpusName = params.corpus
      String procName = params.selectedProcessor
      String fullProcName = params.processingService
      
      println "---corpusName: " + corpusName
      println "--procName: " + procName

      key << corpusName
      //key << ":"
      key << procName
      //key << ":"
      
      def types = []
      def options = [:]
      def directories = []
      
      int count = 0
      println "params: $params"
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
//                     else
//                     {
//                        // special handling for ptb syntax
//                        if (value == "f.ptb_syntax")
//                        {
//                           println "found ptb_syntax"
//                        }
//                     }
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
//				   println "param ends with dir! ${param}"
				   def dirName = param.tokenize('_')[2]
//				   println "dirName: ${dirName}"
				   directories << dirName
			   }
//               else if (proc == "DIR")
//               {
//                  log.debug("Adding directory " + type)
//                  directories << type
//               }
            }
         }
         
      }
      
      key << types.join(",")
      //key << ":"
      key << directories.join(",")
      //key << ":"
      key << options.values().join(",")
      
      println '///////\nKEY KEY KEY\n//////'
      println key.toString()
      
      // Look up job by key. If null, it hasn't been started yet; instantiate and begin processing 
      def job = Job.findByKey(key.toString())
      String filename = "Unknown"
      //TODO switch back
      if (job) {
         log.info("job is already currently processing or has already been finished during this session.")
         if (job.ready) {
//            filename = job.key.replace(':', '-').substring(0, job.key.length() - 1) + ".zip"
            filename = Util.getZipFilename(job)
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
            new JobRequest(email:params.email1, job:job).save()
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
         println "map!!!:::   " + map
		 println "proc service!!!::  " + processingService
         new JobRequest(email:params.email, job:job).save(flush: true, failOnError: true)
		 processingService.start(map)
      }

      render(view:"success", model: [filename:filename])
      
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
