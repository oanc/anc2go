package org.anc.togo.api;

/**
 * Exception type to be thrown by processors during initialization.
 * 
 * @author Keith Suderman
 *
 */
public class InitializationException extends ProcessorException
{

   public InitializationException()
   {
   }

   public InitializationException(String message)
   {
      super(message);
   }

   public InitializationException(Throwable cause)
   {
      super(cause);
   }

   public InitializationException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
