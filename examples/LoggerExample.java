package org.xeroserver.x0library.log;

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
		MyLogger root = new MyLogger("Root");
		MyLogger sub = new MyLogger("Sub");
		sub.setParentLogger(root);
		root.setTimestampEnabled(true);
		root.info("Hallo");
		sub.error("Yolo");
	}
		
}




