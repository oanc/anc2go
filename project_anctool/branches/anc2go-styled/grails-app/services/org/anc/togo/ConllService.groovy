package org.anc.togo

//import org.anc.togo.api.IProcessor;
import org.anc.togo.api.IProcessorService;
import org.anc.togo.db.*;
import org.anc.togo.ui.CorpusUIDescriptor;
import org.anc.togo.ui.ProcessorUIDescriptor;
import org.anc.tool.api.IProcessor

class ConllService implements IProcessorService
{
    final String name = "CoNLL"
    
    static transactional = true
    
    IProcessor processor

    def databaseService
    
    public ConllService()
    {
    }    
    
    public String getFileExtension() 
    {
       return "txt"
	 }
    
    public void initialize()
    {
       
    }
    
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
            cuid.annotations = databaseService.getAnnotations(corpus.name, name)
//            cuid.annotations = databaseService.getAllAnnotations(corpus.name, name)
            
//            log.info("ConllService tokens: " + databaseService.getTokens(corpus.name, name))
            
            descriptor.corpora << cuid
         }
 
         return descriptor
    }

   //Override
   public void setProcessor(IProcessor proc)
   {
      processor = proc
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
//         
//         descriptor.corpora << cd
//         
//         cd.annotations = databaseService.getAnnotations(corpus.name, name)
//      }
//      
//      return descriptor
//   }
    
}
