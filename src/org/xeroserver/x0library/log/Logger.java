package org.xeroserver.x0library.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Logger {

	private Logger parentLogger = null;
	private String name = null;
	private boolean muted = false;
	private ArrayList<String> log = new ArrayList<String>();
	private Long startTime = System.nanoTime();
	private boolean timestampEnabled = false;

	public Logger() {
		this(null, null);
	}

	public Logger(String name) {
		this(null, name);
	}

	public Logger(Logger parentLogger) {
		this(parentLogger, null);
	}

	public Logger(Logger parentLogger, String name) {
		this.parentLogger = parentLogger;
		this.name = name;
	}

	public final void log(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.log(msg);
		else
			flush(null, msg, false);
	}

	public final void info(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.info(msg);
		else
			flush("INFO", msg, false);
	}

	public final void warning(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.warning(msg);
		else
			flush("WARN", msg, false);
	}

	public final void error(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.error(msg);
		else
			flush("ERROR", msg, true);
	}

	public final void fatal(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.fatal(msg);
		else
			flush("FATAL", msg, true);
	}

	public final String[] getLog() {
		return log.toArray(new String[0]);
	}

	public final void clear() {
		log.clear();
	}

	public final boolean dump(File f) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(f));

			for (String s : log) {
				out.write(s + "\n");
			}

			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public final void custom(String prefix, String msg, boolean isError) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.custom(prefix, msg, isError);
		else
			flush(prefix, msg, isError);
	}

	private void flush(String prefix, String msg, boolean error) {

		if (muted)
			return;

		if (prefix != null) {
			msg = "{" + prefix + "}\t" + msg;
		}

		if (timestampEnabled) {
			double time = (double) ((double) (System.nanoTime() - startTime) / 1000000 / 1000);
			msg = "(" + String.format("%.5f", time) + "s) " + msg;

		}

		output(msg, error);
		log.add(msg);
	}

	// Overridable for e.g adding to a JFrame
	public void output(String msg, boolean error) {
		System.out.println(msg);
	}

	public final Logger getParentLogger() {
		return parentLogger;
	}

	public final void setParentLogger(Logger parentLogger) {
		this.parentLogger = parentLogger;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final boolean isMuted() {
		return muted;
	}

	public final void setMuted(boolean muted) {
		this.muted = muted;
	}

	public final boolean isTimestampEnabled() {
		return timestampEnabled;
	}

	public final void setTimestampEnabled(boolean timestampEnabled) {
		this.timestampEnabled = timestampEnabled;
	}

}
