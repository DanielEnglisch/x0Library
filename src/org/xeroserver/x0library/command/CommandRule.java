package org.xeroserver.x0library.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.xeroserver.x0library.command.CommandParser.Command;

public class CommandRule {

	private String head = "";
	private ArrayList<String> mandatoryArguments = new ArrayList<String>();
	private ArrayList<String> optionalArguments = new ArrayList<String>();
	private ArrayList<String> allowedFlags = new ArrayList<String>();
	private Hashtable<Integer, String[]> allowedValuesAtPosition = new Hashtable<Integer, String[]>();
	private int maxValues = -1;
	private int minValues = -1;

	public CommandRule(String head) {
		this.head = head;
	}

	public String getHead() {
		return head;
	}

	public CommandRule addMandatoryArgument(String name) {
		mandatoryArguments.add(name);
		return this;
	}

	public CommandRule addOptionalArgument(String name) {
		optionalArguments.add(name);
		return this;
	}

	public CommandRule addFlag(String flag) {
		allowedFlags.add(flag);
		return this;
	}

	public CommandRule setAllowedValuesAtPosition(int pos, String[] values) {
		allowedValuesAtPosition.put(pos, values);
		return this;
	}

	public CommandRule setMininalNumberOfValues(int num) {
		minValues = num;

		if (maxValues < minValues)
			maxValues = num;

		return this;
	}

	public CommandRule setMaximalNumberOfValues(int num) {
		maxValues = num;

		if (minValues > maxValues)
			minValues = num;

		return this;
	}

	public void list() {
		System.out.println("CommandRule for command '" + head + "'");

		System.out.print("Mandatory Arguments: ");
		for (String s : mandatoryArguments)
			System.out.print(s + " ");
		System.out.print("\n");

		System.out.print("Optional Arguments: ");
		for (String s : optionalArguments)
			System.out.print(s + " ");
		System.out.print("\n");

		System.out.print("Allowed Flags: ");
		for (String s : allowedFlags)
			System.out.print(s + " ");
		System.out.print("\n");

		System.out.println("Allowed Values at Positions: ");

		allowedValuesAtPosition.forEach((pos, values) -> {
			System.out.print("Pos " + pos + ": ");
			for (String s : values)
				System.out.print(s + " ");
			System.out.print("\n");
		});

		System.out.println("Value range: " + minValues + " < numOfValues < " + maxValues);

		System.out.println("#########################");
	}

	public CommandReport isValid(Command c) {

		CommandReport report = new CommandReport();

		// If the head doesn't match
		if (!c.getHead().equals(head))
			report.addErrorMessage("Header missmatch!");

		// Check mandatory arguments
		for (String arg : mandatoryArguments) {
			if (!c.getArguments().containsKey(arg))
				report.addErrorMessage("Missing argument '" + arg + "' !");
		}

		// Check optional arguments
		c.getArguments().forEach((k, v) -> {
			if (!mandatoryArguments.contains(k) && !optionalArguments.contains(k))
				report.addErrorMessage("Unknown argument '" + k + "' !");
		});
		// -------------------------

		// Check flags
		for (String f : c.getFlags()) {
			if (!allowedFlags.contains(f))
				report.addErrorMessage("Unknown flag '" + f + "' !");
		}

		// Check number of values
		if (minValues != -1 || maxValues != -1)
			if (c.numberOfValues() < minValues || c.numberOfValues() > maxValues)
				report.addErrorMessage("Number of values (" + c.numberOfValues() + ") is not between " + minValues
						+ " and " + maxValues + " !");

		// Check command positions and entries
		allowedValuesAtPosition.forEach((pos, values) -> {

			if (c.getValue(pos) == null)
				report.addErrorMessage("Missing value at position " + pos + " !");
			else {
				if (!Arrays.asList(values).contains(c.getValue(pos))) {
					String valueReport = "";

					for (String s : values)
						valueReport += "'" + s + "' ";

					report.addErrorMessage("Value '" + c.getValue(pos) + "' is not allowed at position " + pos
							+ "! Allowed: " + valueReport);
				}
			}
		});

		return report;
	}

	public class CommandReport {

		private boolean hasErrors = false;
		private ArrayList<String> errorMessages = new ArrayList<String>();

		public CommandReport() {
		}

		public void addErrorMessage(String errorMessage) {
			errorMessages.add(errorMessage);
			hasErrors = true;
		}

		public boolean hasErrors() {
			return hasErrors;
		}

		public void list() {
			if (hasErrors) {
				for (String s : errorMessages) {
					System.out.println(s);
				}

			} else {
				System.out.println("No errors found!");
			}
		}

	}

}
