package org.xeroserver.x0library.examples;

import java.io.File;

import org.xeroserver.x0library.config.ConfigFile;

public class ConfigFileExample {

	public static void main(String[] args) {

		ConfigFile f = new ConfigFile(new File(".\\tmp\\myConfig.conf"));

		System.out.println("Working with " + f.getName());

		if (f.exists())
			System.out.println("This config instance is loaded from a file.");
		else
			System.out.println("This config instance was freshly created and has to be saved!");

		f.setProperty("myProperty", "x0");
		f.addComment("MyComment");
		f.addNewLine();
		f.addComment("Da Keys:");
		f.setProperty("key", "dD2 dno i2d2 D");

		f.save();

		if (f.hasKey("key"))
			System.out.println("Key is " + f.getValue("key"));

		f.removeProperty("myProperty");
		f.save();

	}

}
