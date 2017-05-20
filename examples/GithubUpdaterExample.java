package org.xeroserver.x0library.net;

public class GithubUpdaterExample {

	public static void main(String[] args) {
		
		// Create an updater instance
		GithubUpdater updater = new GithubUpdater("Personal Access Token", "DanielEnglisch/x0Library","1.0");
		
		// If there is an update available
		if(updater.isUpdateAvailable()){
			System.out.println("Update: " + updater.getDownloadLink());
		}
		updater.showUpdateDialog();
	}

}
