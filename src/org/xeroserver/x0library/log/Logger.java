package org.xeroserver.x0library.log;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.xeroserver.x0library.io.FileOutput;

public class Logger {

	private String name = "";

	public static int NORMAL = 0, ERRORS_ONLY = 1, SILENT = 2;
	private int mode = NORMAL;

	private boolean isGUIInitialized = false;
	private JFrame frame = null;
	private JTextArea scroll = null;

	private ArrayList<String> log = new ArrayList<String>();

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

	private void initGUI() {
		frame = new JFrame("Logger - " + name);
		frame.setLocationRelativeTo(null);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

		return "[" + name + "] ";
	}

	public final void showGUI() {
		if (!isGUIInitialized) {
			initGUI();
		}

		frame.setVisible(true);
	}

	public final void hideGUI() {
		if (isGUIInitialized) {
			frame.setVisible(false);
		}

	}

	public final void setMode(int mode) {
		this.mode = mode;
	}

	public final int getMode() {
		return mode;
	}

	public final void write(File f) {

		if (FileOutput.writeStringList(log, f))
			info("Successfully wrote file " + f.getAbsolutePath());
		else
			error("Failed to write log!");
	}

	public final void log(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + " " + s;

		flush(lg);
	}

	public final void info(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[INFO] " + s;

		flush(lg);

	}

	public final void warning(String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[WARNING] " + s;

		flush(lg);

	}

	public final void error(String s) {

		if (mode == SILENT)
			return;

		String lg = getHead() + "[ERROR] " + s;

		flush(lg);

	}

	public final void fatal(String s) {

		String lg = getHead() + "[FATAL] " + s;

		flush(lg);

	}

	public final void custom(String type, String s) {

		if (mode == ERRORS_ONLY || mode == SILENT)
			return;

		String lg = getHead() + "[" + type + "] " + s;

		flush(lg);

	}

}
