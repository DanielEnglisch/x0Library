package org.xeroserver.x0library.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * <h3>An extension of the default JTextField enabling the user to specify
 * different options how this InputField should behave. It is possible to only
 * allow double or integer numbers with several conditions such as 'not-zero' of
 * rounding. There is also a console like mode where the the console-history
 * needs to be enabled. To learn more, take a look at the X0Inputfile Example
 * class.</h3>
 */
public class X0InputField extends JTextField {

	private static final long serialVersionUID = 1L;

	/** Only integers will be accepted. */
	final public static int INT = 0;
	/** Only doubles will be accepted. */
	final public static int DOUBLE = 1;
	/** The input mustn't be 0. */
	final public static int NOT_ZERO = 2;
	/** The input must be positive. */
	final public static int POSITIVE = 3;
	/** The input must be negative. */
	final public static int NEGATIVE = 4;
	/** The input will be rounded up. */
	final public static int ROUND_UP = 5;
	/** The input will be rounded down. */
	final public static int ROUND_DOWN = 6;
	/** The input will be rounded. */
	final public static int ROUND = 7;
	/** No spaces are allowed. */
	final public static int NO_SPACES = 8;
	/** Spaced will be cleared. */
	final public static int CLEAR_SPACES = 9;
	/** Input will be cleared on enter. */
	final public static int CLEAR_ON_ENTER = 10;
	/**
	 * The text color won't be changes when editing or when an error occurred.
	 */
	final public static int NO_COLOR = 11;
	/**
	 * With this flag every input will be saved an you can scroll trough it by
	 * hitting the up/down arrow key.
	 */
	final public static int CONSOLE_HISTORY = 12;

	private int[] flags = new int[] {};

	private double doubleValue = 0;
	private int integerValue = 0;
	private String stringValue = "";

	private ArrayList<String> history = new ArrayList<String>();
	private int history_nav = 0;

	private Color errorColor = Color.RED;
	private Color editColor = Color.ORANGE;

	private boolean displayErrors = false;

	/**
	 * 
	 * @param flags
	 *            Options to customize this InputField.
	 * @param display
	 *            Boolean weather there should be a JOptionPane displaying the
	 *            input error depending on the flag options.
	 */
	public X0InputField(int[] flags, boolean display) {
		this.flags = flags;
		this.displayErrors = display;
		registerKeyListener();

		if (contains(NO_COLOR)) {
			errorColor = Color.BLACK;
			editColor = Color.BLACK;
		}

	}

	/**
	 * 
	 * @param flags
	 *            Options to customize this InputField.
	 */
	public X0InputField(int[] flags) {
		this(flags, false);
	}

	/**
	 * 
	 * @param display
	 *            Boolean weather there should be a JOptionPane displaying the
	 *            input error depending on the flag options.
	 */
	public X0InputField(boolean display) {
		this(new int[] {}, display);
	}

	public X0InputField() {
		this(new int[] {}, false);
	}

	private void registerKeyListener() {
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {

				// Console Mechanism
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {

					if (!contains(CONSOLE_HISTORY))
						return;

					if (history.size() != 0 && history.size() - 1 - history_nav >= 0)
						setText(history.get(history.size() - 1 - history_nav));

					if (history_nav < history.size() - 1)
						history_nav++;

				}

				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {

					if (!contains(CONSOLE_HISTORY))
						return;

					if (history.size() != 0 && history.size() - 1 - history_nav >= 0)
						setText(history.get(history.size() - 1 - history_nav));

					if (history_nav > 0)
						history_nav--;

				}

				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (processValue()) {
						setForeground(Color.GREEN);
						setForeground(Color.BLACK);
						update();

					} else {
						setForeground(errorColor);

					}

				} else {
					setForeground(editColor);
				}
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}

		});

	}

	/**
	 * Returns the parsed double value of the InputField.
	 * 
	 * @return Returns the parsed double value of the InputField.
	 */
	public final double getDoubleValue() {
		return doubleValue;
	}

	/**
	 * Returns the parsed integer value of the InputField.
	 * 
	 * @return Returns the parsed double value of the InputField.
	 */
	public final int getIntegerValue() {
		return integerValue;
	}

	/**
	 * Clears the input history if enabled.
	 */
	public final void clearHistory() {
		if (contains(CONSOLE_HISTORY))
			history.clear();
	}

	/**
	 * Returns the string value of the InputField.
	 * 
	 * @return Returns the string value of the InputField.
	 */
	public final String getStringValue() {
		return stringValue;
	}

	/**
	 * Sets the color the text of the InputField should have while typing.
	 * 
	 * @param color
	 *            The Color the text should have while editing.
	 */
	public final void setEditColor(Color color) {
		editColor = color;
	}

	/**
	 * Sets the color the text of the InputField when an input error occurred.
	 * 
	 * @param color
	 *            The Color the text should have when an error occurred.
	 */
	public final void setErrorColor(Color color) {
		errorColor = color;
	}

	/**
	 * Sets weather a JOptionPane containing the error message should appear
	 * when an error occurred.
	 * 
	 * @param display
	 *            A boolean weather the JOptionPane sould be displayed.
	 */
	public final void setDisplayErrors(boolean display) {
		displayErrors = display;
	}

	private boolean processValue() {
		String txt = this.getText();

		if (contains(INT) || contains(DOUBLE)) {
			double d;

			try {
				d = Double.parseDouble(txt);
			} catch (Exception e) {
				e.printStackTrace();
				error(DOUBLE);
				return false;
			}

			if (contains(NOT_ZERO)) {
				if (d == 0) {
					error(NOT_ZERO);
					return false;
				}

			}

			if (contains(POSITIVE)) {
				if (d < 0) {
					error(POSITIVE);
					return false;
				}

			}

			if (contains(NEGATIVE)) {
				if (d > 0) {
					error(NEGATIVE);
					return false;
				}

			}

			if (contains(ROUND)) {
				d = Math.round(d);
			}

			if (contains(ROUND_UP)) {
				d = Math.ceil(d);
			}

			if (contains(ROUND_DOWN)) {
				d = Math.floor(d);
			}

			if (contains(INT)) {
				try {
					integerValue = (int) (d);

				} catch (Exception e) {
					e.printStackTrace();
					error(INT);
					return false;
				}

			}

			if (contains(DOUBLE))
				doubleValue = d;

		}

		// STRING:

		String content = getText();

		if (contains(NO_SPACES)) {
			if (content.contains(" ")) {
				error(NO_SPACES);
				return false;
			}

		}

		if (contains(CLEAR_SPACES)) {
			content = content.replaceAll("\\s+", "");
		}

		stringValue = content;

		if (contains(CLEAR_ON_ENTER)) {
			setText("");
		}

		if (contains(CONSOLE_HISTORY)) {

			// Limit the number of entries to 50
			if (history.size() == 50)
				history.remove(0);

			history.add("" + content);
			history_nav = 0;
		}

		return true;

	}

	private void error(int flag) {
		handleError(flag);

		if (displayErrors)
			display(flag);

	}

	private void display(int flag) {

		String msg = "";

		switch (flag) {
		case INT:
			msg = "Please enter a valid integer!";
			break;
		case DOUBLE:
			msg = "Please enter a valid number!";
			break;
		case NOT_ZERO:
			msg = "Please enter a value that is not 0!";
			break;
		case POSITIVE:
			msg = "Please enter a value above 0!";
			break;
		case NEGATIVE:
			msg = "Please enter a value below 0!";
			break;
		case NO_SPACES:
			msg = "Input mustn't contain spaces!";
			break;
		}

		JOptionPane.showMessageDialog(null, msg);
	}

	private boolean contains(int i) {

		for (Integer x : flags) {
			if (i == x)
				return true;
		}

		return false;
	}

	/**
	 * Overridable method which is triggered when an InputError occurred.
	 * 
	 * @param flag
	 *            The Flag on which the error occurred.
	 */
	public void handleError(int flag) {
		System.err.println("An error occurred using the flag " + flag + "\nOverride this method to handle errors!");
	}

	/**
	 * Overridable method which is triggered when the user hits enter to apply
	 * his input.
	 */
	public void update() {
		System.err.println("The value of this X0InputField was updated\nOverride this method to handle updates!");
	}

}
