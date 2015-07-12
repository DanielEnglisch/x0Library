package at.xer0.x0_Library.Log;

public class Logger {
	
	
	public static int NORMAL = -1, ERRORS_ONLY = 0,SILENT = 1;

	
	private String parentApp = "ParentApp";
	
	private int option = -1;
	
	public void setMode(int opt) {
		this.option = opt;
	}

	public Logger(String parent)
	{
		parentApp = parent;
	}
	
	public Logger(String parent,int opt)
	{
		parentApp = parent;
		this.option = opt;
	}
	
	
	
	public void log(String s)
	{
		if(option == ERRORS_ONLY){return;}
		if(option == SILENT){return;}
		System.out.println("[" + parentApp + "]" + " [LOG] " + s);
	}
	
	public void info(String s)
	{
		if(option == ERRORS_ONLY){return;}
		if(option == SILENT){return;}
		System.out.println("[" + parentApp + "]" + " [INFO] " + s);
	}
	
	public void warning(String s)
	{
		if(option == ERRORS_ONLY){return;}
		if(option == SILENT){return;}
		System.out.println("[" + parentApp + "]" + " [WARNING] " + s);
	}
	
	public void error(String s)
	{
		if(option == SILENT){return;}
		System.out.println("[" + parentApp + "]" + " [ERROR] " + s);
	}

	public void fatal(String s)
	{
		if(option == SILENT){return;}
		System.out.println("[" + parentApp + "]" + " [FATAL] " + s);
	}
	
}
