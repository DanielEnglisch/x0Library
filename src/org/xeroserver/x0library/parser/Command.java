package org.xeroserver.x0library.parser;

public class Command {
	
	public static final int 
	SINGLE_ARGS_FLAGS_VALUES = 1,  //HEAD [VALUE] [-ARG VALUE] [--FLAG]	--> update_app csgo --force -dir "C:\CS:GO\"
	VALUES_ONLY = 0;				//HEAD [VALUE]						--> remove_api "C:\APIs\x0 API Ref\x0.api" force

	private int type = -1;
	private String head = null;
	private Flag[] flags = null;
	private Argument[] arguments = null;
	private String[] values = null;
	
	//VALUE_CHAIN
	public Command(String head, String[] values){
		this.head = head;
		this.values = values;
		this.type = Command.VALUES_ONLY;
	}
	
	//DASHED_ARGS_AND_FLAGS
	public Command(String head, Argument[] arguments,Flag[] flags, String[] values ){
		this.head = head;
		this.flags = flags;
		this.arguments = arguments;
		this.values = values;
		this.type = Command.SINGLE_ARGS_FLAGS_VALUES;
	}
	
	public final Flag[] getFlags() {
		return flags;
	}

	public final Argument[] getArguments() {
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
	


	
}
