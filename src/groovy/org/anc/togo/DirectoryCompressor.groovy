package org.anc.togo

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Adapted from http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
 * @author jestuart
 *
 */
class DirectoryCompressor
{
   public static void zip(File directory, File zipfile) throws IOException
   {
      URI base = directory.toURI()
      Deque<File> queue = new LinkedList<File>()
      queue.push(directory)
      OutputStream out = new FileOutputStream(zipfile)
      Closeable res = out
      try {
         ZipOutputStream zout = new ZipOutputStream(out)
         res = zout
         while (!queue.isEmpty()) {
            directory = queue.pop()
            for (File kid : directory.listFiles()) {
//               log.info("adding file ${kid.name} to zip")
               String name = base.relativize(kid.toURI()).getPath()
               if (kid.isDirectory()) {
                  queue.push(kid)
                  name = name.endsWith("/") ? name : name + "/"
                  zout.putNextEntry(new ZipEntry(name))
               } else {
                  zout.putNextEntry(new ZipEntry(name))
                  
                  InputStream input = new FileInputStream(kid)
                  try {
                     byte[] buffer = new byte[1024]
                     while (true) {
                        int readCount = input.read(buffer)
                        if (readCount < 0) {
                           break
                        }
                        zout.write(buffer, 0, readCount)
                     }
                  } finally {
                     input.close()
                  }
//                  this.copy(kid, zout)
                  zout.closeEntry()
               }
            }
         }
      } finally {
         res.close()
      }
   }

   private void copy(File file, OutputStream out) throws IOException {
      InputStream input = new FileInputStream(file)
      try {
         copy(input, out)
      } finally {
         input.close()
      }
   }
   
   private void copy(InputStream input, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024]
      while (true) {
         int readCount = input.read(buffer)
         if (readCount < 0) {
            break
         }
         out.write(buffer, 0, readCount)
      }
   }
}
