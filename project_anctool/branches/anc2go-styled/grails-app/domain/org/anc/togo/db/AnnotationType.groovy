package org.anc.togo.db

class  AnnotationType implements Comparable {
	String name
	String description
	boolean isToken
	boolean isSentence
	int sortOrder
   
	String toString() { description }
	int compareTo(Object object)
   {
      if (!(object instanceof AnnotationType))
      {
         return 0
      }
      AnnotationType at = (AnnotationType) object
      return sortOrder - at.sortOrder
   }
   
    static constraints = {
		name blank:false, unique:true
		description blank:false
		isToken default:false
		isSentence default:false
      sortOrder default:100
    }
}
