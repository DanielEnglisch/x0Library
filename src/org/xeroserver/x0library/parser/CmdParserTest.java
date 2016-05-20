package org.xeroserver.x0library.parser;

public class CmdParserTest {
	
	public static void main(String[] args){
		CommandParser cp = new CommandParser();
		Command c = cp.parse("apt-get install mysql-server php5 apache2 \"x0 Framework\"", Command.VALUES_ONLY);
		
		System.out.println("Head: " + c.getHead() + "\nType: " + c.getType() + "\nValues:");
		for(String s: c.getValues())
			System.out.println(s);
	}
}
