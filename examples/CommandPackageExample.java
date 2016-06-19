package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.command.CommandBuilder;
import org.xeroserver.x0library.command.CommandParser;
import org.xeroserver.x0library.command.CommandParser.Command;
import org.xeroserver.x0library.command.CommandRule;
import org.xeroserver.x0library.command.CommandRule.CommandReport;

public class CommandPackageExample {
	
	public static void main(String[] args) {
		CommandBuilder cmdBuilder = new CommandBuilder("app_update", "/" , "#");
		String inputCommand = cmdBuilder.addValue("csgo").addFlag("force").addArgument("dir", "C:\\csgo\\").build();
		
		System.out.println("Input command: "+ inputCommand);
		
		CommandParser parser = new CommandParser("/" , "#");
		Command command = parser.parse(inputCommand);
				
		CommandRule rule = new CommandRule("app_update");
		rule.addFlag("force").addMandatoryArgument("dir")
		.setMininalNumberOfValues(1).setMaximalNumberOfValues(1)
		.setAllowedValuesAtPosition(0, new String[]{"csgo","minecraft"});
		
		System.out.println("Command rule: ");
		rule.list();
		
		CommandReport report = command.matches(rule);
		report.list();
	}
}
