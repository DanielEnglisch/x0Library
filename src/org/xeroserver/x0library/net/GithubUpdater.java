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

import org.xeroserver.x0library.objtools.StringTools;

/**
 * For checking if there is an update available and get the download link of the latest release of a Github repository
 * @author Xer0
 *
 */
public class GithubUpdater {

	private String remoteVersion, localVersion, downloadLink;
	
	/**
	 * Constructor
	 * @param token Generated "Personal Access Token" from https://github.com/settings/tokens
	 * @param url Repository id "DanielEnglisch/x0_Library"
	 * @param locVers Local application version
	 */
	public GithubUpdater(String token, String resource, String locVers) {
		this.localVersion = locVers;
		
		String jsonData = getOAuthedResource(token, resource);

		//Parses the necessary data
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
		downloadLink = StringTools.removeXCharsFromEnd(downloadLink, 1);
		
	}
	
	public boolean isUpdateAvailable(){
		return !localVersion.equals(remoteVersion);
	}
	
	public String getRemoteVersion(){
		return remoteVersion;
	}
	
	public String getLocalVersion(){
		return localVersion;
	}
	
	public String getDownloadLink(){
		return downloadLink;
	}
	
	@Override
	public String toString() {
		return "GithubUpdater [isUpdateAvailable()=" + isUpdateAvailable() + ", getRemoteVersion()="
				+ getRemoteVersion() + ", getLocalVersion()=" + getLocalVersion() + ", getDownloadLink()="
				+ getDownloadLink() + "]";
	}

	/**
	 * Shows update dialog with a download button
	 */
	public void showUpdateDialog(){
		if(isUpdateAvailable() && !GraphicsEnvironment.isHeadless()){
			
			JFrame f = new JFrame("Update Available");
			f.setLocationRelativeTo(null);
			f.setSize(200, 150);
			f.setResizable(false);
			f.setLayout(null);
			JLabel rem = new JLabel("Remote Version: " + remoteVersion);
			JLabel loc = new JLabel("Local Version: " + localVersion);
			rem.setBounds(10, 10, 180 , 20);
			loc.setBounds(10, 30, 180 , 20);
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
		}else
		{
			System.out.println("There is an update available: " + downloadLink);
		}
	}
	
	/**
	 * Function to request resources from the GithubAPI using OAuth
	 * @param token Generated "Personal Access Token" from https://github.com/settings/tokens
	 * @param repository id e.g. "DanielEnglisch/x0_Library"
	 * @return Returns requested data
	 */
	private String getOAuthedResource(String token, String url) {
		String ret = null;
		url = "https://api.github.com/repos/"+url+"/releases/latest";

		try {
			URL myURL = new URL(url);
			URLConnection connection = myURL.openConnection();
			token = token + ":x-oauth-basic";
			String authString = "Basic " + Base64.getEncoder().encodeToString(token.getBytes());
			connection.setRequestProperty("Authorization", authString);
			InputStream webInputStream = connection.getInputStream();

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
