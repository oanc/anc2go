package org.anc.togo

import grails.test.*

class DatabaseServiceTests extends GroovyTestCase {
   
   DatabaseService databaseService
   XmlService xmlService
   
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testService() {
       assertTrue("The DatabaseService has not been injected.", databaseService != null)
    }
    
    void testGetTokens() { 
       def test = { corpus, processor ->
          databaseService.getTokens(corpus, processor)
       }
       checkAll(test)
    }
    
    void testGetAnnotations() {
       def test = { corpus, processor ->
          databaseService.getAnnotations(corpus, processor)
       }
    }
    
    void testGetAllAnnotations() {
       def test = { corpus, processor ->
          databaseService.getAllAnnotations
       }
    }
    
    void testXmlService() {
       assertTrue(xmlService != null)
       def descriptor = xmlService.getUIDescriptor()
       new UIDescriptorParser().parse(descriptor)
    }
    
    private void checkAll(test)
    {
       ['OANC', 'MASC'].each { corpus ->
          ['XML', 'NLTK', 'Wordsmith', 'CoNLL'].each { proc ->
             def result = test(corpus, proc)
             assertTrue("${corpus} ${proc} failed test.", result && (result.size() > 0))
          }
       }
    }
}
