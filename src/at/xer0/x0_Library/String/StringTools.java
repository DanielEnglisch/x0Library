package at.xer0.x0_Library.String;

/**
 * This class provides the ability to perform simple string operations. For
 * everyone who doesn't understand substring and regex too well.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-24
 **/

public class StringTools {

	public static String getFirstChar(String s) {
		return s.substring(0, 1);
	}

	public static String getLastChar(String s) {
		return s.substring(s.length() - 1);
	}

	public static String removeFirstChar(String s) {
		return s.substring(1);
	}

	public static String removeLastChar(String s) {
		return s.substring(0, s.length() - 1);
	}

}
