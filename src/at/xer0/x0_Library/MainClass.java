package at.xer0.x0_Library;

import java.io.File;

import at.xer0.x0_Library.Config.ConfigFile;
import at.xer0.x0_Library.Log.Logger;

public class MainClass {
	
	private static Logger l = new Logger("MainClass");

	public static void main(String[] args)
	{
		l.info("Initializing Programm...");
		l.info("Creating Config...");
		
		ConfigFile conf = new ConfigFile(new File(".","test.conf"));
		
		//ifNotExists:
		if(!conf.exists())
		{
			l.info("Writing Config...");
			conf.setValue("love", "true");
			conf.setValue("life", "false");
		}
		
		l.info("Enumerating config:");
		conf.list();

		
	}

}
