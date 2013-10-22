package org.anc.togo.db

class Corpus implements Comparable<Corpus>{
	String name
	String root
	String ver
	String cid
   
	String toString()
	{
		return "$name v$ver"
	}
	
	static hasMany = [ directories: Directory, types:AnnotationType ]
//	static fetchMode = [directories:'eager', types:'eager']
   
    static constraints = {
		name blank:false, unique:true
		root blank:false
		ver blank:false
      cid blank:false, unique:true
    }


   @Override
   public int compareTo(Corpus o)
   {
      return this.name.compareTo(((Corpus) o).name)
   }}
