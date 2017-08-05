package org.xeroserver.x0library;

public final class x0Library{
	
	public static String getVersion(){
		return "0.37";
	}
	
	public static String getChangelog(String version) {
		switch(version) {
		case "0.37": return "removed PersonalAccessToken from GithubUpdater";
			default: return "No changelog available!";
		}
	}
	
}
