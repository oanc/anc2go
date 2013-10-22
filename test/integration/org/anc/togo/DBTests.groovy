package org.anc.togo

import org.anc.togo.db.AnnotationType;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Processor;

import grails.test.*

class DBTests extends GroovyTestCase {
   // Expected test results
   final int EXPECTED_CORPUS_SIZE = 2
   final int EXPECTED_PROCESSOR_SIZE = 6
   
    protected void setUp() {
        super.setUp()
//        DB.init()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCorpusSize() {
       int size = Corpus.list().size()
       assertTrue("# Corpora is ${size}. Expected ${EXPECTED_CORPUS_SIZE}", size == EXPECTED_CORPUS_SIZE)
    }
    
    void testProcessorSize() {
       int size = Processor.list().size()
       assertTrue("# Processors is ${size}. Expected ${EXPECTED_PROCESSOR_SIZE}", size == EXPECTED_PROCESSOR_SIZE)
    }
    
    void testAnnotationTypes() {
       int size = AnnotationType.list().size()
       println "AnnotationType size is ${size}"   
       assertTrue(size > 5)
    }
    
    void testCorpusOanc()
    {
//       assertTrue("Can not load OANC from the database.", Corpus.findByName("OANC"))
//       assertTrue("Can not load MASC from the database.", Corpus.findByName("MASC"))
    }
    
    void testProcessors()
    {
//       ['XML', 'NLTK', 'Wordsmith', 'CoNLL'].each {
//          assertTrue("Could not load processor ${it}", Processor.findByName(it))
//       }   
    }    
}
