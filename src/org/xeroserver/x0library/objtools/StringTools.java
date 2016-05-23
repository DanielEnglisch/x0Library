package org.xeroserver.x0library.objtools;

public final class StringTools {

	private StringTools() {
		/* Avoid Instantiating */}

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
