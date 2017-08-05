package org.xeroserver.x0library.net;

public class GithubUpdaterExample {

	public static void main(String[] args) {
		GithubUpdater updater = new GithubUpdater("DanielEnglisch/GravitySimulator", "1.0");

		if (updater.isUpdateAvailable()) {
			System.out.println("Update: " + updater.getDownloadLink());
		}
		updater.showUpdateDialog();
	}

}
