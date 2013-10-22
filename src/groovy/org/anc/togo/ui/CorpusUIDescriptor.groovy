package org.anc.togo.ui

import java.util.List;

import org.anc.togo.db.AnnotationType;

class CorpusUIDescriptor
{
   int corpusId
   String name
   List notes
   List<AnnotationType> tokens
   List<AnnotationType> annotations
   List<Option> options
}
