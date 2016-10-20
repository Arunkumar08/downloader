package com.downloader.helpers;

public enum Status {

	FAILED("Failed"),
	DOWNLOADING("Downloading"),
	DOWNLOAD_FINISHED("Downloaded to the destination location"),
	WAITING_FOR_CONNECTION("Waiting for the connection!!!")
	;
	
	public String status;
	
	public String getStatus() {
		return status;
	}
	
	private Status(final String status) {
		this.status = status;
	}
}
