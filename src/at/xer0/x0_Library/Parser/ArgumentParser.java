package at.xer0.x0_Library.Parser;

/**
 * This class provides the ability to parse command-line arguments.
 * You can check for certain arguments and their values.
 * @author  Daniel 'Xer0' Englisch
 * @since   2015-07-24 
 **/

import java.util.HashMap;
import java.util.Map;

import at.xer0.x0_Library.Log.Logger;
import at.xer0.x0_Library.String.StringTools;

public class ArgumentParser {

	// LoggerBlock:
	private Logger l = new Logger("ArgParser", Logger.SILENT);

	public Logger getLogger() {
		return l;
	}
	// -----------

	private HashMap<String, String> arguments = new HashMap<String, String>();

	/**
	 * Constructor
	 * 
	 * @param args
	 *            ArrayString containing the arguments (e.g: "-t 34 -f -t")
	 **/
	public ArgumentParser(String[] args) {
		parse(args);
	}

	/**
	 * Constructor
	 * 
	 * @param args
	 *            String containing the arguments (e.g: "-t 34 -f -t")
	 **/
	public ArgumentParser(String args) {
		parse(args);
	}

	private void parse(String args) {
		parse(args.split("\\s"));
	}

	private void parse(String[] args) {
		l.info("Parsing arguments...");

		for (int i = 0; i < args.length; i++) {

			try {
				if (i < args.length - 1) {
					if (StringTools.getFirstChar(args[i]).equals("-")
							&& !StringTools.getFirstChar(args[i + 1]).equals("-")) {
						arguments.put(args[i], args[i + 1]);
						l.info("Added " + args[i] + " " + args[i + 1]);

					} else if (StringTools.getFirstChar(args[i]).equals("-")
							&& StringTools.getFirstChar(args[i + 1]).equals("-")) {
						arguments.put(args[i], "");
						arguments.put(args[i + 1], "");

						l.info("Added " + args[i]);
						l.info("Added " + args[i + 1]);
						i++;

					}
				} else if (StringTools.getFirstChar(args[i]).equals("-")) {
					arguments.put(args[i], "");
					l.info("Added " + args[i]);
				}

			} catch (Exception e) {
				l.fatal("Error parsing the arguments!");
				e.printStackTrace();
			}

		}
	}

	public boolean exists(String argument) {
		if (arguments.containsKey(argument)) {
			l.info("Arg " + argument + " exists!");
			return true;
		}

		l.error("Arg " + argument + " doesn't exist!");
		return false;
	}

	public String getValue(String argument) {
		if (exists(argument)) {
			String value = "";
			value = arguments.get(argument);

			l.info("Returned " + value + " for arg " + argument);
			return value;

		}

		l.error("No Value: Arg doesn't exist!");
		return null;
	}

	public void list() {
		Logger g = new Logger("ArgParser");
		g.log("#####Listing arguments:#####");

		for (Map.Entry<String, String> entry : arguments.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			g.log(key + " " + value);
		}
		g.log("#####End of Contents#####");
		g = null;

	}

}
