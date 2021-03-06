package org.xeroserver.x0library.examples;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.xeroserver.x0library.gui.InputField;

public class X0InputFieldExample {

	public static void main(String[] args) {

		JFrame f = new JFrame("X0InputFieldExample");
		f.setSize(300, 200);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setLayout(null);

		JLabel l1 = new JLabel("Most basic input Field");
		l1.setBounds(0, 0, 300, 25);
		f.add(l1);

		// Most basic input Field
		@SuppressWarnings("serial")
		InputField xif1 = new InputField() {
			@Override
			public void update() {
				System.out.println("xif1: " + getStringValue());
			}
		};
		xif1.setBounds(0, 25, 300, 25);
		f.add(xif1);
		// -------------------------------------

		JLabel l2 = new JLabel("InputField using Integers");
		l2.setBounds(0, 50, 300, 25);
		f.add(l2);

		// InputField using Integers
		@SuppressWarnings("serial")
		InputField xif2 = new InputField(true, InputField.INT, InputField.POSITIVE, InputField.CLEAR_ON_ENTER ) {
			@Override
			public void update() {
				System.out.println("xif2: " + getIntegerValue());
			}

			@Override
			public void handleError(int flag) {
				System.out.println("xif2: ERROR with flag: " + flag + " for input: " + getStringValue());
			}
		};
		xif2.setBounds(0, 75, 300, 25);
		f.add(xif2);
		// -------------------------------------

		JLabel l3 = new JLabel("InputField using History and NoColor");
		l3.setBounds(0, 100, 300, 25);
		f.add(l3);

		// InputField using History and NoColor
		@SuppressWarnings("serial")
		InputField xif3 = new InputField(false, InputField.CONSOLE_HISTORY, InputField.NO_COLOR, InputField.CLEAR_ON_ENTER) {
			@Override
			public void update() {
				System.out.println("xif3: " + getStringValue());
			}
		};
		xif3.setBounds(0, 125, 300, 25);
		f.add(xif3);
		// -------------------------------------

		f.setVisible(true);

	}

}
