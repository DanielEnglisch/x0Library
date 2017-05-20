package org.xeroserver.x0library.net;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * GithubUpdater checks the GithubAPI with a given repository to check for
 * updates and obtain their download links.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 14.5.2017
 * @version 1.1
 *
 */
public class GithubUpdater {

	private String remoteVersion, localVersion, downloadLink;

	/**
	 * Constructor to initialize a GithubUpdater instance.
	 * 
	 * @param token
	 *            "Personal Access Token" generated from
	 *            https://github.com/settings/tokens
	 * @param repository
	 *            Repository slug e.g. "DanielEnglisch/x0Library"
	 * @param localVersion
	 *            Local application version
	 */
	public GithubUpdater(String token, String repository, String locVers) {
		this.localVersion = locVers;

		// Gets the requested API page
		String jsonData = getOAuthedResource(token, repository);

		// Regex parsing
		Pattern version_p = Pattern.compile("tag_name\":\"[^\"]*\"");
		Pattern download_p = Pattern.compile("browser_download_url\":\"[^\"]*\"");

		Matcher m = version_p.matcher(jsonData);
		m.find();
		remoteVersion = m.group().replaceAll("\"", "").split(":")[1];

		m = download_p.matcher(jsonData);
		m.find();
		downloadLink = "";
		String[] spl = m.group().replaceAll("\"", "").split(":");
		for (int i = 1; i < spl.length; i++)
			downloadLink += spl[i] + ":";
		downloadLink = downloadLink.substring(0, downloadLink.length() - 1);

	}

	/**
	 * To check if the local version differs from the remote one.
	 * 
	 * @return true if there is an update available
	 */
	public boolean isUpdateAvailable() {
		return !localVersion.equals(remoteVersion);
	}

	/**
	 * To get the remote version of the latest repository release.
	 * 
	 * @return the remote version as a String e.g "1.2.3"
	 */
	public String getRemoteVersion() {
		return remoteVersion;
	}

	/**
	 * To get the specified local version of the application.
	 * 
	 * @return the local version as a String e.g "1.2.3"
	 */
	public String getLocalVersion() {
		return localVersion;
	}

	/**
	 * To get the download link of the latest repository release.
	 * 
	 * @return a download url e.g. "http://... .zip"
	 */
	public String getDownloadLink() {
		return downloadLink;
	}

	@Override
	public String toString() {
		return "GithubUpdater [isUpdateAvailable()=" + isUpdateAvailable() + ", getRemoteVersion()="
				+ getRemoteVersion() + ", getLocalVersion()=" + getLocalVersion() + ", getDownloadLink()="
				+ getDownloadLink() + "]";
	}

	/**
	 * If there is an update available it shows a simple dialog box containing
	 * information about the version and the download link.
	 */
	public void showUpdateDialog() {
		if (GraphicsEnvironment.isHeadless())
			return;
		if (isUpdateAvailable()) {

			JFrame f = new JFrame("Update Available");
			f.setLocationRelativeTo(null);
			f.setSize(200, 150);
			f.setResizable(false);
			f.setLayout(null);
			JLabel rem = new JLabel("Remote Version: " + remoteVersion);
			JLabel loc = new JLabel("Local Version: " + localVersion);
			rem.setBounds(10, 10, 180, 20);
			loc.setBounds(10, 30, 180, 20);
			f.add(rem);
			f.add(loc);
			JButton downloadBtn = new JButton("Download");
			downloadBtn.addActionListener(e -> {
				System.out.println("Opening: " + downloadLink);
				try {
					Desktop.getDesktop().browse(new URL(downloadLink).toURI());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				}
			});
			downloadBtn.setBounds(10, 50, 180, 35);
			f.add(downloadBtn);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setVisible(true);
		} else {
			System.out.println("There is an update available: " + downloadLink);
		}
	}

	/**
	 * Function to request resources from the GithubAPI using OAuth.
	 * 
	 * @param token
	 *            "Personal Access Token" generated from
	 *            https://github.com/settings/tokens
	 * @param repository
	 *            slug e.g. "DanielEnglisch/x0Library"
	 * @return requested API page
	 */
	private String getOAuthedResource(String token, String url) {
		String ret = null;
		url = "https://api.github.com/repos/" + url + "/releases/latest";

		try {
			URL myURL = new URL(url);
			URLConnection connection = myURL.openConnection();
			token = token + ":x-oauth-basic";
			String authString = "Basic " + Base64.getEncoder().encodeToString(token.getBytes());
			// Sets the token as Authorization Property of the request
			connection.setRequestProperty("Authorization", authString);
			InputStream webInputStream = connection.getInputStream();
			// Reads the response and returns it
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(webInputStream));
			while (in.ready()) {
				ret += in.readLine();
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

}
