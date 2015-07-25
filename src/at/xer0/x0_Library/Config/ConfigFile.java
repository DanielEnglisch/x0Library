package at.xer0.x0_Library.Config;

/**
 * This class provides the ability to create and manage config files.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-11
 **/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.xer0.x0_Library.Log.Logger;
import at.xer0.x0_Library.String.StringTools;

public class ConfigFile {

	// LoggerBlock:
	private Logger l = new Logger("ConfigFile", Logger.SILENT);

	public Logger getLogger() {
		return l;
	}
	// -----------

	private File file = null;
	private String name = "NULL";
	private HashMap<String, String> properties = new HashMap<String, String>();

	public ConfigFile(File f) {
		name = f.getName();
		file = f;

		l.info("Initializing " + name + ":");

		parseContents();
	}

	public boolean exists() {
		return file.exists();
	}

	private void parseContents() {
		if (!exists()) {
			l.error("FileNotFound: " + name);
			return;
		}

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));

			while (in.ready()) {
				String s = in.readLine();

				if (StringTools.getFirstChar(s).equalsIgnoreCase("#")) {
					l.log("Skipping comment: " + s);
					properties.put("#", StringTools.removeFirstChar(s));
				} else {
					try {
						String[] sp = s.split("=");
						String property = sp[0].replaceAll("\\s", "");
						String valueRaw = sp[1];
						String value = "NULL";
						if (valueRaw.substring(0, 1).equals(" ")) {
							value = valueRaw.substring(1);
						} else {
							value = valueRaw;
						}

						loadProperty(property, value);
						l.log("Parsed: [" + property + " - " + value + "];");
					} catch (Exception ex) {
						l.warning("Failed to parse line: " + s);
					}
				}

			}

			in.close();
		} catch (Exception e) {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			l.error("Error reading file!");
			e.printStackTrace();

		}

	}

	public void saveFile() {

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedWriter out = null;

		try {
			out = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<String, String> entry : properties.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				if (StringTools.getFirstChar(key).equals("#")) {
					out.write("#" + value + "\n");
				} else {
					out.write(key + " = " + value + "\n");
				}

				out.flush();
			}

			out.close();
		} catch (Exception ex) {
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			l.error("Error writing file!");
			ex.printStackTrace();
		}

		l.log("Saved " + name);
	}

	public void reloadFile() {
		l.log("Reloading " + name + ":");
		properties.clear();
		parseContents();
	}

	public String getName() {
		return name;
	}

	public void removeProperty(String property) {
		properties.remove(property);
		saveFile();
	}

	private void addProperty(String property, String value) {

		properties.put(property, value);
		l.log("Added: [" + property + " - " + value + "];");

		saveFile();
	}

	private void loadProperty(String property, String value) {

		properties.put(property, value);
	}

	public String getValue(String property) {
		if (!isProperty(property)) {
			return "NULL";
		}
		return properties.get(property);
	}

	public void setValue(String property, String value) {
		if (isProperty(property)) {
			properties.replace(property, value);
			l.log("Set: [" + property + " - " + value + "];");

			saveFile();
		} else {
			addProperty(property, value);
		}

	}

	public boolean isProperty(String property) {
		return properties.containsKey(property);
	}

	public void list() {
		Logger g = new Logger("ConfigFile");
		g.log("#####Contents of " + name + ":#####");
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (!StringTools.getFirstChar(key).equals("#")) {
				g.log(key + " = " + value);
			}

		}
		g.log("#####End of Contents#####");
		g = null;

	}
}
