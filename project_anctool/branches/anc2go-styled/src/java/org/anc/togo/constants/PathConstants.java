package org.anc.togo.constants;

import org.anc.constants.*;

public class PathConstants extends Constants 
{
	@Default("/var/corpora/OANC-1.2b1")
	public final String OANC = null;
	
	@Default("/var/corpora/MASC-3.0.0-RC1")
	public final String MASC = null;
	
	@Default("/usr/share/anc2go/work")
	public final String WORK = null;
		
	@Default("/usr/share/anc2go/cache")
	public final String CACHE = null;

	@Default("/usr/share/anc2go/download")
	public final String DOWNLOADS = null;
	
	@Default("/usr/share/anc2go/conf")
	public final String CORPORA_CONF = null;
	
	@Default("/usr/share/anc2go/processors")
	public final String PROCESSORS = null;
	
	@Default("/var/corpora/MASC-3.0.0/resource-header.xml")
	public final String RESOURCE_HEADER = null;
	
	public PathConstants()
	{
		super.init();
	}
}
