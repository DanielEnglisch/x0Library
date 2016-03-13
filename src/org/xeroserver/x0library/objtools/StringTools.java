package org.xeroserver.x0library.objtools;

/**
 * This class provides the ability to perform simple string operations. For
 * everyone who doesn't understand substring and regex too well.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-24
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

public final class StringTools {

	private StringTools(){/*Avoid Instantiating*/}
	
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
