package org.xeroserver.x0library.net;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class UpdateNotifier {
	
	public static void checkForUpdate(String id, double currentVersion, String link, boolean display){
		String removeVersion= null, downloadLink= null, remoteId = null;
		
		try {
			@SuppressWarnings("resource")
			String json = new Scanner(new URL(link).openStream(), "UTF-8").useDelimiter("\\A").next();
			removeVersion = getData(json, 2);
			downloadLink = getData(json, 3);
			remoteId = getData(json, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(!id.equals(remoteId))
		{
			System.err.println("Wrong id!");
			return;
		}
		
		double remoteVersionD = 0;

		try {
			remoteVersionD = Double.parseDouble(removeVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(remoteVersionD <= currentVersion){
			System.err.println("Verison error!");
			return;
		}
		
		
	}
	
	public static String getData(String json, int line){
		String parsed = null;
		try{
			line--;
			String[] split = json.split(",");
			parsed = split[line].split(":")[1];
			if(split[line].split(":").length > 2){
				for(int x = 2; x == split[line].split(":").length-1;x++)
					parsed+=split[line].split(":")[x];
			}	
			parsed = parsed.trim();
			parsed = parsed.replaceAll("\\}|\"", "");
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return parsed;
	}
	
	

	
	public static void main(String[] args) {
		checkForUpdate("org.xeroserver.GravitySimulator",1.0d, "https://raw.githubusercontent.com/DanielEnglisch/GravitySimulator/master/update.json", true);
	}
	
}
