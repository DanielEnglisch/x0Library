package org.xeroserver.x0library.parser;

import java.util.Map;

public class Command {
	
	public static final int 
	SINGLE_ARGS_FLAGS_VALUES = 1,  //HEAD [VALUE] [-ARG VALUE] [--FLAG]	--> update_app csgo --force -dir "C:\CS:GO\"
	VALUES_ONLY = 0;				//HEAD [VALUE]						--> remove_api "C:\APIs\x0 API Ref\x0.api" force

	private int type = -1;
	private String head = null;
	private String[] flags = null;
	private Map<String,String> arguments = null;
	private String[] values = null;
	
	//Null command
	public Command()
	{
		
	}
	
	//VALUE_CHAIN
	public Command(String head, String[] values){
		this.head = head;
		this.values = values;
		this.type = Command.VALUES_ONLY;
	}
	
	//DASHED_ARGS_AND_FLAGS
	public Command(String head, Map<String,String> arguments,String[] flags, String[] values ){
		this.head = head;
		this.flags = flags;
		this.arguments = arguments;
		this.values = values;
		this.type = Command.SINGLE_ARGS_FLAGS_VALUES;
	}
	
	public final String[] getFlags() {
		return flags;
	}

	public final Map<String,String> getArguments() {
		return arguments;
	}

	public final String[] getValues() {
		return values;
	}

	public final int getType() {
		return type;
	}

	public final String getHead() {
		return head;
	}
	
	public final String getValue(int index){
		
		try
		{
			if(index < values.length && index >= 0)
				return values[index];
			else
				throw new ValueIndexOutOfBoundException();	
		}catch(ValueIndexOutOfBoundException e){
			e.printStackTrace();
			return "";
		}
			
	}	
	
}

class ValueIndexOutOfBoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
}
