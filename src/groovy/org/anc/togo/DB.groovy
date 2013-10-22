package org.anc.togo

import java.io.File;
import org.anc.resource.ResourceLoader
import org.anc.togo.dsl.corporaDSL
import org.anc.togo.constants.*;
import org.anc.togo.db.AnnotationType;
import org.anc.togo.db.Corpus;
import org.anc.togo.db.Directory;
import org.anc.togo.db.Processor;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.anc.conf.*

import groovy.util.logging.*

@Log
class DB
{
   public static final List TOKENS = ['penn', 'hepple', 'biber', 'ptbtok', 'fntok', 'notoken']
   
   static init()
   {
      // AnnotationType must be initialized first since Corpus and Processor
      // have associations to AnnotationType.
      initAnnotationTypes()
      initProcessors()
      initCorpora()
   }
   
   static initAnnotationTypes()
   {
      if (!AnnotationType.count()) {
         [
            penn:['GATE Tokens with POS', 1],
            hepple:['GATE Tokens with POS', 2],
            biber:['Tokens w/ Biber POS', 3],
            ptbtok:['Penn TreeBank Tokens w/ POS', 4],
            fntok:['FrameNet Tokens w/ POS', 5],
            notoken:['No Tokens', 6],
            logical: ['Logical', 6],
            s:['Sentences', 7],
            nc:["Noun chunks", 8],
            vc:['Verb chunks', 9],
            ne:['Named entites', 10],
            ptb:['Penn TreeBank syntax', 11],
            fn:['FrameNet elements', 12],
            mpqa:['MPQA Opinion', 13],
            cb:['Committed Belief', 14]
         ].each() 
         { key,value ->
            def type = new AnnotationType()
            type.name = key
            type.description = value[0]
            type.sortOrder = value[1]
            type.save()
         }
         TOKENS.each()
         {
            AnnotationType type = AnnotationType.findByName(it)
            if (type)
            {
               type.isToken = true;
               type.save()
            }
            else
            {
               println("Token type $it not found.")
            }
         }
      }
   }
   
   static initProcessors()
   {
      if (!Processor.count())
      {
         def rdf = new Processor(name:'RDF', className:'org.anc.tool.rdf.RDFProcessor').save()
         
         def xml = new Processor(name:'XML', className:'org.anc.tool.xml.XMLProcessor')
         def uima = new Processor(name:'UIMA CAS', className:'org.anc.tool.uima.UimaProcessor')
         AnnotationType.list().each {
//            if (it.name != 'fn')
//            {
//               xml.addToTypes(it)
//            }
            xml.addToTypes(it)
            uima.addToTypes(it);
         }
         xml.save()
         uima.save();
         
         def nltk = new Processor(name:'NLTK (POS only)', className:'org.anc.tool.nltk.NLTKProcessor')
         def wordsmith = new Processor(name:'Word+POS', className:'org.anc.tool.wordsmith.WordSmithProcessor')
         
         TOKENS.each { type ->
            def token = AnnotationType.findByName(type)
            if (token)
            {
               if (type != 'notoken')
               {
                  nltk.addToTypes(token)
                  wordsmith.addToTypes(token)
               }
            }
            else
            {
               println "Unknown token type ${type}."
            }
         }
         nltk.save()
         wordsmith.save()
         
         def conll = new Processor(name:'CoNLL', className:'org.anc.tool.conll.ConllProcessor')
         ['s', 'nc', 'vc', 'ne', 'penn', 'hepple', 'biber', 'mpqa', 'cb', 'ptbtok', 'fntok'].each {
            def type = AnnotationType.findByName(it)
            if (type)
            {
               conll.addToTypes(type)
            }
            else
            {
               println("Encountered an unknown annotation type $it initializing the CoNLL processor.")
            }
         }
         conll.save()
      }

   }
   
   static initCorpora()
   {
      if (Corpus.count()) {
         return
      }
      
//      File corpScript = new File('conf/corpora/corpora.corp')
//      def corpora = corporaDSL.getCorporaListForScript(corpScript.getText())
      
      def corporaScript = ResourceLoader.loadString('conf/corpora/corpora.corp')
      def corpora = corporaDSL.getCorporaListForScript(corporaScript)
      /*
       *  Naming conventions in this part get confusing, because there are grails'
       *  database Directories but also Keith's corporaDSL Directories. (Same with
       *  Corpus). For clarity's sake, I'll disinguish between these by prefacing 
       *  grails Directories with a 'g' and the corporaDSL directories with a 'k'
       *
       *  Also: sorry about all the nesting - haven't discovered an iterative way
       *  to do these well.
       */
      corpora.each { kCorpus ->
//         println "corpus name: ${kCorpus.name}"
         Corpus gCorpus = new Corpus(name:kCorpus.name, root:kCorpus.root, ver:kCorpus.version, cid:kCorpus.cid)
         
         gCorpus.save(flush:true)
         
         // top level directions
         def corpDirectories = kCorpus.directories
         
         
         // At top of the corpus, directories are spoken/written
         corpDirectories.each { kDir ->
//            println "processing directory with name ${kDir.name}"
//            def dirList = []
//            Directory gDir = new org.anc.togo.db.Directory()
//            gDir.name = kDir.name
//            gDir.type = kDir.textClass
//            gDir.textClass = kDir.textClass
//            gDir.path = kDir.path            
            
            // Next level directories are genres
            def kGenreDirs = kDir.directories
//            println kGenreDirs.size()
            kGenreDirs.each { kGenreDir ->
//               println "\tgenre directory: ${kGenreDir.name}"
               def genreDirList = []
               Directory gGenreDir = new org.anc.togo.db.Directory()
               gGenreDir.name = kGenreDir.name
               gGenreDir.type = kGenreDir.textClass
               gGenreDir.textClass = kDir.name
               gGenreDir.path = kGenreDir.path
               gGenreDir.did = kGenreDir.did
//               dirList << gGenreDir
               
               // Don't really need this.
//               // Next level in is sub-genres
//               def kSubGenreDirs = kGenreDir.directories
//               kSubGenreDirs.each { kSubGenreDir ->
//                  println "\t\tsubgenre directory: ${kGenreDir.name}"
//                  Directory gSubGenreDir = new org.anc.togo.db.Directory()
//                  gSubGenreDir.name = kGenreDir.name
//                  gSubGenreDir.type = kGenreDir.textClass
//                  gSubGenreDir.textClass = kGenreDir.textClass
//                  gSubGenreDir.path = kGenreDir.path
//                  genreDirList << gSubGenreDir
//                  
//                  // Wrap up subgenre directory
//                  gSubGenreDir.save()
//                  gCorpus.addToDirectories(gSubGenreDir)
//               }
               
               // Wrap up genre directory
               gGenreDir.directories = genreDirList
               gGenreDir.save()
               gCorpus.addToDirectories(gGenreDir)
            }
            
            // Wrap up spoken/written directory
//            gDir.directories = dirList
//            gDir.save()
//            gCorpus.addToDirectories(gDir)
         }
         
         // Hack for types
         if (gCorpus.name.equals('OANC'))
         {
//            println 'Found OANC!'
            ['s', 'logical', 'ne', 'hepple', 'biber', 'nc', 'vc'].each {
               def t = AnnotationType.findByName(it)
               if (t)
               {
                  gCorpus.addToTypes(t)
               }
               else
               {
                  println "Unable to add annotation type $it to the OANC"
               }
            }
         }
         else if (gCorpus.name.equals('MASC'))
         {
//            println 'Found MASC!'
            ['s', 'logical', 'ne', 'nc', 'vc', 'mpqa', 'cb', 'ptb', 'ptbtok', 'fn', 'fntok', 'penn'].each {
               AnnotationType type = AnnotationType.findByName(it)
               if (type)
               {
                  gCorpus.addToTypes(type)
               }
               else
               {
                  println("Unable to create CorpusTypes for $type")
               }
            }
         }
         
         
         // Wrap up corpus
         gCorpus.save(flush:true)
      }
//      if (Corpus.count()) {
//         return
//      }
//      Corpus masc = new Corpus(name:"MASC", root:Globals.PATH.MASC, ver:"1.0.3")
//      Corpus oanc = new Corpus(name:"OANC", root:Globals.PATH.OANC, ver:"1.1")
//      
//      masc.save(flush:true)
//      oanc.save(flush:true)
//      
//      
//      Directory written = new Directory()
//      written.name = "written"
//      written.type = "written"
//      written.textClass = "written"
//      written.path = "written"
//      written.size = 55
//      written.save()
//      masc.addToDirectories(written)
//
//      Directory spoken = new Directory()
//      spoken.name = "spoken"
//      spoken.type = "spoken"
//      spoken.textClass = "spoken"
//      spoken.path = "spoken"
//      spoken.size = 10
//      spoken.save()
//      masc.addToDirectories(spoken)
//         
//      ['s', 'logical', 'ne', 'nc', 'vc', 'mpqa', 'cb', 'ptb', 'ptbtok', 'fn', 'fntok', 'penn'].each {
//         AnnotationType type = AnnotationType.findByName(it)
//         if (type)
//         {
//            masc.addToTypes(type)
//         }
//         else
//         {
//            println("Unable to create CorpusTypes for $type")
//         }
//      }
//      masc.save(flush:true)
//      
//      insert(oanc, "charlotte", "spoken", "spoken/face-to-face/charlotte")
//      insert(oanc, "switchboard", "spoken", "spoken/telephone/switchboard")
//      insert(oanc, "eggan", "fiction", "written_1/fiction/eggan")
//      insert(oanc, "slate", "journal", "written_1/journal/slate")
//      insert(oanc, "verbatim", "journal", "written_1/journal/verbatim")
//      insert(oanc, "icic", "letters", "written_1/letters/icic")
//      ['s', 'logical', 'ne', 'hepple', 'biber', 'nc', 'vc'].each {
//         def t = AnnotationType.findByName(it)
//         if (t)
//         {
//            oanc.addToTypes(t)
//         }
//         else
//         {
//            println "Unable to add annotation type $it to the OANC"
//         }
//      }
//      oanc.save(flush:true)
      println 'Done initializing corpora'
   }
   
   static protected void insert(Corpus corpus, String name, String type, String path) {
      Directory dir = new Directory()
      dir.name = name
      dir.type = type
      dir.textClass = type
      dir.path = path
      File root = new File(corpus.getRoot() + '/data/' + path)
      if (root.exists()) {
         dir.size = countFiles(root)
      }
      else {
         println("Directory not found " + root.getPath())
      }
      dir.save()
      corpus.addToDirectories(dir)
   }

   static protected int countFiles(File directory) {
      int count = 0;
      directory.eachFile {
         if (it.isFile()) {
            if (it.getName().endsWith(".anc")) {
               ++count
            }
         }
//         else if (it.isDirectory()) {
//            count += countFiles(it)
//         }
      }
      return count
   }

}