package org.anc.togo

import grails.test.*

class ConllServiceTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
       UIDescriptorParser parser = new UIDescriptorParser()
       XmlService service = new XmlService()
       Map desc = service.getUIDescriptor()
       parser.parse(desc)
    }
}
