package org.xeroserver.x0library.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
				case Command.VALUES_ONLY: return parseValueChain(cmd);
				case Command.SINGLE_ARGS_FLAGS_VALUES: return parseSingleArgsFlagsValues(cmd);
				default: throw new UnknownCommandTypeException();
			}
			
		}catch(UnknownCommandTypeException ex)
		{
			ex.printStackTrace();
			return null;
		}
		
	}
	
	private Command parseSingleArgsFlagsValues(String[] cmd){
		
	    ArrayList<String> cmds = new ArrayList<String>( Arrays.asList( cmd ) );

  		
		ArrayList<String> flags = null;
		ArrayList<String> values = null;
		HashMap<String, String> arguments = null;
		
		if(cmd.length > 1)
		{
			
			 flags = new ArrayList<String>();
			 values = new ArrayList<String>();
			 arguments = new HashMap<String, String>();
			
			//Init flags,values,args when neccesary
			//Parse that shit
			
			 //Filter Flags
			for(int i = 1; i< cmds.size(); i++)
			{
				if(cmds.get(i).startsWith("--"))
				{
					flags.add(StringTools.removeFirstChar(StringTools.removeFirstChar(cmds.get(i))));
					cmds.remove(i);
				}
			}
			
			
			 //Filter Args
			for(int i = 1; i< cmds.size(); i++)
			{
				if(cmds.get(i).startsWith("-"))
				{
					String key = StringTools.removeFirstChar(cmds.get(i));
					String value = "";
					
					cmds.set(i, "");
					
					if(i+1 < cmds.size())
					{
						if(cmds.get(i+1).startsWith("\"")||cmds.get(i+1).startsWith("\'"))
						{
							
							value+=StringTools.removeFirstChar(cmds.get(i+1)) + " ";
							cmds.set(i+1, "");

							i+=2;
							
							while(!cmds.get(i).endsWith("\"")&&!cmds.get(i).endsWith("\'"))
							{
								value+=cmds.get(i) + " ";
								cmds.set(i, "");
								i++;
							}
							
							value+=StringTools.removeLastChar(cmds.get(i));
							cmds.set(i, "");
							
						}else
						{
							value = cmds.get(i+1);
							cmds.set(i+1, "");
						}
						
					}
					
					arguments.put(key, value);
				}
			}
			
			//Filter Values
			cmds.remove(0);
			for(String s: cmds)
			{
				if(!s.equals(""))
					values.add(s);
			}
			
			
			
			
			
			
		}
		
		return new Command(cmd[0], Collections.unmodifiableMap(arguments), flags.toArray(new String[flags.size()]), values.toArray(new String[values.size()]) );
	}
	
	private Command parseValueChain(String[] cmd){
		
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
