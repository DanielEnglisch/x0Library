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
		//NumberFlags:
		INT = 0,
		DOUBLE = 1,
		NOT_ZERO = 2,
		POSITIVE = 3,
		NEGATIVE = 4,
		ROUND_UP = 5,
		ROUND_DOWN = 6,
		ROUND = 7,
		//StringFlags:
		CLEAR_SPACES = 8,
		NO_SPACES = 9;
	
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
				error(DOUBLE);
				//e.printStackTrace();
				return false;
			}
			
			if(contains(NOT_ZERO))
			{
				if(d == 0)
				{
					error(NOT_ZERO);
					return false;
				}
				
			}
			
			if(contains(POSITIVE))
			{
				if(d < 0)
				{
					error(POSITIVE);
					return false;
				}
				
			}
			
			if(contains(NEGATIVE))
			{
				if(d > 0)
				{
					error(NEGATIVE);
					return false;
				}
					
				
			}
			
			
			if(contains(ROUND))
			{
				d = Math.round(d);
			}
			
			if(contains(ROUND_UP))
			{
				d = Math.ceil(d);
			}
			
			
			if(contains(ROUND_DOWN))
			{
				d = Math.floor(d);
			}
			
				
			if(contains(INT))
			{
				try {
					integerValue = (int)(d);

				} catch (Exception e) {
					error(INT);
					return false;
				}
				
				
			}
			
			
			if(contains(DOUBLE))
				doubleValue = d;

				
		}
		else //IF STRING:
		{
			String content = getText();
			
			if(contains(NO_SPACES))
			{
				if(content.contains(" "))
				{
					error(NO_SPACES);
					return false;
				}
				
			}
			
			if(contains(CLEAR_SPACES))
			{
				content = content.replaceAll("\\s+", "");
			}
			
			stringValue = content;
		}
		
		return true;
		
	}
	
	private void error(int flag)
	{
		handleError(flag);
		
		if(displayErrors)
			display(flag);
	
	}
	
	private void display (int flag)
	{
		
		
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
			case NO_SPACES: msg = "Input mustn't contain spaces!";
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
