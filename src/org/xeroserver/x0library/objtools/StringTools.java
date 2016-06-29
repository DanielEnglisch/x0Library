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

	public static String removeXCharsFromStart(String s, int x) {
		return s.substring(x);
	}

	public static String removeXCharsFromEnd(String s, int x) {
		return s.substring(0, s.length() - x);
	}

}
