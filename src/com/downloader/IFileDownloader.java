package com.downloader;


public interface IFileDownloader extends Runnable {
	
	public int ouptputStreamRetryCount = 5;
	
	public boolean downloadFile() throws Exception;

}
