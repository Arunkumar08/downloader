package com.downloader.impl;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import com.downloader.AbstractFileDownloader;
import com.downloader.exception.DownloadException;
import com.downloader.helpers.FileDetails;
import com.downloader.helpers.Status;
import com.downloader.util.DownloaderUtil;

public class HTTPFileDownloader extends AbstractFileDownloader {
	
	public HTTPFileDownloader(final FileDetails fileDetails) {
		super(fileDetails);
	}

	@Override
	public boolean downloadFile()
		throws Exception {
		
		boolean fileDownloaded = false;
		HttpURLConnection connection = null;
		
		if(fileDetails.getFilePath() != null) {
			
			registerForMonitoring(Status.WAITING_FOR_CONNECTION);
			
			try {
				final URL url = new URL(fileDetails.getFullPath());

				Authenticator.setDefault(new MyAuthenticator(fileDetails.getUserName(), fileDetails.getPassword()));
				connection = (HttpURLConnection) url.openConnection();
				int connRes = connection.getResponseCode();
				
				if(connRes == 401) {
					System.out.println("Please enter the credentials!!");
					String[] userPass = DownloaderUtil.getCredentialsFromTheUser(fileDetails.getFilePath());
					Authenticator.setDefault(new MyAuthenticator(userPass[0], userPass[1]));
					connection = (HttpURLConnection) url.openConnection();
					connRes = connection.getResponseCode();
					if(connRes == 401)
						handleAuthenticationError();
				}
				
				if(connRes == 200) {
					/* Get the estimated input file size. */
					fileDetails.setFileSize(connection.getContentLengthLong());
					allocateMemory();
					
					final InputStream is = connection.getInputStream();
					/* Write to the output file. */
					fileDownloaded = writeToFile(is);
				} else {
	            	throw new DownloadException("Connection failed to the remote server..Retry after sometime.");
	            }
			} finally {
				if(connection != null) {
					connection.disconnect();
				}
			}
		}
	
		handleSuccess();
		return fileDownloaded;
	}

	/**
	 * @author arun
	 *
	 */
	class MyAuthenticator extends Authenticator {
		private String username = "";
	    private String password = "";

	    public MyAuthenticator(final String user, final String pass) {
	    	this.username = user;
	    	this.password = pass;
	    }
	    
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication (username, 
	                						   password.toCharArray());
	    }

	    public void setPasswordAuthentication(String username, String password) {
	        this.username = username;
	        this.password = password;
	    }
	}
}
