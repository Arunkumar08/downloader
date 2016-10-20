package com.downloader.services;

import java.util.List;

public interface IDownloadService {

	/**
	 * This method takes URL, validates and download the file to the specified location.
	 * 
	 * @param urls
	 */
	public boolean downloadFiles(final List<String> urls,
								 final String prefLocation);
	
	/**
	 * By passing the URL, the status of the file download can be checked.
	 * 
	 * @param url
	 */
	public Boolean checkDownloadStatus(final String url);
}
