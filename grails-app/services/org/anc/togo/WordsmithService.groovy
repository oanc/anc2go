package org.anc.togo

import java.util.Map;

import org.anc.togo.api.*;
import org.anc.togo.db.Corpus;
import org.anc.togo.ui.CorpusUIDescriptor;
import org.anc.togo.ui.ProcessorUIDescriptor;

class WordsmithService implements IProcessorService  
{
   final String name = 'Word+POS'
   
    static transactional = true

    def databaseService
        
    public WordsmithService()
    {
    }
    
    String getFileExtension() { return "txt" }
    
    void initialize() { }
    
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
//    Map getUIDescriptor()
//    {
//       def descriptor = [:]
//       descriptor.name = name
//       descriptor.corpora = []
//       Corpus.list().each { corpus ->
//          def cd = [:]
//          
//          cd.title = corpus.toString();
//          cd.name = corpus.name
//          cd.id = corpus.id
//          
//          descriptor.corpora << cd
//          
//          cd.tokens = databaseService.getTokens(corpus.name, name)
//       }
//       
//       return descriptor
//    }
 
}
