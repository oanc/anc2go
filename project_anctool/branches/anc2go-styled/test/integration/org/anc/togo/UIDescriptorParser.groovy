package org.anc.togo

import org.anc.togo.api.IProcessorService;

class UIDescriptorParser
{
   void parse(Map desc)
   {
      desc.each { name, cd ->
         println "Key : ${name}"
         println "Name: ${cd.name}"
         println "ID  : ${cd.id}"
         println "Title : ${cd.title}"
         if (cd.tokens)
         {
            println "Tokens:"
            cd.tokens.each {
               println "\t${it.description}"               
            }
         }
         if (cd.annotations)
         {
            println "Annotations:"
            cd.annotations.each {
               println "\t${it.description}"
            }
         }
         if (cd.options)
         {
            println "Options:"
            cd.options.each { option ->
               println "\tType: ${option.type}"
               println "\tTitle: ${option.title}"
               if (option.items)
               {
                  option.items.each { item ->
                     print "\tItem ${item.id}: ${item.label}"
                     if (item.default)
                     {
                        print " (default)"
                     }
                     println ""
                  }
               }
               else
               {
                  println "ERROR: Option has no items!"
               }
            }
         }
      }
   }
}
