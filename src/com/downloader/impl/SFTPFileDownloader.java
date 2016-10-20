package com.downloader.impl;
import java.io.BufferedInputStream;

import com.downloader.AbstractFileDownloader;
import com.downloader.exception.DownloadException;
import com.downloader.helpers.FileDetails;
import com.downloader.helpers.Status;
import com.downloader.util.DownloaderUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

public class SFTPFileDownloader extends AbstractFileDownloader {

   private static int DEFAULT_PORT_SSH = 22;
   public SFTPFileDownloader(FileDetails fileDetails) {
    	   super(fileDetails);
	}

	@Override
	public boolean downloadFile() throws Exception {
        
        Session     session     = null;
        Channel     channel     = null;
        ChannelSftp channelSftp = null;
        boolean fileDownloaded = false;
        
        try {
        	   registerForMonitoring(Status.WAITING_FOR_CONNECTION);
        	
               JSch jsch = new JSch();
               boolean authPassed = false;
               boolean first = true;
               boolean triedAgain = false;
               while(!authPassed && !triedAgain) {
            	   try {
	            	   String[] userPass = new String[2];
	            	   if(first) {
	               			userPass[0] = fileDetails.getUserName();
	               			userPass[1] = fileDetails.getPassword();
	            	   } else {
	            		   System.out.println("Please enter the credentials!!");
	            		   userPass = DownloaderUtil.getCredentialsFromTheUser(fileDetails.getFilePath());
	            		   triedAgain = true;
	            	   }
            		   first = false;
            		   session = jsch.getSession(userPass[0],
            				   					 fileDetails.getHostName(),
            				   					 fileDetails.getPort() == 0 ? DEFAULT_PORT_SSH : fileDetails.getPort());
            		   session.setPassword(userPass[1]);
            		   
            		   java.util.Properties config = new java.util.Properties();
            		   config.put("StrictHostKeyChecking", "no");
            		   session.setConfig(config);
            		   
            		   session.connect();
            		   authPassed = true;
            	   } catch(JSchException ex) {
            		   if (!ex.getMessage().contains("Auth fail")) {            			   
            			   throw new DownloadException(ex);
            		   }
            	   }
               }
               if(!authPassed)
            	   handleAuthenticationError();
               
               channel = session.openChannel("sftp");
               channel.connect();
               channelSftp = (ChannelSftp)channel;
               
               final SftpATTRS attrs = channelSftp.stat(fileDetails.getFilePath());
               
               if(attrs != null) {
	               fileDetails.setFileSize(attrs.getSize());
	               
	               allocateMemory();
	        	   BufferedInputStream bis = new BufferedInputStream(channelSftp.get(fileDetails.getFilePath()));
	        	   fileDownloaded = writeToFile(bis);
               } else {
            	   throw new DownloadException("File does not exist in the remote location.");
               }
        } finally {
        	if(session.isConnected()) {
        		if(channel.isConnected()) {
        			channel.disconnect();
        		}
        		session.disconnect();
        	}
        }
        
        handleSuccess();
		return fileDownloaded;
	}

}
