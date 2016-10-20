package com.downloader.util;

import java.io.File;
import java.util.Scanner;

import com.downloader.helpers.FileDetails;


public class DownloaderUtil {

	private static Scanner scanner = new Scanner(System.in);
	public boolean sizeCheck(final Long sourceFileSize) {
		return false;
		
	}
	
	public FileDetails parseURL(final String url) {
		FileDetails fileDetails = new FileDetails(url);
		return fileDetails;	
	}
	
	/**
	 * @param fileName
	 * @return
	 */
	public synchronized static String[] getCredentialsFromTheUser(String fileName) {
		String[] credetials = new String[2];
		System.out.println("Provide credentials to access the file: " + fileName);
		credetials[0] = getInputFromUser("Enter username:");
		credetials[1] = getInputFromUser("Enter password:");
		return credetials;
	}

	/**
	 * @param command
	 * @return
	 */
	public synchronized static String getInputFromUser(String command) {
		String output = "";
		System.out.println(command);
		output = scanner.nextLine();
		return output;
	}
	
	/**
	 * @param fileDetails
	 * @param destinationDirectory
	 * @return
	 */
	public static String getDestinationFileName(final FileDetails fileDetails,
												final String destinationDirectory) {
		int counter = 0;
		String destFileName = String.format("[%s - %s] %s",
											fileDetails.getHostName(),
											counter,
											fileDetails.getFileName()); 
		File destFile = new File(destinationDirectory, destFileName);
		while(destFile.exists()) {
			destFileName = String.format("[%s - %s] %s",
										fileDetails.getHostName(),
										++counter,
										fileDetails.getFileName()); 
			destFile = new File(destinationDirectory, destFileName);
		}
		return destFileName;
	}
	
	public static synchronized void displayOutput(String outputMessage) {
		System.out.println(outputMessage);
	}
}
