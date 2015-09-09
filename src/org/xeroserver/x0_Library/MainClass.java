package org.xeroserver.x0_Library;

/**
 * This class shows some examples to user some classes of this library.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-12
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

import java.io.File;

import javax.swing.JFrame;

import org.xeroserver.x0_Library.Config.ConfigFile;
import org.xeroserver.x0_Library.GUI.X0InputField;
import org.xeroserver.x0_Library.Log.Logger;
import org.xeroserver.x0_Library.ObjectTools.StringTools;
import org.xeroserver.x0_Library.Parser.ArgumentParser;

public class MainClass {

	private static Logger l = new Logger("MainLogger");

	public static void main(String[] args) {
		

		  l.showGUI();
		  
		  l.info("-----Logger-----"); testLogger();
		  
		  l.info("-----X0InputField-----"); testInpuField();
		  
		  l.info("-----ConfigFile-----"); testConfigFile();
		 
		  l.info("-----StringTools-----"); testStringTools();
		  
		  l.info("-----ArgumentParser-----"); testArgumentParser();
		 
		 // l.write(new File(".", "log.log"));
		  
		 
	}
	
	private static void testLogger()
	{
		Logger l1 = new Logger();
		Logger l2 = new Logger("Yolo");
		Logger l3 = new Logger("XER0",Logger.ERRORS_ONLY);
		
		l1.fatal("Something went horribly wrong...");
		l2.info("Ther is an update available!");
		
		l3.info("Yea this is not important...");
		l3.error("This on the other hand is!");
	

	}
	
	private static void testInpuField()
	{
		JFrame f = new JFrame("GUI TEST");
		f.setLayout(null);

		@SuppressWarnings("serial")
		X0InputField inp = new X0InputField(
				new int[] { X0InputField.DOUBLE, X0InputField.NOT_ZERO, X0InputField.POSITIVE,

		}) {
			@Override
			public void update() {
				l.info("##########");
				l.info("String: " + this.getStringValue());
				l.info("Integer: " + this.getIntegerValue());
				l.info("Double: " + this.getDoubleValue());

			}

			

		}

		;
		inp.setDisplayErrors(true);

		inp.setBounds(50, 50, 200, 40);
		f.add(inp);

		f.setSize(500, 300);
		f.setLocationRelativeTo(null);

		f.setResizable(false);
		f.setVisible(true);

	}

	public static void testConfigFile() {
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

		conf.setValue("cs", "go");
	}

	public static void testStringTools() {
		String s = "Xer0";

		l.log(s);
		l.info(StringTools.getFirstChar(s));
		l.info(StringTools.getLastChar(s));
		l.info(StringTools.removeFirstChar(s));
		l.info(StringTools.removeLastChar(s));

	}

	public static void testArgumentParser() {
		String args = "-x -y -z -verbose -name Daniel";

		ArgumentParser p = new ArgumentParser(args);

		l.info(p.getValue("-name"));

		if (p.exists("-y")) {
			l.info("YES");
		}
	}

}
