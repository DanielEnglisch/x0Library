package at.xer0.x0_Library.Log;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import at.xer0.x0_Library.IO.FileOutput;

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

	private boolean enableTimestamp = false;

	private boolean isGUIInitialized = false;

	private ArrayList<String> log = new ArrayList<String>();

	private int mode = -1;

	private JFrame frame = null;
	private JTextArea scroll = null;

	private String parentApp = "Logger";

	public Logger(String parent) {
		parentApp = parent;

	}

	public Logger(String parent, int mode) {
		parentApp = parent;
		this.mode = mode;
	}

	public Logger(String parent, boolean timestamp) {
		parentApp = parent;
		this.enableTimestamp = timestamp;
	}

	public Logger(String parent, int mode, boolean timestamp) {
		parentApp = parent;
		this.mode = mode;
		this.enableTimestamp = timestamp;

	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	private String getTimestamp() {
		java.util.Date date = new java.util.Date();
		return "[" + (new Timestamp(date.getTime()).toString()) + "] ";
	}

	public void log(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}

		String lg = "[" + parentApp + "]" + " [LOG] " + s;

		if (this.enableTimestamp) {
			lg = getTimestamp() + lg;
		}

		System.out.println(lg);
		log.add(lg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + lg + "\n");
		}
	}

	public void info(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}

		String lg = "[" + parentApp + "]" + " [INFO] " + s;

		if (this.enableTimestamp) {
			lg = getTimestamp() + lg;
		}

		System.out.println(lg);
		log.add(lg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + lg + "\n");
		}

	}

	public void warning(String s) {
		if (mode == ERRORS_ONLY) {
			return;
		}
		if (mode == SILENT) {
			return;
		}

		String lg = "[" + parentApp + "]" + " [WARNING] " + s;

		if (this.enableTimestamp) {
			lg = getTimestamp() + lg;
		}

		System.out.println(lg);
		log.add(lg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + lg + "\n");
		}

	}

	public void error(String s) {
		if (mode == SILENT) {
			return;
		}

		String lg = "[" + parentApp + "]" + " [ERROR] " + s;

		if (this.enableTimestamp) {
			lg = getTimestamp() + lg;
		}

		System.out.println(lg);
		log.add(lg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + lg + "\n");
		}

	}

	public void fatal(String s) {
		if (mode == SILENT) {
			return;
		}

		String lg = "[" + parentApp + "]" + " [FATAL] " + s;

		if (this.enableTimestamp) {
			lg = getTimestamp() + lg;
		}

		System.out.println(lg);
		log.add(lg);

		if (this.isGUIInitialized) {
			scroll.setText(scroll.getText() + lg + "\n");
		}

	}

	public void write(File f) {

		FileOutput.writeArrayList(log, f);
		info("Successfully wrote file " + f.getAbsolutePath());
	}

	private void initGUI() {
		frame = new JFrame("Logger");
		frame.setLocationRelativeTo(null);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		scroll = new JTextArea();
		JScrollPane pane = new JScrollPane(scroll);
		frame.add(pane);

		isGUIInitialized = true;
	}

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

}
