package org.xeroserver.x0library.net;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.xeroserver.x0library.objtools.StringTools;

public class AppUpdater {

	private String updateURL = null;

	public AppUpdater(String updateURL) {
		this.updateURL = updateURL;
	}

	public boolean isUpdateAvailable() {

		return !getRemoteChecksum().equals(getLocalChecksum()) && !getRemoteChecksum().equals("UNKNOWN");
	}

	private boolean update(UpdaterGUI gui) {

		String name = null;
		try {
			name = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getName();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		FileDownloader fd = new FileDownloader(new File(".", name), getDownloadLink()) {
			@Override
			public void progressUpdate(double progress) {
				gui.updateProgressbar(progress);
			}
		};

		return fd.download();
	}

	public void showUpdateDialog(String title, String description) {
		UpdaterGUI gui = new UpdaterGUI(this, title, description);
		gui.setVisible(true);
	}

	public void showUpdateDialog() {
		UpdaterGUI gui = new UpdaterGUI(this, "Updater",
				"<html><center>There is an update available!<br>Do you want to update?</center></html>");
		gui.setVisible(true);
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

	private class UpdaterGUI extends JDialog {

		private static final long serialVersionUID = 1L;
		private final JPanel contentPanel = new JPanel();
		public JProgressBar progressBar = new JProgressBar();

		public void updateProgressbar(double p) {
			progressBar.setValue((int) p);
		}

		public UpdaterGUI(AppUpdater parent, String title, String text) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setResizable(false);
			setTitle(title);
			setAlwaysOnTop(true);
			setBounds(100, 100, 250, 160);
			getContentPane().setLayout(new BorderLayout());
			setLocationRelativeTo(null);
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);
			{
				progressBar.setStringPainted(true);
				progressBar.setBounds(10, 57, 224, 30);
				contentPanel.add(progressBar);
			}

			JLabel lblEsIstEin = new JLabel(text);
			lblEsIstEin.setHorizontalAlignment(SwingConstants.CENTER);
			lblEsIstEin.setBounds(10, 11, 224, 35);
			contentPanel.add(lblEsIstEin);
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("NO");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							dispose();
						}
					});
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("YES");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (parent.update(UpdaterGUI.this)) {
								String name = null;
								try {
									name = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
											.toURI().getPath()).getName();
								} catch (URISyntaxException e2) {
									e2.printStackTrace();
								}

								JOptionPane.showMessageDialog(null,
										"Update process finished! The application will now restart!");
								try {
									Runtime.getRuntime().exec("java -jar " + name);
								} catch (IOException e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null,
											"Couldn't restart the application! Please restart it manually!");

								}
								System.exit(0);
							} else
								JOptionPane.showMessageDialog(null, "There was an error updating the application!");
							System.exit(0);
						}
					});
					buttonPane.add(cancelButton);
				}
			}
		}
	}

}
