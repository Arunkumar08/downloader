package com.downloader.exception;

/**
 * @author arun
 *
 */
public class DownloadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DownloadException() {
		super();
	}
	
	public DownloadException(final String message) {
		super(message);
	}

	public DownloadException(Exception ex) {
		super(ex);
	}
}
