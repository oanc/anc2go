package org.anc.togo.constants;

import java.io.*;

public class Globals
{
   public static final TimConstants PATH = new TimConstants();
   public static final MailConstants SMTP = new MailConstants();
   
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      try
      {
         PATH.save();
         SMTP.save();
         System.out.println("Global constants saved.");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

}
