package com.downloader.impl;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.downloader.AbstractFileDownloader;
import com.downloader.exception.DownloadException;
import com.downloader.helpers.FileDetails;
import com.downloader.helpers.Status;
import com.downloader.util.DownloaderUtil;

public class FTPFileDownloader extends AbstractFileDownloader {

	public FTPFileDownloader(FileDetails details) {
		super(details);
	}

	@Override
	public boolean downloadFile()
		throws Exception {
		
		boolean downloaded = false;
		
        FTPClient ftpClient = new FTPClient();
        try {
        	registerForMonitoring(Status.WAITING_FOR_CONNECTION);
            ftpClient.connect(fileDetails.getHostName(), fileDetails.getPort());
            
            ftpClient.login(fileDetails.getUserName(), fileDetails.getPassword());
            int code = ftpClient.getReplyCode();
            
            if(code == 530) {
        		System.out.println("Please enter the credentials!!");
            	String[] userPass = DownloaderUtil.getCredentialsFromTheUser(fileDetails.getFilePath());
            	ftpClient.login(userPass[0], userPass[1]);
            	code = ftpClient.getReplyCode();
            	if(code == 530)
            		handleAuthenticationError();
            }
         
            if(code == 200 || code ==230) {
            	
	            ftpClient.enterLocalPassiveMode();
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	
	            String path = fileDetails.getFilePath().substring(1);
	            FTPFile file = ftpClient.mlistFile(path);
	            if(file == null) {
	            	String dir = fileDetails.getFilePath().substring(0, fileDetails.getFilePath().lastIndexOf("/"));
	            	FTPFile[] files = ftpClient.listFiles(dir);
	            	
	            	for(FTPFile file2 : files) {
	            		if(file2.getName().equals(fileDetails.getFileName())) {
	            			file = file2;
	            			break;
	            		}
	            	}
	            }
	            
	            if(file != null) {
		            
	            	fileDetails.setFileSize(file.getSize());
	            	/*Allocate memory for the file.*/
		            allocateMemory();
		            
			        InputStream inputStream = ftpClient.retrieveFileStream(fileDetails.getFilePath());
			        downloaded = writeToFile(inputStream);
	            } else {
            	   throw new DownloadException("File does not exist in the remote location.");
                }
		        
            } else {
            	throw new DownloadException("Connection failed to the remote server..Retry after sometime.");
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }

        handleSuccess();
        return downloaded;
	}

}
