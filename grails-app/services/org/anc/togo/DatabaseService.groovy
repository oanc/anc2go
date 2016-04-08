package org.anc.togo

import org.anc.togo.db.AnnotationType;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Processor;

class DatabaseService {

    static transactional = true

    def serviceMethod() {

    }
    
    def getJob(String key)
    {
       Job.findByKey(key)
    }    
    
    List getTokens(String corpusName, String processorName)
    {
//       log.info("Getting tokens for corpus ")
       
       def tokens = AnnotationType.findAllByIsToken(true)
       def ctypes = Corpus.findByName(corpusName)?.types
       def ptypes = Processor.findByName(processorName)?.types
       if (!ptypes) return null
       if (!ctypes) return null
       ctypes = ctypes.intersect(tokens)
       ctypes = ctypes.intersect(ptypes).toList()
       Collections.sort(ctypes)
       return ctypes
    }
    
    List getAnnotations(String corpus, String processor)
    {
       def tokens = AnnotationType.findAllByIsToken(false)
       def ctypes = Corpus.findByName(corpus).types
       ctypes = tokens.intersect(ctypes)
       
       def ptypes = Processor.findByName(processor).types
       ctypes = ctypes.intersect(ptypes).toList()
       Collections.sort(ctypes)
       return ctypes
    }
    
    List getAllAnnotations(String corpus, String processor)
    {
       def ctypes = Corpus.findByName(corpus).types
       def ptypes = Processor.findByName(processor).types
       ctypes = ctypes.intersect(ptypes).toList()
       Collections.sort(ctypes)
       return ctypes
    }
 
}
