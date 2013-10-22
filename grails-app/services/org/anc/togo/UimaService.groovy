package org.anc.togo

import java.io.File;

import org.anc.togo.ui.ProcessorUIDescriptor;

import org.anc.togo.ui.*
import org.anc.togo.api.*
import org.anc.togo.db.Corpus;

class UimaService implements IProcessorService
{
   final String name = 'UIMA CAS'

   static transactional = true

   def databaseService
   
   public UimaService()
   {
   }
   
   String getFileExtension() { return "xmi" }
   
   void initialize()
   {
      
   }
   
   void process(File input, File output)
   {
      log.info("Processing ${input.getPath()}")
   }

   ProcessorUIDescriptor getUIDescriptor()
   {
        ProcessorUIDescriptor descriptor = new ProcessorUIDescriptor()
        descriptor.name = name
        descriptor.corpora = []
        Corpus.list().each { corpus ->
           CorpusUIDescriptor cuid = new CorpusUIDescriptor()
           cuid.corpusId = corpus.id
           cuid.name = corpus.name
           cuid.tokens = databaseService.getTokens(corpus.name, name)
           cuid.annotations = databaseService.getAnnotations(corpus.name, name)

           descriptor.corpora << cuid
        }
        return descriptor 
   }   
}
