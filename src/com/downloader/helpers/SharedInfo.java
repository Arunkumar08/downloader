package com.downloader.helpers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SharedInfo {

	private static SharedInfo sharedInfo = null;
	private String downloadLocation = "C:" + File.separator + File.separator + "FileDownloader";
	private long totalAvailableDiskSize = 0L;
	private static Map<String, DownloadStatus> statusMonitor = new HashMap<String, DownloadStatus>();
	
	private SharedInfo() {
		updateAvailableSize(0L);
	}
	
	/**
	 * @return
	 */
	public static SharedInfo getInstance() {
		if(sharedInfo == null) {
			synchronized (SharedInfo.class) {
				if(sharedInfo == null) {
					sharedInfo = new SharedInfo();
				}
			}
		}
		return sharedInfo;
	}
	
	public String getDownloadLocation() {
		return downloadLocation;
	}
	
	public void setDownloadLocation(String downloadLocation) {
		this.downloadLocation = downloadLocation;
		updateAvailableSize(0L);
	}
	
	public synchronized long getTotalAvailableDiskSize() {
		return totalAvailableDiskSize;
	}
	
	/**
	 * Updates the available size in the disk.
	 */
	public synchronized void updateAvailableSize(long sizeToBeAdded) {
		File file = new File(downloadLocation);
		totalAvailableDiskSize = file.getUsableSpace();
	}
	
	/**
	 * @param reqFileSize
	 * @return
	 */
	public synchronized boolean allocateMemoryForIncomingFile(final long reqFileSize) {
		if(totalAvailableDiskSize > reqFileSize) {
			totalAvailableDiskSize = totalAvailableDiskSize - reqFileSize;
			return true;
		}
		return false;
	}
	
	/**
	 * @param url
	 * @param status
	 */
	public synchronized void updateStatusForURL(final String url,
												final DownloadStatus status) {
		statusMonitor.put(url, status);
	}
	
	public DownloadStatus getStatusForURL(final String url) {
		return statusMonitor.get(url);
	}
}
