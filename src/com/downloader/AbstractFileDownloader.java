package com.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.downloader.exception.DownloadException;
import com.downloader.helpers.DownloadStatus;
import com.downloader.helpers.FileDetails;
import com.downloader.helpers.SharedInfo;
import com.downloader.helpers.Status;
import com.downloader.util.DownloaderUtil;


/**
 * @author arun
 *
 */
public abstract class AbstractFileDownloader implements IFileDownloader {
	
	protected final FileDetails fileDetails;
	protected final DownloadStatus downloadInfo;

	protected SharedInfo sharedInfo = SharedInfo.getInstance();
	
	public AbstractFileDownloader(final FileDetails fileDetails) {
		this.fileDetails = fileDetails;
		downloadInfo = new DownloadStatus();
	}

	
	@Override
	public void run() {
		try {
			downloadFile();
		} catch (Exception e) {
			handleFailure(e);
		}
	}

	/**
	 * This method will read info from input file and writes it to output file.
	 * Takes care of closing the stream as well during the error cases.
	 * 
	 * @param is
	 * @return
	 * @throws DownloadException 
	 * @throws IOException 
	 */
	public boolean writeToFile(final InputStream is)
	    throws IOException,
	           DownloadException {
		
		boolean downloaded = false;
		
		FileOutputStream os = null;
		final File outputFile = getOutputFile();
		
		fileDetails.setOutputFilePath(outputFile.getAbsolutePath());
		
    	registerForMonitoring(Status.DOWNLOADING);

		try {
			final byte[] buffer = new byte[4096];
			os = new FileOutputStream(outputFile);
			int outputRetryCount = 0;
			int read = -1;
			while((read = is.read(buffer)) != -1) {
				try {
					os.write(buffer, 0, read);
				} catch(Exception ex) {
					if(outputRetryCount++ == ouptputStreamRetryCount) {
						throw new DownloadException("Failed to write to the output file. Please retry after sometime.");
					}
				}
				downloadInfo.setDownloadedSize(downloadInfo.getDownloadedSize()
																	+ buffer.length);
			}
			downloaded = true;
		} finally {
			os.close();
			is.close();
		}

		return downloaded;
	}

	/**
	 * This method registers the specified download URL for monitoring.
	 */
	public void registerForMonitoring(final Status status) {
		getDownloadInfo().setStatus(status);
		sharedInfo.updateStatusForURL(fileDetails.getFullPath(),
									  getDownloadInfo());
	}
	
	/**
	 * @return
	 */
	public File getOutputFile() {
		return new File(sharedInfo.getDownloadLocation()
							+ File.separator 
							+ DownloaderUtil.getDestinationFileName(fileDetails,
																	sharedInfo.getDownloadLocation()));
	}
	
	/**
	 * @param file
	 * @throws DownloadException
	 */
	public void deleteFile(final String fileStr) {
		File file = new File(fileStr);
		if(file.exists()) {
			if(file.delete()) {
				/* File deleted.*/
			}
		}
	}
	
	/**
	 * Setting the failure status and message.
	 * 
	 * @param ex
	 * @throws DownloadException
	 */
	protected void handleFailure(Exception ex) {
		
		downloadInfo.setStatus(Status.FAILED);
		downloadInfo.setStatusDescription(ex.getMessage());
		
		if(fileDetails.getOutputFilePath() != null) {
			deleteFile(fileDetails.getOutputFilePath());
		}
		
		/* Update the deleted size in the total size. */
		sharedInfo.updateAvailableSize(downloadInfo.getDownloadedSize());
	}
	
	/**
	 * Setting the success message and status.
	 */
	protected void handleSuccess() {
		downloadInfo.setStatus(Status.DOWNLOAD_FINISHED);
		downloadInfo.setStatusDescription("Downloaded the file in the location: " + fileDetails.getOutputFilePath());
	}
	
	/**
	 * @throws DownloadException
	 */
	protected void handleAuthenticationError() throws DownloadException {
		throw new DownloadException("Invalid credentials. Please try after sometime.");
	}
	
	/**
	 * @return 
	 * @throws DownloadException 
	 */
	public boolean allocateMemory() throws DownloadException {
		if (!sharedInfo.allocateMemoryForIncomingFile(fileDetails.getFileSize())) {
			throw new DownloadException("No free space available in the disk.");
		}
		return true;
	}
	
	/**
	 * @return downloadInfo
	 */
	public DownloadStatus getDownloadInfo() {
		return downloadInfo;
	}


	/**
	 * @return fileDetails
	 */
	public FileDetails getFileDetails() {
		return fileDetails;
	}
}
