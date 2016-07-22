package org.xeroserver.x0library.examples;

import java.io.File;

import org.xeroserver.x0library.log.Logger;

public class LoggerExample {

	public static void main(String[] args) throws InterruptedException {
		Logger rootLogger = new Logger("Root");
		rootLogger.setTimestampEnabled(true);
		rootLogger.info("Logger loaded!");

		Logger subLogger1 = new Logger(rootLogger, "Programm1");
		Logger subLogger2 = new Logger(rootLogger, "Programm2");

		subLogger1.error("There was an error!");
		subLogger1.warning("A warning!");

		subLogger2.info("Everything is fine!");

		Logger subsubLogger = new Logger(subLogger1, "SubSub");
		subsubLogger.fatal("I am a yellow submarine!");

		File f = new File("tmp/tmpLog.txt");

		if (rootLogger.dump(f))
			rootLogger.info("Successfully wrote log!");

		rootLogger.clear();
	}

}
