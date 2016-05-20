package org.xeroserver.x0library.parser;

public class Argument {
	
	private String name = null;
	private String[] values = null;
	
	public Argument(String name, String[] values){
		
	}

	public final String getName() {
		return name;
	}

	public final String[] getValues() {
		return values;
	}
	
	public final String getValue(int index){
		return values[index];
	}

}
