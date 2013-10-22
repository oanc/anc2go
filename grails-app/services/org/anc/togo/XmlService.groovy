package org.anc.togo

import org.anc.conf.AnnotationConfig
import org.anc.conf.AnnotationSpaces.*
import org.anc.togo.api.IProcessorService;
import org.anc.togo.db.Corpus;
import org.anc.togo.ui.CorpusUIDescriptor;
import org.anc.togo.ui.Item;
import org.anc.togo.ui.Option
import org.anc.togo.ui.ProcessorUIDescriptor;
import org.anc.togo.xml.EchoXmlHandler
import org.anc.togo.*
import org.anc.tool.xml.*
import org.anc.tool.api.*
import org.xces.graf.*

class XmlService implements IProcessorService //extends ProcessorBase implements IProcessorService
{
   final String name = 'XML'

   static transactional = true

   def databaseService

   private AnnotationConfig config = null;
   private String overlap = null;
   
   public XmlService()
   {
   }
   
   String getFileExtension() { return "xml" }
   
   void initialize() { }
   
   void process(File input, File output)
   {
      log.info("Processing ${input.getPath()}")
   }

   ProcessorUIDescriptor getUIDescriptor()
   {
        ProcessorUIDescriptor descriptor = new ProcessorUIDescriptor()
        descriptor.name = name
        descriptor.corpora = []
        Corpus.list().each { corpus ->
           CorpusUIDescriptor cuid = new CorpusUIDescriptor()
           cuid.corpusId = corpus.id
           cuid.name = corpus.name
           cuid.tokens = databaseService.getTokens(corpus.name, name)
           cuid.annotations = databaseService.getAnnotations(corpus.name, name)
           cuid.options = []
           
           Option overlap = new Option(title:'Overlap Mode', name:'overlap', type:Option.MUTEX)
           overlap.items = []
           overlap.items << new Item(itemId:0, label:'Discard', checked:true)
           overlap.items << new Item(itemId:1, label:'Nest')
           overlap.items << new Item(itemId:2, label:'Milestone')
          
           cuid.options << overlap

           cuid.notes = []
           cuid.notes << "Not all combinations of annotations result in well-formed XML files." +
              "For example, both the logical and Penn TreeBank annotations define hierarchies (trees) " +
              "of annotations, and it is likely that a combination of these two annotation types will " +
              "result in hierarchies that overlap and cannot be properly nested using XML tags. The XML " +
              "processor will attempt to resolve simple problems with overlapping annotations using the " +
              "overlap mode chosen below, but it may not be possible to fully resolve all such instances."
              
//           cuid.notes << "GATE tokens: Tokens with lemma and <a href=\"http://www.anc.org/pennpostags\">" +
//              "Penn Treebank Part of speech tags</a> produced by " +
//              "<a href=\"http://gate.ac.uk/sale/tao/splitch6.html#x9-1360006.6\">GATE's ANNIE Part of Speech Tagger</a>."
//           cuid.notes << "PTB Tokens: Tokens with <a href=\"http://www.anc.org/pennpostags\">Penn Treebank Part of speech tags" +
//              "</a> produced by the <a href=\"http://www.cis.upenn.edu/~treebank/\">Penn Treebank project</a>."
//           cuid.notes << "FN Tokens: Tokens with <a href=\"http://www.anc.org/pennpostags\">Penn Treebank Part of speech tags</a> " +
//              "produced by the <a href=\"https://framenet.icsi.berkeley.edu/fndrupal/\">FrameNet project</a>."
           
           
//           cuid.notes <<  "Not all combinations of anntotaions will result in " +
//              "well formed XML files. In particular the <i>logical</i> and " +
//              "<i>Penn TreeBank</i> both define hierarchies (trees) of annotations " +
//              "and it is likely that merging the two annotation types will result in a " +
//              "malformed XML file."
//           cuid.notes << "The XML processor will attempt to resolve simple problems " +
//              "with overlapping annotations. But it can not, in general, magically " +
//              "turn invalid heirarchies of annotations into valid XML. Some trial " +
//              "and error may be required."
           descriptor.corpora << cuid
        }
        return descriptor 
   }   
//   Map getUIDescriptor()
//   {
//      def descriptor = [:]
//      descriptor.name = name
//      descriptor.corpora = []
//      Corpus.list().each { corpus ->
//         def cd = [:]
//         
//         cd.title = corpus.toString();
//         cd.name = corpus.name
//         cd.id = corpus.id
//         
//         cd.tokens = databaseService.getTokens(corpus.name, 'XML')
//         cd.annotations = databaseService.getAnnotations(corpus.name, 'XML')
//         
//         cd.options = []
//         def option = [:]
//         option.type = 'mutex'
//         option.title = 'Overlap Mode'
//         option.items = [
//               [id:1, label:'Discard', default:true],
//               [id:2, label:'Overlap'],
//               [id:2, label:'Milestone']
//         ]
//         cd.options.add(option)
//         
//         descriptor.corpora << cd
//
//      }
//      
//      return descriptor
//   }
}
