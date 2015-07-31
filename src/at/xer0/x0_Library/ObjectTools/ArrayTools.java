package at.xer0.x0_Library.ObjectTools;

/**
 * This class provides the ability to perform array operations.
 * One example would be the conversion of a String ArrayList to an Integer ArrayList.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-31
 **/

import java.util.ArrayList;

public class ArrayTools {
	
	public static ArrayList<Integer> convertToIntArrayList(ArrayList<?> array)
	{
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		for(Object o : array)
		{
			try
			{
				Integer i = Integer.parseInt(o.toString());
				ret.add(i);
			}
			catch(Exception e)
			{}
		}
		
		return ret;
		
	}
	

}
