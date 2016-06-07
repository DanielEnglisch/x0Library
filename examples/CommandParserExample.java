package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.parser.Command;
import org.xeroserver.x0library.parser.CommandParser;

public class CommandParserExample {

	public static void main(String[] args) {

		CommandParser cp = new CommandParser();
		
		System.out.println("##### Default flag and arg prefix #####");
		String cmd2 = "app_update csgo --yolo -fx 22 -dir \"C:\\yolo negev\" pipi --force";

		System.out.println("INPUT: " + cmd2);
		Command d = cp.parse(cmd2);

		System.out.println("Head: " + d.getHead());

		System.out.println("### Flags: ###");
		for (String s : d.getFlags())
			System.out.println(s);

		System.out.println("### Args - Values: ###");
		d.getArguments().forEach((k, v) -> {
			System.out.println(k + " - " + v);
		});

		System.out.println("### Values: ###");
		for (String s : d.getValues())
			System.out.println(s);

		// Checking and Getting
		if (d.hasFlag("force")) {
			System.out.println("Forcing...");

			if (d.hasArgument("fx")) {
				System.out.println("-fx=" + d.getArgumentValue("fx"));
			}
		}
		
		cp = new CommandParser("#","/");
		
		System.out.println("##### Modified flag and arg prefix (# and /) #####");
		String cmd = "app_remove rust #delconfig /timeout 60 /dir \"C:\\rust dedicated\" xer0 #force";

		System.out.println("INPUT: " + cmd);
		Command d2 = cp.parse(cmd);

		System.out.println("Head: " + d2.getHead());

		System.out.println("### Flags: ###");
		for (String s : d2.getFlags())
			System.out.println(s);

		System.out.println("### Args - Values: ###");
		d2.getArguments().forEach((k, v) -> {
			System.out.println(k + " - " + v);
		});

		System.out.println("### Values: ###");
		for (String s : d2.getValues())
			System.out.println(s);

		// Checking and Getting
		if (d2.hasFlag("force")) {
			System.out.println("Forcing...");

			if (d2.hasArgument("timeout")) {
				System.out.println("/timeout=" + d.getArgumentValue("timeout"));
			}
		}

	}
}
