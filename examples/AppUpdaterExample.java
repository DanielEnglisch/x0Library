package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.net.AppUpdater;

public class AppUpdaterExample {

	public static void main(String[] args) {
		
		
		
		AppUpdater up = new AppUpdater("http://linkToJSONData/data.php");
		if (up.isUpdateAvailable()) {
			up.showUpdateDialog();
		}

		/*
		 * Example for updater.php It has so echo out JSON data containing two
		 * keys: checksum and download!
		 * 
		 * <?php
		 * 
		 * $filename = "MyProgram.jar"; $sha1 = sha1_file($filename);
		 * 
		 * $json= '{ "checksum": "'.$sha1.'", "download":
		 * "http://download.me/MyProgram.jar" }';
		 * 
		 * echo($json); 
		 * 
		 * ?>
		 * 
		 * 
		 */

	}

}
