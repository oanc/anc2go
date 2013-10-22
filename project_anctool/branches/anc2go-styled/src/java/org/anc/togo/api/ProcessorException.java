package org.anc.togo.api;

public class ProcessorException extends Exception
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public ProcessorException()
   {
   }

   public ProcessorException(String message)
   {
      super(message);
   }

   public ProcessorException(Throwable cause)
   {
      super(cause);
   }

   public ProcessorException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
