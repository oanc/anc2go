package org.anc.togo

import org.anc.togo.api.IProcessorService;
import org.anc.togo.db.Processor;

class ProcessorBase
{
   protected IProcessorService processor 
   
   protected void init(String processorName)
   {
      Processor proc = Processor.findByName(processorName)
      if (proc) try
      {
         System.out.println("Attempting to load class " + proc.className);
         Class theClass = Class.forName(proc.className)
         if (theClass)
         {
            processor = theClass.newInstance()
         }
      }
      catch (Exception e)
      {
         System.out.println("Unable to load class for " + processorName);
         // TODO log this failure.
      }
   }
}
