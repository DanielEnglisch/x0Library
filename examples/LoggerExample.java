package org.xeroserver.x0library.examples;

import java.io.File;

import org.xeroserver.x0library.log.Logger;

public class LoggerExample {

	public static void main(String[] args) {
		
		Logger l1 = new Logger("Example Logger");
		
		l1.showGUI();
		
		l1.log("NORMAL");
		l1.info("This is an informational message");
		l1.warning("This is my only warning");
		l1.error("An error occurred");
		l1.fatal("A fatal error occured during the execution...");
		l1.custom("CustomType", "This is a custom message");
		
		System.out.println("###################################");
		
		Logger l2 = new Logger(Logger.ERRORS_ONLY);
		l2.log("NORMAL");
		l2.info("This is an informational message");
		l2.warning("This is my only warning");
		l2.error("An error occurred");
		l2.fatal("A fatal error occured during the execution...");
		l2.custom("CustomType", "This is a custom message");
		
		l2.write(new File(".\\tmp\\report.log"));
				
	}

}
