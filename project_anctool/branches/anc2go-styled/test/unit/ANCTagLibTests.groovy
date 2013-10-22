

import grails.test.*

class ANCTagLibTests extends TagLibUnitTestCase
{

   ANCTagLib anc
   protected void setUp()
   {
      super.setUp()
      anc = new ANCTagLib()
   }

   protected void tearDown()
   {
      super.tearDown()
   }

   void testCopyright()
   {
      println anc.copyright()
   }
   
   void testHome()
   {
      println anc.home()
   }
}
