package org.xeroserver.x0library.config;

/**
 * This class provides the ability to create and manage config files.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-11
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.xeroserver.x0library.log.Logger;
import org.xeroserver.x0library.objtools.StringTools;

public final class ConfigFile {

	// LoggerBlock:
	private Logger l = new Logger("ConfigFile", Logger.ERRORS_ONLY);

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

		parseContents();
		
	}

	public boolean exists() {
		return file.exists();
	}

	private void parseContents() {

		if (!exists()) {
			l.warning("Config File doesn't exist (yet): " + name);
			return;
		}

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));

			while (in.ready()) {

				String s = in.readLine();

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

					properties.put(property, value);

				} catch (Exception ex) {
					l.warning("Failed to parse line: " + s);
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

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void save() {

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

			ArrayList<String> outputList = new ArrayList<String>();

			for (Map.Entry<String, String> entry : properties.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				outputList.add(key + " = " + value);

			}

			Collections.reverse(outputList);

			for (String s : outputList) {
				out.write(s + "\n");
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

		l.info("Saved " + name);
	}

	public void reload() {
		l.log("Reloading " + name + ":");
		properties.clear();
		parseContents();
	}

	public String getName() {
		return name;
	}

	public void removeProperty(String property) {
		properties.remove(property);
		l.info("removed [" + property + "];");
	}

	public String getProperty(String property) {

		if (!hasProperty(property)) {
			return null;
		}
		return properties.get(property);

	}

	public void setProperty(String property, String value) {

		if (hasProperty(property)) {
			properties.replace(property, value);
			l.info("replaced [" + property + " - " + value + "];");

		} else {
			properties.put(property, value);
			l.info("created [" + property + " - " + value + "];");

		}

	}

	public boolean hasProperty(String property) {
		return properties.containsKey(property);
	}

	public void listProperties() {
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
