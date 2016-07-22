package org.xeroserver.x0library.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.xeroserver.x0library.objtools.StringTools;

public final class AppUpdater {

	private String updateURL = null;
	private JFrame gui = null;
	private JProgressBar pb = null;

	public AppUpdater(String updateURL) {
		this.updateURL = updateURL;
		initGUI();
	}

	private void initGUI() {
		gui = new JFrame("Update Progress");
		pb = new JProgressBar();
		pb.setStringPainted(true);
		gui.setLayout(null);
		pb.setSize(300, 40);
		gui.add(pb);
		gui.setSize(300, 65);
		gui.setLocationRelativeTo(null);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public boolean isUpdateAvailable() {

		return !getRemoteChecksum().equals(getLocalChecksum()) && !getRemoteChecksum().equals("UNKNOWN");
	}

	public boolean showUpdateDialog() {
		return showUpdateDialog("Updater", "There is an update available! Do you want to download it?",
				"Update successful! Please restart your application.", "An error occurred!");
	}

	public boolean showUpdateDialog(String title, String description, String success, String fail) {
		boolean ret = false;
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, description, title, dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			if (update(true)) {
				JOptionPane.showMessageDialog(null, success);
				ret = true;
			} else
				JOptionPane.showMessageDialog(null, fail);
		}
		return ret;
	}

	public boolean update(boolean showProgress) {

		File tmp = new File(".", getAppName() + ".tmp");
		File ori = new File(".", getAppName());

		if (!downloadToTmp(showProgress)) {
			tmp.delete();
			gui.dispose();
			return false;
		}

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(ori));
			BufferedReader in = new BufferedReader(new FileReader(tmp));

			while (in.ready()) {
				out.write(in.readLine() + "\n");
				out.flush();
			}

			out.close();
			in.close();
			tmp.delete();

		} catch (Exception e) {
			e.printStackTrace();
			tmp.delete();
			gui.dispose();
			return false;

		}

		gui.dispose();

		return true;

	}

	private String getAppName() {
		String name = null;
		try {
			name = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getName();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}

		return name;
	}

	private boolean downloadToTmp(boolean showProgress) {
		if (showProgress) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					gui.setVisible(true);
				}

			}).start();
		}

		FileDownloader fd = new FileDownloader(new File(".", getAppName() + ".tmp"), getDownloadLink()) {
			@Override
			public void progressUpdate(double progress) {
				System.out.println("Update Progress " + progress + "%");
				pb.setValue((int) progress);

			}
		};

		boolean downloaded = fd.download();

		if (!downloaded)
			gui.dispose();

		return downloaded;
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
		String sum = "UNKNOWN";

		if (json.equals("ERROR"))
			return sum;

		json = json.replaceAll("\\s", "");
		json = json.replaceAll("\"", "");
		json = json.replaceAll("\\{", "");
		json = json.replaceAll("\\}", "");

		String[] split = json.split(",");
		for (String s : split) {
			if (s.startsWith("checksum")) {
				sum = s.split(":")[1].replaceAll("\"", "");
			}
		}

		return sum;

	}

	public String getDownloadLink() {

		String json = getUpdateData();
		String dw = "UNKNOWN";

		if (json.equals("ERROR"))
			return dw;

		json = json.replaceAll("\\s", "");
		json = json.replaceAll("\"", "");
		json = json.replaceAll("\\{", "");
		json = json.replaceAll("\\}", "");

		String[] split = json.split(",");
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
