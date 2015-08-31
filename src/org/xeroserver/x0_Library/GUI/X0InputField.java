package org.xeroserver.x0_Library.GUI;

import javax.swing.JTextField;

import org.xeroserver.x0_Library.Log.Logger;

public class X0InputField extends JTextField{
	
	private static final long serialVersionUID = 1L;

	
	// LoggerBlock:
	private Logger l = new Logger("X0InputField", Logger.SILENT);

	public Logger getLogger() {
		return l;
	}
	// -----------
	
	//Mode enum
	public static int 
		INT = 0,
		DOUBLE = 1;
	
	private int[] flags;
	
	public X0InputField(int[] flags)
	{
		this.flags = flags;
	}
	
	

}
