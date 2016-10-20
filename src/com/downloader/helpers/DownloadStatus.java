package com.downloader.helpers;

public class DownloadStatus {

	private Status status;
	private String statusDescription;
	private long downloadedSize = 0;
	private boolean finalOutputGiven = false;
	
	public boolean isOutputGiven() {
		return finalOutputGiven;
	}
	public void setOutputGiven(boolean outputGiven) {
		this.finalOutputGiven = outputGiven;
	}
	
	public long getDownloadedSize() {
		return downloadedSize;
	}
	public void setDownloadedSize(long downloadedSize) {
		this.downloadedSize = downloadedSize;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
}
