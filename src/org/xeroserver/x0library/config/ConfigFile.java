package org.xeroserver.x0library.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xeroserver.x0library.log.Logger;

public class ConfigFile {

	private File file = null;
	private ArrayList<Line> lines = new ArrayList<Line>();

	//Loggerblock
	private Logger logger = new Logger();
	public Logger getLogger() {
		return this.logger;
	}
	//-----------

	public ConfigFile(File file) {
		this.file = file;

		if (file.exists())
			parse();
	}

	public boolean exists() {
		return file.exists();
	}

	public void save() {

		if (!file.canWrite()) {
			logger.error("No permission to write ConfigFile '" + file.getName() + "' !");
		}

		BufferedWriter out = null;

		try {

			out = new BufferedWriter(new FileWriter(file));

			for (Line l : lines) {
				if (l.isComment())
					out.write(l.getComment());
				else
					out.write(l.getKey() + " = " + l.getValue());

				out.write("\n");
				out.flush();
			}

			out.close();

		} catch (IOException e) {
			logger.fatal("There was an error saving the ConfigFile '" + getName() + "' !");
			e.printStackTrace();
		}

	}

	private void parse() {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));

			while (in.ready()) {

				String ln = in.readLine();

				if (ln.startsWith("#") || ln.equals("")) {
					lines.add(new Line(ln));
				} else {

					Line l = null;
					String k, v = null;

					try {

						String[] spl = ln.split("\\s=\\s");
						k = spl[0].replaceAll("\\s", "");
						v = spl[1];

						l = new Line(k, v);

					} catch (Exception e) {
						logger.warning("Invalid line in ConfigFile " + file.getName() + ": '" + ln + "'");
						l = null;
					}

					if (l != null)
						lines.add(l);

				}

			}

		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
				logger.fatal("There was an error parsing the ConfigFile '" + getName() + "' !");
				e1.printStackTrace();
			}
			logger.fatal("There was an error parsing the ConfigFile '" + getName() + "' !");
			e.printStackTrace();
		}

	}

	public String getName() {
		return file.getName();
	}

	public void clear() {
		lines.clear();
	}

	public boolean hasKey(String key) {

		for (Line l : lines) {
			if (!l.isComment()) {
				if (key.equals(l.getKey()))
					return true;
			}
		}

		return false;

	}

	public void removeProperty(String key) {

		if (!hasKey(key))
			return;

		for (int i = 0; i < lines.size(); i++) {
			Line l = lines.get(i);

			if (!l.isComment()) {
				if (l.getKey().equals(key)) {
					lines.remove(i);
					return;
				}
			}

		}

	}

	public String[] getKeys() {
		ArrayList<String> keysList = new ArrayList<String>();

		for (Line l : lines) {
			if (!l.isComment()) {
				keysList.add(l.getKey());
			}
		}

		return keysList.toArray(new String[0]);

	}

	public Map<String, String> getProperties() {

		HashMap<String, String> map = new HashMap<String, String>();

		for (Line l : lines) {
			if (!l.isComment()) {
				map.put(l.getKey(), l.getValue());
			}
		}

		return map;
	}

	public void renameKey(String key, String newKey) {
		if (!hasKey(key)) {
			logger.error("Renaming failed! Unknown key '" + key + "' in ConfigFile '" + file.getName() + "' !");
			return;
		}

		if (key.equals(newKey))
			return;

		if (hasKey(newKey)) {
			logger.error("Renaming failed! Key '" + key + "' already exists in ConfigFile '" + file.getName() + "' !");
			return;
		}

		for (Line l : lines) {
			if (!l.isComment()) {
				if (key.equals(l.getKey()))
					l.setKey(newKey);
			}
		}

	}

	public void removeComments() {
		for (int i = 0; i < lines.size(); i++) {
			Line l = lines.get(i);

			if (l.isComment()) {
				lines.remove(i);
				i--;
			}

		}
	}

	public PropertyValue getValue(String key) {

		if (!hasKey(key))
			return null;

		for (Line l : lines) {
			if (!l.isComment()) {
				if (key.equals(l.getKey()))
					return new PropertyValue(l.getValue());
			}
		}

		return null;

	}

	public void setProperty(String key, String value) {

		value = value.replaceAll("\n", "");

		if (value.matches(".*\\s+.*"))
			value = "\"" + value + "\"";

		if (hasKey(key)) {

			for (Line l : lines) {
				if (!l.isComment()) {
					if (l.getKey().equals(key)) {
						l.setValue(value);
						return;
					}
				}
			}

		} else {
			lines.add(new Line(key, value));
		}

	}

	public void addComment(String comment) {
		comment = "#" + comment.replaceAll("\n", "");
		lines.add(new Line(comment));
	}

	public void addNewLine() {
		lines.add(new Line(""));
	}

	public class PropertyValue {

		private String stringValue;

		private PropertyValue(String stringValue) {
			this.stringValue = stringValue;
		}

		public String toString() {
			return stringValue;
		}

		public Double toDouble() {

			double d = -0.0d;

			try {
				d = Double.parseDouble(stringValue);
			} catch (Exception e) {
				logger.error("Couldn't cast '" + stringValue + "' to double!");
			}

			return d;
		}

		public Integer toInt() {

			int i = -0;

			try {
				i = Integer.parseInt(stringValue);
			} catch (Exception e) {
				logger.error("Couldn't cast '" + stringValue + "' to int!");
			}

			return i;
		}

	}

}

class Line {

	public final int COMMENT = 0, PROPERTY = 1;
	private String comment, key, value;
	private boolean isComment = false;

	public Line(String comment) {
		this.comment = comment;
		this.isComment = true;
	}

	public Line(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getComment() {
		return comment;
	}

	public boolean isComment() {
		return isComment;
	}

}
