package org.xeroserver.x0library.net;

import java.io.File;

import org.xeroserver.x0library.log.Logger;

public class LoggerExample {

	public static void main(String[] args) throws InterruptedException {
		Logger rootLogger = new Logger();
		rootLogger.setTimestampEnabled(true);
		rootLogger.info("Logger loaded!");

		Logger subLogger1 = new Logger();
		Logger subLogger2 = new Logger("Programm2");
		subLogger1.setParentLogger(rootLogger);
		subLogger2.setParentLogger(rootLogger);

		subLogger1.error("There was an error!");
		subLogger1.warning("A warning!");

		subLogger2.info("Everything is fine!");

		
		Logger subsubLogger = new Logger("SubSub");
		subsubLogger.setParentLogger(subLogger1);

		subsubLogger.fatal("I am a yellow submarine!");

		File f = new File("C:\\Users\\Xer0\\Desktop\\tmpLog.txt");

		if (rootLogger.dump(f))
			rootLogger.info("Successfully wrote log!");

		rootLogger.clear();
	}

}
