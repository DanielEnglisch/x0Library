package at.xer0.x0_Library;

/**
 * This class shows some examples to user some classes of this library.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-12
 **/

import java.io.File;

import at.xer0.x0_Library.Config.ConfigFile;
import at.xer0.x0_Library.Log.Logger;
import at.xer0.x0_Library.Parser.ArgumentParser;
import at.xer0.x0_Library.String.StringTools;

public class MainClass {

	private static Logger l = new Logger("MainLogger");

	public static void main(String[] args) {

		l.info("-----ConfigFile-----");
		testConfigFile();

		l.info("-----StringTools-----");
		testStringTools();

		l.info("-----ArgumentParser-----");
		testArgumentParser();

		l.write(new File(".", "log.log"));
	}

	private static void testConfigFile() {
		l.info("Initializing Programm...");
		l.info("Creating Config...");

		ConfigFile conf = new ConfigFile(new File(".", "test.conf"));

		// ifNotExists:
		if (!conf.exists()) {
			l.info("Writing Config...");
			conf.setValue("love", "true");
			conf.setValue("life", "false");
		}

		l.info("Enumerating config:");
		conf.list();
	}

	private static void testStringTools() {
		String s = "Xer0";

		l.log(s);
		l.info(StringTools.getFirstChar(s));
		l.info(StringTools.getLastChar(s));
		l.info(StringTools.removeFirstChar(s));
		l.info(StringTools.removeLastChar(s));

	}

	private static void testArgumentParser() {
		String args = "-x -y -z -verbose -name Daniel";

		ArgumentParser p = new ArgumentParser(args);

		l.info(p.getValue("-name"));

		if (p.exists("-y")) {
			l.info("YES");
		}
	}

}
