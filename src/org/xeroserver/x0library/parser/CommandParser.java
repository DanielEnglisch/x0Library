package org.xeroserver.x0library.parser;

import java.util.ArrayList;

import org.xeroserver.x0library.objtools.StringTools;

public class CommandParser {
		
	public final Command parse(String cmd, int type){
		return parse(cmd.split("\\s"), type);
	}
	
	public final Command parse(String[] cmd, int type)
	{
		try{
			switch(type)
			{
				case Command.VALUES_ONLY: return parseValueChain(cmd, type);
				default: throw new UnknownCommandTypeException();
			}
			
		}catch(UnknownCommandTypeException ex)
		{
			ex.printStackTrace();
			return null;
		}
		
	}
	
	private Command parseValueChain(String[] cmd, int type){
		
		ArrayList<String> values = null;
		
		if(cmd.length > 1)
		{
			values =  new ArrayList<String>();
			for(int i = 1; i < cmd.length; i++)
			{
				String value = "";
				
				if(cmd[i].startsWith("\'") || cmd[i].startsWith("\""))
				{	
					cmd[i] = StringTools.removeFirstChar(cmd[i]);
					
					for(int x = i; x < cmd.length; x++)
					{
						if(!cmd[x].endsWith("\'") && !cmd[x].endsWith("\""))
						{
							value+=cmd[x] + " ";
						}else
						{
							cmd[x] = StringTools.removeLastChar(cmd[x]);
							value+=cmd[x];
							i = x;
							break;
						}
					}
				}else
				{
					value = cmd[i];
				}
				
				values.add(value);
			}
		}
				
		return new Command(cmd[0], values.toArray(new String[values.size()]));
	}
}

class UnknownCommandTypeException extends Exception{

	private static final long serialVersionUID = 1946439405140135280L;
	
}
