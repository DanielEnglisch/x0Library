package org.xeroserver.x0_Library.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This class provides the ability to read certain data from file.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-31
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

public class FileInput {

	public static ArrayList<String> readStringList(File f) {

		ArrayList<String> ar = new ArrayList<String>();

		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(f));

			while (in.ready()) {
				ar.add(in.readLine());
			}

			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ar;

	}

}
