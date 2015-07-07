package at.xer0.x0_Library.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.xer0.x0_Library.Log.Logger;

public class ConfigFile {
	
	private File file = null;
	private String name = "NULL";
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	public ConfigFile(File f)
	{
		name = f.getName();
		file = f;
		
		Logger.log("Initializing " + name + ":");

		parseContents();
	}
	
	private void parseContents()
	{
		if(!file.exists())
		{
			Logger.error("FileNotFound");
			return;
		}
		
		BufferedReader in = null;
		
		try
		{
			in =  new BufferedReader(new FileReader(file));
			
			
			while(in.ready())
			{
				String s = in .readLine();
				
				if(s.substring(0,1).equalsIgnoreCase("#"))
				{
					Logger.log("Skipping comment: " + s);
				}else
				{
					try
					{
						String[] sp = s.split("=");
						String property = sp[0].replaceAll("\\s", "");
						String valueRaw = sp[1];
						String value = "NULL";
						if(valueRaw.substring(0, 1).equals(" "))
						{
							value = valueRaw.substring(1);
						}else
						{
							value = valueRaw;
						}
						
						loadProperty(property, value);
						Logger.log("Parsed: [" + property + " - " + value + "];");
					}
					catch(Exception ex)
					{
						Logger.warning("Failed to parse line: " + s);
					}
				}
				
			
			}
			
			in.close();
		}
		catch(Exception e)
		{
			try {in.close();} catch (IOException e1) {e1.printStackTrace();}
			Logger.error("Error reading file!");
			e.printStackTrace();
			
		}
		
		
	}
	
	public void saveFile()
	{
		
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		BufferedWriter out = null;
		
		try
		{
			out = new BufferedWriter(new FileWriter(file));
			
			for (Map.Entry<String, String> entry : properties.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    
			    out.write(key + " = " + value + "\n");
			    out.flush();
			}
			
			out.close();
		}
		catch(Exception ex)
		{			
			try {out.close();} catch (IOException e1) {e1.printStackTrace();}
			Logger.error("Error writing file!");
			ex.printStackTrace();
		}
		
		Logger.log("Saved " + name);
	}
	
	public void reloadFile()
	{
		Logger.log("Reloading " + name + ":");
		properties.clear();
		parseContents();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void removeProperty(String property)
	{
		properties.remove(property);
		saveFile();
	}
	
	public void addProperty(String property, String value)
	{
		
		properties.put(property, value);
		Logger.log("Added: [" + property + " - " + value + "];");

		saveFile();
	}
	
	private void loadProperty(String property, String value)
	{
		
		properties.put(property, value);
	}
	
	public String getValue(String property)
	{
		if(!isProperty(property)){return "NULL";}
		return properties.get(property);
	}
	
	public void setValue(String property, String value)
	{
		if(isProperty(property))
		{
			properties.replace(property, value);
			saveFile();
		}else
		{
			addProperty(property, value);
		}
		
	}
	
	public boolean isProperty(String property)
	{
		return properties.containsKey(property);
	}

	public void list()
	{
		Logger.log("#####Contents of " + name + ":#####");
		for (Map.Entry<String, String> entry : properties.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    
		    Logger.log(key + " = " + value);
		}		
		Logger.log("#####End of Contents#####");

	}
}
