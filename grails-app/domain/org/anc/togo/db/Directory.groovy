package org.anc.togo.db

class Directory implements Comparable<Directory> {
   List<Directory> directories
   String name
	String type
	String textClass
	String path
   String did
	int size
	
	String toString() { name }
	
	static belongsTo = [ corpus:Corpus]
	
    static constraints = {
		name blank:false // unique:true
		type blank:false
		textClass blank:false
		path blank:false // unique:true
      did blank:false // unique:true
    }


   //Override
   public int compareTo(Directory o)
   {
      if (this.textClass.equals(((Directory) o).textClass))
      {
         return this.name.compareTo(((Directory) o).name)
      }
      else
      {
         return this.textClass.compareTo(((Directory) o).textClass)
      }
   }
   
}
