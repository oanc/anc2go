package org.anc.togo

import java.io.File;

import org.anc.togo.api.IProcessorService;
import org.anc.togo.db.Corpus;
import org.anc.togo.ui.*;

class RdfService implements IProcessorService
{
   final String name = 'RDF'

   static transactional = true

   def databaseService
   
   public RdfService()
   {
   }
   
   String getFileExtension() { return "rdf" }
   
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
           cuid.notes = []
           cuid.notes << "The RDF processor inlcudes all available annotations in the " +
           "output files. There is nothing to select."

//           cuid.tokens = databaseService.getTokens(corpus.name, name)
//           cuid.annotations = databaseService.getAnnotations(corpus.name, name)
//           cuid.options = []
//           
           descriptor.corpora << cuid
        }
        return descriptor 
   }   
}
