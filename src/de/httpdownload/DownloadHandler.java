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

	public void receiveResponse(HttpResponse response) {
		File file = new File("downloadedWebsite.html");
		
		int n = 0;
		while (file.exists()) {
			System.out.println("override existing? (Y/N)");
			String s = Main.scanner.next();
			if (s.equalsIgnoreCase("n")) {
				file = new File("downloadedWebsite(" + n++ + ").html");
			} else {
				break;
			}
			
		}
		OutputStream output;
		if (response != null) {
			try {
				file.createNewFile();
				output = new FileOutputStream(file);
				response.download(output);
				response.disconnect();
				System.out.println("");
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
