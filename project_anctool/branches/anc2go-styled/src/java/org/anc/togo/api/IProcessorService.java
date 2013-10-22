package org.anc.togo.api;

import java.io.File;

import org.anc.togo.ui.ProcessorUIDescriptor;
import org.anc.tool.api.IProcessor;

public interface IProcessorService 
{
//   void setProcessor(IProcessor proc);
   String getName();
   String getFileExtension();
   ProcessorUIDescriptor getUIDescriptor();
   void process(File input, File output);
}
