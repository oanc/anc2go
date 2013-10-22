package org.anc.togo

import org.anc.togo.api.IProcessorService;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Processor;
import org.anc.togo.ui.CorpusUIDescriptor;
import org.anc.togo.ui.ProcessorUIDescriptor;
import org.anc.tool.api.IProcessor

class NltkService implements IProcessorService
{
   final String name = 'NLTK (POS only)'
   
   static transactional = true
   
   IProcessor processor

   def databaseService
   
   public NltkService()
   {
   }
   
   void setProcessor(IProcessor p) {
		processor = p
	}
   
   String getFileExtension() { return "txt" }
   void initialize() {}
   
   void process(File input, File output)
   {
      
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

           descriptor.corpora << cuid
        }

        return descriptor
   }

//   Map getUIDescriptor()
//   {
//      def descriptor = [:]
//      descriptor.name = name
//      descriptor.corpora = []
//      Corpus.list().each { corpus ->
//         def cd = [:]
//         
//         cd.title = corpus.toString();
//         cd.name = corpus.name
//         cd.id = corpus.id
//         cd.tokens = databaseService.getTokens(corpus.name, name)
//         descriptor.corpora << cd
//        
//      }
//      
//      return descriptor
//   }
}
