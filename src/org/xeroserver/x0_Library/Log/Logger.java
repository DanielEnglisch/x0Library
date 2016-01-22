package org.xeroserver.x0_Library.Log;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.xeroserver.x0_Library.IO.FileOutput;

/**
 * This class provides the ability to manage logging in your program. You not
 * only have control over the different types of log-outputs but also if and
 * what will be logged.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-12
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

public class Logger {

	private String name = "";

	// Mode
	public static int NORMAL = 0, ERRORS_ONLY = 1, SILENT = 2;
	private int mode = NORMAL;
	// --------

	// Gui
	private boolean isGUIInitialized = false;
	private JFrame frame = null;
	private JTextArea scroll = null;
	// ----------------------

	// Log/Gui
	private ArrayList<String> log = new ArrayList<String>();
	// ----------

	// Constructors

	public Logger() {
		this("", NORMAL);

	}

	public Logger(String parent) {
		this(parent, NORMAL);

	}

	public Logger(int mode) {
		this("", mode);

	}

	public Logger(String parent, int mode) {
		this.name = parent;
		this.mode = mode;
	}

	// -----------------

	// Private methods
	private void initGUI() {
		frame = new JFrame("Logger - " + name);
		frame.setLocationRelativeTo(null);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		scroll = new JTextArea();
		scroll.setEditable(false);
		JScrollPane pane = new JScrollPane(scroll);
		frame.add(pane);

		isGUIInitialized = true;
	}

	private void flush(String msg) {
		System.out.println(msg);
		log.add(msg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + msg + "\n");
		}

	}

	private String getHead() {
		if (name.equals("")) {
			return "";
		}

		return "[" + name + "]";
	}

	// ------------------

	// GUI related methods
	public void showGUI() {
		if (!isGUIInitialized) {
			initGUI();
		}

		frame.setVisible(true);
	}

	public void hideGUI() {
		if (isGUIInitialized) {
			frame.setVisible(false);
		}

	}
	// -------------------

	// Setter/Getter
	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}
	// ---------

	// IO
	public void write(File f) {

		if (FileOutput.writeStringList(log, f))
			info("Successfully wrote file " + f.getAbsolutePath());
		else
			error("Failed to write log!");
	}
	// -------

	// Logging:
	public void log(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + " " + s;

		flush(lg);
	}

	public void info(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[INFO] " + s;

		flush(lg);

	}

	public void warning(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[WARNING] " + s;

		flush(lg);

	}

	public void error(String s) {

		if (mode == SILENT)
			return;

		String lg = getHead() + "[ERROR] " + s;

		flush(lg);

	}

	public void fatal(String s) {

		String lg = getHead() + "[FATAL] " + s;

		flush(lg);

	}

	public void custom(String type, String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[" + type + "] " + s;

		flush(lg);

	}

}
