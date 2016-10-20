package com.downloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.downloader.helpers.FileDetails;
import com.downloader.helpers.PROTOCOL;
import com.downloader.impl.FTPFileDownloader;
import com.downloader.impl.HTTPFileDownloader;
import com.downloader.impl.SFTPFileDownloader;

public class FileDownloaderFactory {

	private static Map<PROTOCOL, Class<? extends IFileDownloader>> protocolToDownloaderMap = new HashMap<PROTOCOL, Class<? extends IFileDownloader>>();
	
	static {
		/*
		 * Add the downloader classes for each protocol.
		 * 
		 * Note:
		 * Whenever a new protocol is going to be added, this map has to updated
		 * along with the enum PROTOCOL.
		 * */
		protocolToDownloaderMap.put(PROTOCOL.HTTP, HTTPFileDownloader.class);
		protocolToDownloaderMap.put(PROTOCOL.HTTPS, HTTPFileDownloader.class);
		protocolToDownloaderMap.put(PROTOCOL.FTP, FTPFileDownloader.class);
		protocolToDownloaderMap.put(PROTOCOL.SFTP, SFTPFileDownloader.class);
	}
	
	/**
	 * @param protocol
	 * @param filePath
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static IFileDownloader getFileDownloader(final String filePath) {
		IFileDownloader downloader = null;
	
		FileDetails extractedDetails = new FileDetails(filePath);
		
		if(extractedDetails.isValidFile()) {
			
			/* Create the instance. */
			Class<? extends IFileDownloader> classToBeInstantiated = protocolToDownloaderMap.get(extractedDetails.getProtocol());
			try {
				Constructor<? extends IFileDownloader> consForClass = classToBeInstantiated.getConstructor(FileDetails.class);
				downloader = consForClass.newInstance(extractedDetails);
			} catch (Exception ex) {
				System.out.println("Failed creating the downloader");
			}
		}
		return downloader;
	}
}
