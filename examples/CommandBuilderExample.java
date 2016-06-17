package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.command.CommandBuilder;

public class CommandBuilderExample {
	
	public static void main(String[] args) {
		
		String cmd = new CommandBuilder("app_update").addValue("csgo").addFlag("yolo").addArgument("fx", "22 - 2 = 0").addArgument("dir","C:\\yolo negev").addValue("pipi").addFlag("force").build();
		System.out.println("Generated command string: " + cmd);
		
	}

}
