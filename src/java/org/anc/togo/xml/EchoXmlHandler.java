package org.anc.togo.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import org.anc.Sys;
import org.anc.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX Handler that simply echoes the XML document to an OutputStream.

 * @author Keith Suderman
 * @version 1.0
 */
public class EchoXmlHandler extends DefaultHandler
{
   /** Default character encoding to use if none is specified. */
   public static String DEFAULT_ENCODING = "UTF-8";
   
   /**
    * Namespace prefixes collected by the <code>startPrefixMapping</code>
    * method.
    */
   protected LinkedList<PrefixNamespace> namespaces = new LinkedList<PrefixNamespace>();

   /** Where to write the XML. */
   protected Writer writer = null;

   /** Character encoding to use when writing the output file. Defaults
    *  to UTF-8.
    */
   protected String encoding = "UTF-8";

   private String defaultNamespace = null;
   
   /** Set to true when a tag has been started and is used to detect empty
    * elements. This is set in {@link #startElement} and checked in
    * {@link #characters} and {@link #endElement}
    */
   private boolean tagOpen = false;

   private Comparator<Feature> sorter = new FeatureSorter();

   /** Default constructor.  The XML will be serialized to System.out. */
   public EchoXmlHandler() throws UnsupportedEncodingException
   {
      this(System.out);
   }

   /**
    * XML file will be serialized to <tt>stream</tt>.
    *
    * @param stream The stream to write the XML to.
    */
   public EchoXmlHandler(OutputStream stream) throws
       UnsupportedEncodingException
   {
      this(stream, DEFAULT_ENCODING);
   }

   public EchoXmlHandler(OutputStream stream, String encoding) throws
       UnsupportedEncodingException
   {
      this.encoding = encoding;
      writer = new OutputStreamWriter(stream, encoding);
   }

   public EchoXmlHandler(Writer writer) throws UnsupportedEncodingException
   {
      this.writer = writer;
   }

   /** Set the stream used for output. */
   public void setOutputStream(OutputStream stream) throws
       UnsupportedEncodingException
   {
      writer = new OutputStreamWriter(stream, encoding);
   }

   public void setDefaultNameSpace(String uri)
   {
      this.defaultNamespace = uri;
   }
   
   /** Clear namespace declarations so the handler can be reused on multiple
    *  documents.
    */
   @Override
   public void startDocument() throws SAXException
   {
      namespaces.clear();
      emit("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" +
           System.getProperty("line.separator"));
      tagOpen = false;
   }

   /** Flush the output stream. */
   @Override
   public void endDocument() throws SAXException
   {
      try
      {
         writer.flush();
      }
      catch (IOException ex)
      {
         throw new SAXException("Unable to flush the output stream", ex);
      }
   }

   /**
    * Writes the string to the output stream followed by a line separator. The
    * line separator to use is obtained from the Java system property. Re-throws
    * any IOExceptions as SAXExceptions
    */
   protected void emitln(String s) throws SAXException
   {
      try
      {
         writer.write(s);
         writer.write(Sys.EOL);
         writer.flush();
      }
      catch (IOException e)
      {
         throw new SAXException(e);
      }
   }
   
   /**
    * Writes a line separator to the output stream. The line separator to use 
    * is obtained from the Java system property. Re-throws any IOExceptions 
    * as SAXExceptions
    */
   protected void emitln() throws SAXException
   {
      try
      {
         writer.write(Sys.EOL);
         writer.flush();
      }
      catch (IOException e)
      {
         throw new SAXException(e);
      }
   }
   
   /**
    * Writes the string to the output stream and re-throws any IOExceptions as
    * SAXExceptions
    *
    * @param s The string to write to the output stream.
    */
   protected void emit(String s) throws SAXException
   {
      try
      {
         writer.write(s);
         writer.flush();
      }
      catch (IOException ex)
      {
         throw new SAXException("Unable to write the strings: " + s, ex);
      }
   }

   /**
    * Writes the string to the output stream and re-throws any IOExceptions as
    * SAXExceptions
    */
   protected void emit(char[] ch, int start, int end) throws SAXException
   {
      try
      {
         writer.write(ch, start, end);
         writer.flush();
      }
      catch (IOException ex)
      {
         throw new SAXException("Unable to write the strings: " +
                                new String(ch, start, end),
                                ex);
      }
   }

   protected boolean isDefaultNamespace(String uri)
   {
      return (defaultNamespace != null) && uri.equals(defaultNamespace);
   }
   
   /**
    * Writes characters from the array to the output stream and re-throws any
    * IOExceptions as SAXExceptions. The characters in the array between indices
    * <code>start</code> and <code>start + length - 1</code> are serialized to
    * the output stream.
    *
    * @param ch     The array of characters to write to the output stream
    * @param start  The index of the first character to write.
    * @param length The number of characters to write to the output stream
    * @throws SAXException Rethrows all IOExceptions as SAXExceptions
    */
   @Override
   public void characters(char[] ch, int start, int length) throws SAXException
   {
      if (tagOpen)
      {
         emit(">");
         tagOpen = false;
      }
      emit(ch, start, length);
   }

   /**
    * Prints the end element tag.
    *
    * @param uri        The namespace of the element.
    * @param localName  The name of the element without the namespace prefix.
    * @param qName      The name of the element including the namespace prefix,
    *                   if any.
    * @throws SAXException
    */
   @Override
   public void endElement(String uri, String localName, String qName) throws
       SAXException
   {
      if (tagOpen)
      {
         emit("/>");
      }
      else
      {
         if (isDefaultNamespace(uri))
         {
            emit("</" + localName + ">");
         }
         else
         {
            emit("</" + qName + ">");
         }
      }
      tagOpen = false;
   }

   /**
    * Prints the start tag and any attributes.  Namespace prefixes that have
    * been collected by the <code>startPrefixMapping</code> are added as
    * attributes as well.
    *
    * @param uri         The namespace URI of the element.
    * @param localName   The name of the element without any namespace prefix.
    * @param qName       The name of the element with the namespace prefix, if any.
    * @param attributes  The attributes included with the element.
    * @throws SAXException
    */
   @Override
   public void startElement(String uri,
                            String localName,
                            String qName,
                            Attributes attributes) throws SAXException
   {
      if (tagOpen)
      {
         emit(">");
      }
      if (isDefaultNamespace(uri))
      {
         emit("<" + localName);
      }
      else
      {
         emit("<" + qName);
      }
      int n = attributes.getLength();
      Feature[] features = new Feature[n];
      for (int i = 0; i < n; ++i)
      {
         String name = attributes.getQName(i);
         String value = attributes.getValue(i);
         features[i] = new Feature(name, value);
      }
      Arrays.sort(features, sorter);
      for (Feature f : features)
      {
         String name = f.getFirst();
         if (name != null && name.length() > 0)
         {
            emit(" " + f.getFirst() + "=\"" + encode(f.getSecond()) + "\"");
         }
      }
      if (namespaces.size() > 0)
      {
         Iterator<PrefixNamespace> it = namespaces.iterator();
         while (it.hasNext())
         {
            PrefixNamespace ns = (PrefixNamespace) it.next();
            if (ns.prefix == null || "".equals(ns.prefix))
               emit(" xmlns=\"" + ns.namespace + "\"");
            else
               emit(" xmlns:" + ns.prefix + "=\"" + ns.namespace + "\"");
         }
      }
      tagOpen = true;
      namespaces.clear();
   }

   /**
    * Add the prefix mapping to the list of prefix mappings.  These
    * namespace/prefix mappings will be added to the next element encountered.
    *
    * @param prefix  The prefix mapped to the namespace URI.
    * @param uri     The namespace URI mapped to the prefix.
    * @throws SAXException
    */
   @Override
   public void startPrefixMapping(String prefix, String uri) throws
       SAXException
   {
//      System.out.println("mapping " + prefix + " = \"" + uri + "\"");
      namespaces.add(new PrefixNamespace(prefix, uri));
   }

   /**
    * Removes the prefix from the list of namespace mappings.
    *
    * @param prefix String the prefix to remove.
    */
   @Override
   public void endPrefixMapping(String prefix)
   {
      namespaces.remove(prefix);
   }

   /** Simple conversion routine to handle XML entities. */
   protected String encode(String s)
   {
      s = s.replaceAll("&", "&amp;");
      s = s.replaceAll("\"", "&quot;");
      s = s.replaceAll("<", "&lt;");
      s = s.replaceAll(">", "&gt;");
      return s;
   }

}

/**
 * A very simple "Pair" class used to hold a namespace URI and the prefix it
 * is mapped to.  All fields are public with no accessor methods provided.
 *
 * @author Keith Suderman
 * @version 1.0
 */
class PrefixNamespace
{
   /** The prefix mapped to a namespace URI. */
   public String prefix = null;
   /** The namespace URI mapped to the prefix. */
   public String namespace = null;

   /** A constructor to initialize both fields at once. */
   public PrefixNamespace(String prefix, String uri)
   {
      this.prefix = prefix;
      namespace = uri;
   }
}

class Feature extends Pair<String, String>
{
   public Feature(String name, String value)
   {
      super(name, value);
   }
}

class FeatureSorter implements Comparator<Feature>
{
   public int compare(Feature lhs, Feature rhs)
   {
      return lhs.getFirst().compareTo(rhs.getFirst());
   }
}
