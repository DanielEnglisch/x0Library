package org.xeroserver.x0library.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public final class FileInput {

	private FileInput() {
		/* Avoid Instantiating */}

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
