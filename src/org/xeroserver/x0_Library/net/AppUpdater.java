package org.xeroserver.x0_Library.net;


/**
 * This class provides the ability to automatically check for updates and even update your application.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-08-27
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;

import javax.swing.JOptionPane;

import org.xeroserver.x0_Library.Log.Logger;

public class AppUpdater {
	
	

	private String checkSumFile = "*.php";
	private String downloadFile = ".exe";

	private String name = "CMM_Editor.jar";

	private String localChecksum = "checksum";
	private String remoteChecksum = "checksum";
	private boolean cancelled = false;;
	
	
	// LoggerBlock:
	private Logger l = new Logger("AppUpdater", Logger.SILENT);

	public Logger getLogger() {
		return l;
	}
	// -----------

	
	/**
	 * Constructor
	 * @param checkSumFile HTTP link to the php/html file on your server which prints / generates the checksum of the newest version of your application.
	 * @param donwloadFile HTTP link to the application that should be downloaded once an update is available.
	 * @param defaultName Default name to which the downloaded update will be saved to if it fails to get the current filename.
	 */
	public AppUpdater(String checkSumFile, String donwloadFile, String defaultName) {

			this.checkSumFile = checkSumFile;
			this.downloadFile = donwloadFile;
			name = defaultName;
		
			localChecksum = getLocalChecksum();
			remoteChecksum = getRemoteChecksum();
			
			if(localChecksum.equals("x0_fail")||remoteChecksum.equals("x0_fail"))
			{
				cancelled = true;
				l.fatal("Update checking failed... check HTTP links or internet connection!");
			}

	}
	
	public void checkForUpdate()
	{
		if (isUpdateAvailable()) {
			Object[] options = { "Yes", "No" };
			int n = JOptionPane.showOptionDialog(null, "There is an update available! Do you want to download it?",
					"Updater", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (n == 0) {
				boolean success = downloadUpdate();

				if (success) {
					JOptionPane.showMessageDialog(null, "Successfully applied update! Please restart the program!");

					System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null,
							"Update failed! Check your internet connection and try again later!");
				}
			}

		}
	}
	
	

	private boolean downloadUpdate() {


		try {
			URL website = new URL(downloadFile);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(new File(".", name));
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;

		}

		return true;

	}


	private boolean isUpdateAvailable() {
		
		if(cancelled )
			return false;
		
		return !localChecksum.equals(remoteChecksum);
	}

	private String getRemoteChecksum() {
		String res = "x0_fail";
		HttpURLConnection connection = null;

		try {
			URL url = new URL(checkSumFile);

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				res = inputLine;
			in.close();

		} catch (Exception ex) {
			return "x0_fail";
			
		}

		return res;

	}

	private String getLocalChecksum() {

			
		
		File f = null;
		try {
			f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			name = f.getName();
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
			return "x0_fail";
		}

		return res;
	}
	
	

}
