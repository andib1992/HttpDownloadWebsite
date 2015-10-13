package de.httpdownload;

import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.api.client.http.GenericUrl;

public class Main {
	public static Scanner scanner;

	public static void main(String[] args) {
		String url = "";
		GenericUrl requestUrl;
		DownloadHandler handle = DownloadHandler.getInstance();

		Main.scanner = new Scanner(new InputStreamReader(System.in));

		System.out.println("enter URL:");

		Boolean correct = false;

		while (!correct) {
			url = Main.scanner.next();
			try {
				requestUrl = new GenericUrl(url);
				correct = true;
				handle.downloadWebsite(requestUrl);
			} catch (IllegalArgumentException e) {
				correct = false;
				System.out.println("Invalid URL. Try again:");
			}
		}
		try {
			handle.getCurrentDownloadThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Main.scanner.close();
		}

	}

}
