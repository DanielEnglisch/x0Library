package org.xeroserver.x0library.log;

// Define a custom Logger which prints information differently
class MyLogger extends Logger{
	
	public MyLogger(String name) {
		super(name);
	}
	
	public void dataOutput(LoggerData data) {
		System.out.println(data.getTime() + " - " + data.getPrefix() + " - " + data.getMessage());
	}
	
}

public class LoggerExample {
	
	public static void main(String[] args) throws InterruptedException {
		// Create two loggers
		MyLogger root = new MyLogger("Root");
		MyLogger sub = new MyLogger("Sub");
		
		// Set root as parent logger
		sub.setParentLogger(root);
		
		// Enable timestamp
		root.setTimestampEnabled(true);
		
		// Log messages
		root.info("Hallo");
		sub.error("Yolo");
	}
		
}




