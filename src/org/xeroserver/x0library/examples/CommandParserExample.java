package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.log.Logger;
import org.xeroserver.x0library.parser.Command;
import org.xeroserver.x0library.parser.CommandParser;

public class CommandParserExample {
	
	public static void main(String[] args){
		
		Logger l = new Logger("SwaggerLogger");
		l.showGUI();
		
		String cmd = "apt-get install mysql-server php5 apache2 \"x0 Framework\"";
		l.log("INPUT: "+cmd);
		
		CommandParser cp = new CommandParser();
		Command c = cp.parse(cmd, Command.VALUES_ONLY);
		
		l.log("Head: " + c.getHead());
		l.log("Type: "+ c.getType());
		l.log("### Values: ###");
		
		for(String s: c.getValues())
			l.log(s);
		
		l.log("#############");
	}
}
