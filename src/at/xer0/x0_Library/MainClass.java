package at.xer0.x0_Library;

import java.io.File;

import at.xer0.x0_Library.Config.ConfigFile;
import at.xer0.x0_Library.Log.Logger;

public class MainClass {

	public static void main(String[] args)
	{
		ConfigFile f = new ConfigFile(new File("." , "defaults.cfg"));
		f.setValue("ownerID", "xx00");

		Logger.log(f.getValue("ownerID"));

		
	}

}
