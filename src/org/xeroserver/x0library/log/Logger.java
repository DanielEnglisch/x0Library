package org.xeroserver.x0library.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * The Logger class is used to efficiently create log files featuring different
 * levels (Info, Warning, Error, Fatal) and time logs. It is also possible to
 * dump logs to file.
 * 
 * @author Xer0
 * @version 2.0
 * @since 2012
 */
public class Logger {

	/**
	 * Class that contains the data of the logger message (time, message, type)
	 * 
	 * @author Xer0
	 * @version 1.0
	 * @since 18.5.2017
	 */
	class LoggerData {
		private String message, prefix, time;

		private LoggerData(String time, String prefix, String message) {
			this.time = time;
			this.prefix = prefix;
			this.message = message;
		}

		public String getTime() {
			return time;
		}

		public String getMessage() {
			return message;
		}

		public String getPrefix() {
			return prefix;
		}
	}

	private Logger parentLogger = null;
	private String name = null;
	private ArrayList<String> log = new ArrayList<String>();
	private Long startTime = System.nanoTime();
	private boolean timestampEnabled = false;

	public Logger() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The display name of the logger
	 */
	public Logger(String name) {
		this.name = name;
	}

	/**
	 * Logs a message without a prefix like ERROR or INFO.
	 * 
	 * @param msg
	 *            The message
	 */
	public final void log(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.log(msg);
		else
			flush(null, msg);
	}

	/**
	 * Logs a message with the INFO prefix.
	 * 
	 * @param msg
	 *            The message
	 */
	public final void info(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.info(msg);
		else
			flush("INFO", msg);
	}

	/**
	 * Logs a message with the WARN prefix.
	 * 
	 * @param msg
	 *            The message
	 */
	public final void warning(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.warning(msg);
		else
			flush("WARN", msg);
	}

	/**
	 * Logs a message with the ERROR prefix.
	 * 
	 * @param msg
	 *            The message
	 */
	public final void error(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.error(msg);
		else
			flush("ERROR", msg);
	}

	/**
	 * Logs a message with the FATAL prefix.
	 * 
	 * @param msg
	 *            The message
	 */
	public final void fatal(String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.fatal(msg);
		else
			flush("FATAL", msg);
	}

	/**
	 * Logs a message with a custom prefix.
	 * 
	 * @param prefix
	 *            The prefix e.g "FUNNY"
	 * @param msg
	 *            The message
	 */
	public final void custom(String prefix, String msg) {
		if (name != null)
			msg = "[" + name + "] " + msg;
		if (parentLogger != null)
			parentLogger.custom(prefix, msg);
		else
			flush(prefix, msg);
	}

	/**
	 * Gets the saved log.
	 */
	public final String[] getLog() {
		return log.toArray(new String[0]);
	}

	/**
	 * Clears the saved log array.
	 */
	public final void clear() {
		log.clear();
	}

	/**
	 * Dumps the log to file.
	 * 
	 * @param f
	 *            The file to which the log is saved to
	 * @return weather or not the save was successful
	 */
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

	private final void flush(String prefix, String msg) {

		String formattedTime = null;
		if (timestampEnabled) {
			double time = (double) ((double) (System.nanoTime() - startTime) / 1000000 / 1000);
			formattedTime = String.format("%.5f", time);
		}

		dataOutput(new LoggerData(formattedTime, prefix, msg));
		log.add(msg);
	}

	/**
	 * Overridable method to create a custom log format.
	 * 
	 * @param data
	 *            The container in which the data of the message (prefix,
	 *            message, time) is stored.
	 */
	public void dataOutput(LoggerData data) {
		String out = "";
		if (data.getTime() != null)
			out += "(" + data.getTime() + " s) ";
		if (data.getPrefix() != null)
			out += "{" + data.getPrefix() + "}\t";
		out += data.getMessage();
		System.out.println(out);
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

	public final boolean isTimestampEnabled() {
		return timestampEnabled;
	}

	public final void setTimestampEnabled(boolean timestampEnabled) {
		this.timestampEnabled = timestampEnabled;
	}

	public final void resetTime() {
		startTime = System.nanoTime();
	}

}
