package org.xeroserver.x0library.gui;

/**
 * This class is an extension of JTextField and adds the ability to set certain flags to filter incoming values.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-08-31
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class X0InputField extends JTextField {

	private static final long serialVersionUID = 1L;

	// Mode enum
	final public static int

	// NumberFlags:
	// Conditions:
	INT = 0, DOUBLE = 1, NOT_ZERO = 2, POSITIVE = 3, NEGATIVE = 4,
			// Imperatives
			ROUND_UP = 5, ROUND_DOWN = 6, ROUND = 7,

	// StringFlags:
	// Conditions:
			NO_SPACES = 8,
			// Imperatives:
			CLEAR_SPACES = 9,
			CLEAR_ON_ENTER = 10,
			NO_COLOR = 11,
			CONSOLE_HISTORY = 12;

	private int[] flags = new int[] {};

	private double doubleValue = 0;
	private int integerValue = 0;
	private String stringValue = "";
	
	private ArrayList<String> history = new ArrayList<String>();
	private int history_nav = 0;

	private Color errorColor = Color.RED;
	private Color editColor = Color.ORANGE;

	private boolean displayErrors = false;

	public X0InputField(int[] flags, boolean display) {
		this.flags = flags;
		this.displayErrors = display;
		registerKeyListener();
		
		if(contains(NO_COLOR))
		{
			errorColor = Color.BLACK;
			editColor = Color.BLACK;
		}
					
	}

	public X0InputField(int[] flags) {
		this(flags, false);

	}

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
				
				//Console Mechanism
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					
					if(!contains(CONSOLE_HISTORY))
						return;
					
					if(history.size() != 0 && history.size() - 1 - history_nav >= 0 )
						setText(history.get(history.size() - 1 - history_nav));
					
					if(history_nav < history.size() -1)
					history_nav++;
			
				}
				
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					
					if(!contains(CONSOLE_HISTORY))
						return;
					
					if(history.size() != 0 && history.size() - 1 - history_nav >= 0 )
						setText(history.get(history.size() - 1 - history_nav));
					
					if(history_nav > 0)
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

	public final double getDoubleValue() {
		return doubleValue;
	}

	public final int getIntegerValue() {
		return integerValue;
	}

	public final String getStringValue() {
		return stringValue;
	}

	public final void setEditColor(Color c) {
		editColor = c;
	}

	public final void setErrorColor(Color c) {
		errorColor = c;
	}

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
		
		
		if(contains(CLEAR_ON_ENTER)){
			setText("");
		}
		
		
		
		if(contains(CONSOLE_HISTORY))
		{
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

	//Override
	public void handleError(int flag) {
		System.err.println("An error occurred using the flag " + flag + "\nOverride this method to handle errors!");
	}
	
	public void update() {
		System.err.println("The value of this X0InputField was updated\nOverride this method to handle updates!");
	}

}

