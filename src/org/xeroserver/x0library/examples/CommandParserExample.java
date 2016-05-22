package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.log.Logger;
import org.xeroserver.x0library.parser.Command;
import org.xeroserver.x0library.parser.CommandParser;

public class CommandParserExample {
	
	public static void main(String[] args){
		
		Logger l = new Logger("CommandParser");
		//l.showGUI();
		
		l.log("##### VALUES_ONLY #####");
		
		String cmd = "apt-get install mysql-server php5 apache2 \"x0 Framework\"";
		l.log("INPUT: "+cmd);
		
		CommandParser cp = new CommandParser();
		Command c = cp.parse(cmd, Command.VALUES_ONLY);
		
		l.log("Head: " + c.getHead());
		l.log("Type: "+ c.getType());
		l.log("### Values: ###");
		
		for(String s: c.getValues())
			l.log(s);
		
		l.log("##### SINGLE_ARGS_FLAGS_VALUES #####");
		String cmd2 = "app_update csgo --yolo -fx 22 -dir \"C:\\yolo negev\" pipi --force";


		l.log("INPUT: " + cmd2);
		Command d = cp.parse(cmd2, Command.SINGLE_ARGS_FLAGS_VALUES);
		
		
		l.log("Head: " + d.getHead());
		l.log("Type: "+ d.getType());

		
		l.log("### Flags: ###");
		for(String s: d.getFlags())
			l.log(s);
		
		
		l.log("### Args - Values: ###");
		d.getArguments().forEach((k,v) ->{
			l.log(k + " - " + v);
		});
		
		
		l.log("### Values: ###");
		for(String s: d.getValues())
			l.log(s);
		
		//Checking and Getting
		if(d.hasFlag("force"))
		{
			l.log("Forcing...");

			if(d.hasArgument("fx"))
			{
				l.log("-fx="+d.getArgumentValue("fx"));
			}
		}
			
	}
}
