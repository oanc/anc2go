package org.anc.togo.constants;

import java.io.IOException;

import org.anc.constants.Constants;

public class MailConstants extends Constants
{
	@Default("jestuart@vassar.edu")
	public final String DEFAULT_ADDRESS = null;
	
	@Default("smtp.vassar.edu")
	public final String HOST = null;
	
	@Default("jestuart")
	public final String USER = null;
	
	@Default("password")
	public final String PASSWORD = null;
	
	@Default("anc@anc.org")
	public final String FROM = null;
	
	public MailConstants()
	{
		init();
	}
	
	public static void main(String[] args)
	{
		try {
			new MailConstants().save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
