package org.xeroserver.x0library.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.xeroserver.x0library.objtools.StringTools;

public class AppUpdater {
	private JProgressBar p = null;

	private String updateURL = null;

	public AppUpdater(String updateURL) {
		this.updateURL = updateURL;
	}

	public boolean isUpdateAvailable() {
		return !getRemoteChecksum().equals(getLocalChecksum());
	}

	public boolean update(boolean showProgress) {

		String name = null;
		try {
			name = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getName();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		JFrame f = null;

		if (showProgress) {
			f = new JFrame();
			p = new JProgressBar(0, 100);
			p.setSize(500, 30);
			f.add(p);
			f.pack();
			f.setResizable(false);
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		}

		FileDownloader fd = new FileDownloader(new File(".", name), getDownloadLink()) {
			@Override
			public void progressUpdate(double progress) {
				System.out.println("Update Progress: " + progress);

				if (showProgress) {
					p.setValue((int) progress);
				}
			}
		};

		return fd.download();
	}

	public void showUpdateDialog() {
		Object[] options = { "Yes", "No" };
		int n = JOptionPane.showOptionDialog(null, "There is an update available! Do you want to download it?",
				"Updater", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (n == 0) {
			boolean success = update(true);

			if (success) {
				JOptionPane.showMessageDialog(null, "Successfully applied update! Please restart the program!");

				System.exit(0);

			} else {
				JOptionPane.showMessageDialog(null,
						"Update failed! Check your internet connection and try again later!");
			}
		}

	}

	public String getLocalChecksum() {

		File f = null;
		try {
			f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		String res = "";

		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			FileInputStream fis = new FileInputStream(f);
			byte[] dataBytes = new byte[1024];

			int nread = 0;

			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;

			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			res = sb.toString();
			fis.close();

		} catch (Exception e) {
			return "ERROR";
		}

		return res;

	}

	public String getRemoteChecksum() {
		String json = getUpdateData();
		json = json.replaceAll("\\s", "");
		json = json.replaceAll("\"", "");
		json = json.replaceAll("\\{", "");
		json = json.replaceAll("\\}", "");

		String[] split = json.split(",");
		String sum = "UNKNOWN";
		for (String s : split) {
			if (s.startsWith("checksum")) {
				sum = s.split(":")[1].replaceAll("\"", "");
			}
		}

		return sum;

	}

	public String getDownloadLink() {

		String json = getUpdateData();

		json = json.replaceAll("\\s", "");
		json = json.replaceAll("\"", "");
		json = json.replaceAll("\\{", "");
		json = json.replaceAll("\\}", "");

		String[] split = json.split(",");
		String dw = "UNKNOWN";
		for (String s : split) {
			if (s.startsWith("download")) {
				dw = StringTools.removeXCharsFromStart(s, 9);
			}
		}

		return dw;
	}

	private String getUpdateData() {

		String data = "";

		try {

			URL url = new URL(updateURL);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while (in.ready()) {
				data += in.readLine();
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}

		return data;
	}

}
