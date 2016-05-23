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

public final class ConfigFile {

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
					ex.printStackTrace();
				}

			}

			in.close();

		} catch (Exception e) {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

		}

	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public boolean save() {

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
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
			ex.printStackTrace();

			return false;

		}

		return true;
	}

	public void reload() {
		properties.clear();
		parseContents();
	}

	public String getName() {
		return name;
	}

	public void removeProperty(String property) {
		properties.remove(property);
	}

	public String getProperty(String property) {

		try {

			if (!hasProperty(property)) {
				throw new UnknownPropertyException();
			} else
				return properties.get(property);

		} catch (UnknownPropertyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setProperty(String property, String value) {

		if (hasProperty(property)) {
			properties.replace(property, value);

		} else {
			properties.put(property, value);
		}

	}

	public boolean hasProperty(String property) {
		return properties.containsKey(property);
	}

}

class UnknownPropertyException extends Exception {

	private static final long serialVersionUID = 1L;

}
