package de.httpdownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class DownloadHandler {
	private HttpRequestFactory requestFactory;
	private Thread currentThread;
	private static DownloadHandler instance = null;

	public static DownloadHandler getInstance() {
		if (instance == null) {
			instance = new DownloadHandler();
		}
		return instance;
	}

	private DownloadHandler() {
		requestFactory = new NetHttpTransport().createRequestFactory();

	}

	public synchronized void receiveResponse(HttpResponse response) {
		String filename = "downloadedWebsite.html";
		File file;

		int n = 0;
		do {
			file = new File(filename);
			System.out.println("override " + filename + "? (Y/N)");
			String s = Main.scanner.next();
			if (s.equalsIgnoreCase("n")) {
				filename = "downloadWebsite(" + n++ + ").html";
			} else {
				break;
			}

		} while (file.exists());
		OutputStream output;
		if (response != null) {
			try {
				file.createNewFile();
				output = new FileOutputStream(file);
				response.download(output);
				response.disconnect();
				System.out.println("Website saved in \"" + filename + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no response received");
		}
	}

	public void downloadWebsite(GenericUrl requestUrl) {

		try {
			HttpRequest request = this.requestFactory
					.buildGetRequest(requestUrl);
			if (request != null) {
				this.currentThread = new Thread(new Download(request));
				this.currentThread.start();
			}
		} catch (IOException e) {
			System.err.println("Something went wrong");
		}
	}

	public Thread getCurrentDownloadThread() {
		return this.currentThread;
	}
}
