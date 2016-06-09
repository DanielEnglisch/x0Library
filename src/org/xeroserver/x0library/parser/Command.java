package org.xeroserver.x0library.parser;

import java.util.Arrays;
import java.util.Map;

public class Command {

	private String head = null;
	private String[] flags = null;
	private Map<String, String> arguments = null;
	private String[] values = null;

	// DASHED_ARGS_AND_FLAGS
	public Command(String head, Map<String, String> arguments, String[] flags, String[] values) {
		this.head = head;
		this.flags = flags;
		this.arguments = arguments;
		this.values = values;
	}
	
	public final int numberOfValues(){
		return values.length;
	}
	
	public final int numberOfArguments(){
		return arguments.size();
	}
	
	public final int numberOfFlags(){
		return flags.length;
	}

	public final String[] getFlags() {
		return flags;
	}

	public final Map<String, String> getArguments() {
		return arguments;
	}

	public final String[] getValues() {
		return values;
	}

	public final String getHead() {
		return head;
	}

	public final String getValue(int index) {

		try {
			if (index < values.length && index >= 0)
				return values[index];
			else
				throw new ValueIndexOutOfBoundException();
		} catch (ValueIndexOutOfBoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public final boolean hasFlag(String flag) {
		return Arrays.asList(flags).contains(flag);
	}

	public final boolean hasArgument(String argument) {
		return arguments.containsKey(argument);
	}

	public final String getArgumentValue(String argument) {
		return arguments.get(argument);
	}

}

class ValueIndexOutOfBoundException extends Exception {

	private static final long serialVersionUID = 1L;

}
