package org.xeroserver.x0library.config;

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

/**
 * <h3>A class to load, save and edit simple configuration files with a 'key =
 * value' format.</h3>
 * 
 * @author Daniel 'Xer0' Englisch <xeroserver.org>
 *
 */
public final class ConfigFile {

	private File file = null;
	private String name = "NULL";
	private Map<String, String> properties = new HashMap<String, String>();

	/**
	 * 
	 * @param file
	 *            The file pointing to the desired configuration file.
	 */
	public ConfigFile(File file) {

		name = file.getName();
		this.file = file;

		parseContents();

	}

	/**
	 * Checks weather this ConfigFile was already saved to disk.
	 * 
	 * @return Returns a boolean if the ConfigFile was saved to disk.
	 */
	public boolean exists() {
		return file.exists();
	}

	private void parseContents() {

		if (!exists())
			return;

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

	/**
	 * Returns a Map of all parsed / existing properties of this ConfigFile.
	 * 
	 * @return Returns a Map of all parsed / existing properties of this
	 *         ConfigFile.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Saves the ConfigFile to disk.
	 * 
	 * @return Returns a boolean indicating the success of the saving process.
	 */
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

	/**
	 * Clears loaded / added properties from the memory and freshly parses the
	 * configuration file on disk. If this ConfigFile wasn't saved before, all
	 * properties will be lost!
	 */
	public void reload() {
		if (!exists())
			return;
		properties.clear();
		parseContents();
	}

	/**
	 * Returns the name of the configuration file on disk.
	 * 
	 * @return Returns the name of the configuration file on disk.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Removes a property from this CofigFile
	 * 
	 * @param property
	 *            Removes a property from this ConfigFile
	 */
	public void removeProperty(String property) {
		properties.remove(property);
	}

	/**
	 * Returns the value of the given property if it exists.
	 * 
	 * @param property
	 *            The key of the desired property.
	 * @return Returns the value of the given property if it exists.
	 */
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

	/**
	 * Adds / Overrides a new property.
	 * 
	 * @param property
	 *            Name of the property.
	 * @param value
	 *            Value of the property.
	 */
	public void setProperty(String property, String value) {

		if (hasProperty(property)) {
			properties.replace(property, value);

		} else {
			properties.put(property, value);
		}

	}

	/**
	 * Returns a boolean weather the given property exists or not.
	 * 
	 * @param property
	 *            The name of the desired property.
	 * @return Returns a boolean weather the given property exists or not.
	 */
	public boolean hasProperty(String property) {
		return properties.containsKey(property);
	}

}

class UnknownPropertyException extends Exception {

	private static final long serialVersionUID = 1L;

}
