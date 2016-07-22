package org.xeroserver.x0library.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

interface DLProgressListener {
	public void progressUpdate(double progress);
}

public class FileDownloader implements DLProgressListener {

	private File replaceFile = null;
	private String link = null;

	public boolean download() {

		if (!replaceFile.exists())
			try {
				replaceFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		if (!replaceFile.canWrite()) {
			System.out.println("No write permission for " + replaceFile.getAbsolutePath());
			return false;

		}

		FileOutputStream fos;
		ReadableByteChannel rbc;
		URL url;

		try {
			url = new URL(link);
			int ln = contentLength(url);
			if (ln == -1)
				return false;
			rbc = new ReadableByteChannelWrapper(Channels.newChannel(url.openStream()), ln, this);
			fos = new FileOutputStream(replaceFile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();

			if (replaceFile.length() == ln) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public FileDownloader(File replaceFile, String link) {
		this.replaceFile = replaceFile;
		this.link = link;
	}

	@Override
	public void progressUpdate(double progress) {
		System.out.println("Progress: " + progress);
	}

	private int contentLength(URL url) {
		HttpURLConnection connection;
		int contentLength = -1;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			contentLength = connection.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contentLength;
	}

}

class ReadableByteChannelWrapper implements ReadableByteChannel {

	private DLProgressListener listener;
	private long expectedSize;
	private ReadableByteChannel rbc;
	private long readSoFar;

	ReadableByteChannelWrapper(ReadableByteChannel rbc, long expectedSize, DLProgressListener listener) {
		this.listener = listener;
		this.expectedSize = expectedSize;
		this.rbc = rbc;
	}

	public void close() throws IOException {
		rbc.close();
	}

	public boolean isOpen() {
		return rbc.isOpen();
	}

	public int read(ByteBuffer bb) throws IOException {
		int n;
		double progress;

		if ((n = rbc.read(bb)) > 0) {
			readSoFar += n;
			progress = expectedSize > 0 ? (double) readSoFar / (double) expectedSize * 100.0 : -1.0;
			listener.progressUpdate(progress);
		}

		return n;
	}

}
