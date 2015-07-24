package at.xer0.x0_Library.Log;

/**
 * This class provides the ability to manage logging in your program. You not
 * only have control over the different types of log-outputs but also if and
 * what will be logged.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-12
 **/

public class Logger {

	public static int NORMAL = -1;
	public static int ERRORS_ONLY = 0;
	public static int SILENT = 1;

	private int mode = -1;

	private String parentApp = "Logger";

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Name of the process using this logger
	 **/
	public Logger(String parent) {
		parentApp = parent;
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Name of the logger
	 * @param mode
	 *            Logger Mode
	 **/
	public Logger(String parent, int mode) {
		parentApp = parent;
		this.mode = mode;
	}

	/**
	 * Changes the current Logger Mode
	 * 
	 * @param mode
	 *            Logger Mode
	 **/
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Logs a given string as a normal log
	 * 
	 * @param s
	 *            String to log
	 **/
	public void log(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}
		System.out.println("[" + parentApp + "]" + " [LOG] " + s);
	}

	/**
	 * Logs a given string as an info log
	 * 
	 * @param s
	 *            String to log
	 **/
	public void info(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}
		System.out.println("[" + parentApp + "]" + " [INFO] " + s);
	}

	/**
	 * Logs a given string as a warning
	 * 
	 * @param s
	 *            String to log
	 **/
	public void warning(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}
		System.out.println("[" + parentApp + "]" + " [WARNING] " + s);
	}

	/**
	 * Logs a given string as an error
	 * 
	 * @param s
	 *            String to log
	 **/
	public void error(String s) {
		if (mode == SILENT) {
			return;
		}
		System.out.println("[" + parentApp + "]" + " [ERROR] " + s);
	}

	/**
	 * Logs a given string as a fatal error
	 * 
	 * @param s
	 *            String to log
	 **/
	public void fatal(String s) {
		if (mode == SILENT) {
			return;
		}
		System.out.println("[" + parentApp + "]" + " [FATAL] " + s);
	}

}
