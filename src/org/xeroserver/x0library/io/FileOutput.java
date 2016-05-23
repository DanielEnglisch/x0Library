package org.xeroserver.x0library.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public final class FileOutput {

	private FileOutput() {
		/* Avoid Instantiating */}

	public static boolean writeStringList(List<String> a, File f) {
		BufferedWriter out = null;

		try {
			out = new BufferedWriter(new FileWriter(f));

			for (Object s : a) {
				out.write(s.toString() + "\n");
			}

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;

	}

}
