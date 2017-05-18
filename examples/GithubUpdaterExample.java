package org.xeroserver.x0library.net;

public class GithubUpdaterExample {

	public static void main(String[] args) {
		GithubUpdater updater = new GithubUpdater("Personal Access Token", "DanielEnglisch/x0Library","1.0");
		
		if(updater.isUpdateAvailable()){
			System.out.println("Update: " + updater.getDownloadLink());
		}
		updater.showUpdateDialog();
	}

}
