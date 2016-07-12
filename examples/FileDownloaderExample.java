package org.xeroserver.x0library.examples;

import java.io.File;

import org.xeroserver.x0library.net.FileDownloader;

public class FileDownloaderExample {
	
	public static void main(String[] args) throws InterruptedException {
		
		FileDownloader fd = new FileDownloader(new File("C:\\Users\\Xer0\\Desktop\\test.x0"), "http://xeroserver.org/tmp/vid.avi"){
			@Override
			public void progressUpdate(double progress) {
				System.out.println("CurrentProgress: " + progress);
			}
		};
		System.out.println("Waiting 2 seconds...");
		Thread.sleep(2000);
		fd.download();
	}

}
