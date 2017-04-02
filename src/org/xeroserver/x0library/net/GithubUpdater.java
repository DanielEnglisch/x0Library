package org.xeroserver.x0library.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For checking if there is an update available and get the dowload link of the latest release of a github repository
 * @author Xer0
 *
 */
public class GithubUpdater {

	private String remoteVersion, localVersion, downloadLink;
	
	/**
	 * Constructor
	 * @param token Generated "Personal Access Token" from https://github.com/settings/tokens
	 * @param url Resource location
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
			downloadLink += spl[i];
		
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
	
	/**
	 * Function to request resources from the GithubAPI using OAuth
	 * @param token Generated "Personal Access Token" from https://github.com/settings/tokens
	 * @param url Resource location
	 * @return Returns requested data
	 */
	private String getOAuthedResource(String token, String url) {
		String ret = null;

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
