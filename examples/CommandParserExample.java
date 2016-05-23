package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.parser.Command;
import org.xeroserver.x0library.parser.CommandParser;

public class CommandParserExample {
	
	public static void main(String[] args){
		
		
		System.out.println("##### VALUES_ONLY #####");
		
		String cmd = "apt-get install mysql-server php5 apache2 \"x0 Framework\"";
		System.out.println("INPUT: "+cmd);
		
		CommandParser cp = new CommandParser();
		Command c = cp.parse(cmd, Command.VALUES_ONLY);
		
		System.out.println("Head: " + c.getHead());
		System.out.println("Type: "+ c.getType());
		System.out.println("### Values: ###");
		
		for(String s: c.getValues())
			System.out.println(s);
		
		System.out.println("##### SINGLE_ARGS_FLAGS_VALUES #####");
		String cmd2 = "app_update csgo --yolo -fx 22 -dir \"C:\\yolo negev\" pipi --force";


		System.out.println("INPUT: " + cmd2);
		Command d = cp.parse(cmd2, Command.SINGLE_ARGS_FLAGS_VALUES);
		
		
		System.out.println("Head: " + d.getHead());
		System.out.println("Type: "+ d.getType());

		
		System.out.println("### Flags: ###");
		for(String s: d.getFlags())
			System.out.println(s);
		
		
		System.out.println("### Args - Values: ###");
		d.getArguments().forEach((k,v) ->{
			System.out.println(k + " - " + v);
		});
		
		
		System.out.println("### Values: ###");
		for(String s: d.getValues())
			System.out.println(s);
		
		//Checking and Getting
		if(d.hasFlag("force"))
		{
			System.out.println("Forcing...");

			if(d.hasArgument("fx"))
			{
				System.out.println("-fx="+d.getArgumentValue("fx"));
			}
		}
			
	}
}
