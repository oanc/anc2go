package org.anc.togo.constants;

import org.anc.constants.*;

public class TimConstants extends Constants 
{
	@Default("/var/corpora/OANC-1.2b1")
	public final String OANC = null;
	
	@Default("/Users/Tim/Documents/ANC2Go/MASC-3.0.0")
	public final String MASC = null;
	
	@Default("/Users/Tim/Documents/ANC2Go/work")
	public final String WORK = null;
		  
	
	
	@Default("/Users/Tim/Documents/ANC2Go/cache")
	public final String CACHE = null;

	@Default("/Users/Tim/Documents/ANC2Go/download")
	public final String DOWNLOADS = null;
	
	@Default("/Users/Tim/Documents/ANC2Go/conf")
	public final String CORPORA_CONF = null;
	
	@Default("/Users/Tim/Development/Grails/ANC2GoMulti/processors")
	public final String PROCESSORS = null;
	
	@Default("/Users/Tim/Documents/ANC2Go/MASC-3.0.0/resource-header.xml")
	public final String RESOURCE_HEADER = null;
	
	public TimConstants()
	{
		super.init();
	}
}
