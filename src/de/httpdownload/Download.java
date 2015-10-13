package de.httpdownload;

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

public class Download implements Runnable {
	private HttpRequest downloadRequest;
	private HttpResponse response;
	public Boolean complete = false;

	public Download(HttpRequest request) {
		this.downloadRequest = request;
	}

	public HttpResponse getResponse() {
		return this.response;
	}

	@Override
	public void run() {

		try {
			this.response = this.downloadRequest.execute();
			this.complete = true;

		} catch (IOException e) {
			System.err.println("oops");
			
		} finally {
			DownloadHandler.getInstance().receiveResponse(this.response);
		}
		
	}

}
