package org.anc.togo.db

class Processor
{
   String name
   String className
   
   String toString() { name }
   
   static hasMany = [types:AnnotationType]
//   static fetchMode = [types:'eager']
   static constraints =
   {
      name blank:false, unique:true
   }
}
