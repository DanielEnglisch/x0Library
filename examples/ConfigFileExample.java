package org.xeroserver.x0library.examples;

import java.io.File;

import org.xeroserver.x0library.config.ConfigFile;
import org.xeroserver.x0library.log.Logger;

public class ConfigFileExample {

	public static void main(String[] args) {
		
		Logger l = new Logger("ConfigFileExample");

		ConfigFile f = new ConfigFile(new File(".\\tmp\\myConfig.conf"));		
		l.log("Working with " + f.getName());

		if (f.exists())
			l.log("This config instance is loaded from a file.");
		else{
			l.log("This config instance was freshly created and has to be saved!");
			f.addComment("myConfig file example file");
			f.addNewLine();
			f.addComment("Please don't delete my configuration!");
			f.setProperty("name", "Daniel Englisch");
			f.setProperty("age", "19");
			f.setProperty("hobby", "java");
			f.addComment("End of config!");
			f.save();
		}
		
		
		
		f.renameKey("age", "myAge");
		int age = f.getValue("myAge").toInt();
		
		if(age > 18){
			l.log(f.getValue("name") + " is over 10 years old! He is " + age );
		}
		
		
		if(f.hasKey("hobby")){
			l.log("His hobby is " + f.getValue("hobby").toString());
		}
		
		

	}

}
