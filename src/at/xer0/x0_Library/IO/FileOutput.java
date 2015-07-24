package at.xer0.x0_Library.IO;

/**
 * This class provides the ability to write certain data to file.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-24
 **/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileOutput {
	
	public static void writeArrayList(ArrayList<?> a, File f)
	{
		BufferedWriter out = null;
		
		try
		{
			out = new BufferedWriter(new FileWriter(f));
			
			for(Object s : a)
			{
				out.write(s.toString() + "\n");
			}
			
			out.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

}
