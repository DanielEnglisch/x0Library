package org.xeroserver.x0_Library.GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
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
	final public static int 
		INT = 0,
		DOUBLE = 1,
		NOT_ZERO = 2,
		POSITIVE = 3,
		NEGATIVE = 4;
	
	private int[] flags;
	
	private double doubleValue = 0;
	private int integerValue = 0;
	private String stringValue = "";
	
	private Color errorColor = Color.RED;
	private Color editColor = Color.ORANGE;
	
	private boolean displayErrors = false;


	public X0InputField(int[] flags)
	{
		this.flags = flags;
		
		registerKeyListener();
		
	}
	
	public X0InputField()
	{
		this(new int[]{});
	}
	
	private void registerKeyListener()
	{
		this.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(processValue())
					{
						setForeground(Color.GREEN);
						setForeground(Color.BLACK);
						update();

					}
					else
					{
						setForeground(errorColor);

					}
					
				}else
				{
					setForeground(editColor);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}
	
	public double getDoubleValue() {
		return doubleValue;
	}
	public int getIntegerValue() {
		return integerValue;
	}

	public String getStringValue() {
		return stringValue;
	}
	
	public void setEditColor(Color c)
	{
		editColor = c;
	}
	
	public void setErrorColor(Color c)
	{
		errorColor = c;
	}
	
	public void setDisplayErrors(boolean display)
	{
		displayErrors = display;
	}
	
	private boolean processValue()
	{
		String txt = this.getText();
		
		if(contains(INT)||contains(DOUBLE))
		{
			double d;
			
			try {
					d = Double.parseDouble(txt);
			} catch (Exception e) {
				l.error("Failed to convert input to double!");
				handleError(DOUBLE);
				display(DOUBLE);
				//e.printStackTrace();
				return false;
			}
			
			if(contains(NOT_ZERO))
			{
				if(d == 0)
				{
					handleError(NOT_ZERO);
					display(NOT_ZERO);
					return false;
				}
				
			}
			
			if(contains(POSITIVE))
			{
				if(d < 0)
				{
					handleError(POSITIVE);
					display(POSITIVE);
					return false;
				}
				
			}
			
			if(contains(NEGATIVE))
			{
				if(d > 0)
				{
					handleError(NEGATIVE);
					display(NEGATIVE);
					return false;
				}
					
				
			}
				
			if(contains(INT))
			{
				try {
					integerValue = (int)(d);

				} catch (Exception e) {
					handleError(INT);
					display(INT);
					return false;
				}
			}
			
			
			if(contains(DOUBLE))
				doubleValue = d;

				
		}
		else
		{
			stringValue = this.getText();
		}
		
		return true;
		
	}
	
	private void display (int flag)
	{
		if(!displayErrors)
			return;
		
		String msg = "";
		
		switch(flag)
		{
			case INT: msg = "Please enter a valid integer!";
				break;
			case DOUBLE: msg = "Please enter a valid number!";
				break;
			case NOT_ZERO: msg = "Please enter a value that is not 0!";
				break;
			case POSITIVE: msg = "Please enter a value above 0!";
				break;
			case NEGATIVE: msg = "Please enter a value below 0!";
				break;
		}
		
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public void handleError(int flag)
	{
		l.info("Override handleError(int flag) to handle input errors!");
	}
	
	private boolean contains(int i)
	{
		
		for(Integer x : flags)
		{
			if(i == x)
				return true;
		}
		
		return false;
	}
	
	
	public void update()
	{
		l.info("Override update() to get change events!"); 
	}
	
	
	
	

}
