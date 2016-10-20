package com.downloader.services;

import java.io.File;
import java.util.List;

import com.downloader.FileDownloaderFactory;
import com.downloader.IFileDownloader;
import com.downloader.helpers.DownloadStatus;
import com.downloader.helpers.SharedInfo;
import com.downloader.helpers.Status;
import com.downloader.threadmanager.DownloadThreadPoolExecutor;
import com.downloader.util.DownloaderUtil;

public class DownloaderService implements IDownloadService {
	
	public DownloaderService() {
		
	}

	/* (non-Javadoc)
	 * @see com.downloader.services.IDownloadService#downloadFiles(java.util.List)
	 */
	@Override
	public boolean downloadFiles(final List<String> urls,
								 final String prefLocation) {
		
		if(urls == null
				|| urls.isEmpty()) {
			/* Nothing to do. Just return */
			return false;
		}
		String location = prefLocation;
		if(location == null) {
			/* Get the download location. */
			location = DownloaderUtil.getInputFromUser("Enter the preferred download location for the files:");
			File file = new File(location);
			
			/* Until getting the valid location. */
			while(!file.isDirectory()) {
				location = DownloaderUtil.getInputFromUser("Enter valid location:");
				file = new File(location);
			}
		}
		
		getSharedInfo().setDownloadLocation(location);
		
		for(String url : urls) {
			/* Get downloader from the factory based on the URI. */
			IFileDownloader downloader = FileDownloaderFactory.getFileDownloader(url);
			if(downloader == null) {
				DownloaderUtil.displayOutput("URL is invalid. Provide valid one");
			} else {
				DownloadThreadPoolExecutor.getInstance().execute(downloader);
			}
		}
		
		boolean allDone = false;
		
		/**
		 * This logic checks the download status of all the files at
		 * 3 Seconds interval.
		 * 
		 * This happens till download of all the files reaches the termination state.
		 */
		while(!allDone) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				/* Nothing */
			}
			allDone = true;
			for(String url : urls) {
				Boolean result = checkDownloadStatus(url);
				if(result != null && !result) {
					allDone = false;
				}
			}
		}
		
		return allDone;
	}

	/* (non-Javadoc)
	 * @see com.downloader.services.IDownloadService#checkDownloadStatus(java.lang.String)
	 */
	@Override
	public Boolean checkDownloadStatus(final String url) {
		DownloadStatus status = getSharedInfo().getStatusForURL(url);
		boolean finalOutputGiven = false;
		
		if(status == null) {
			/* Handle the situation. */
			return null;
		} else {
			
			finalOutputGiven = status.isOutputGiven();
			if(!finalOutputGiven) {
				String desc = "";
				if(status.getStatus() == Status.DOWNLOADING) {
					desc = "Downloaded " + (status.getDownloadedSize() / 1000) + "Kbs";
				} else if (status.getStatus() == Status.WAITING_FOR_CONNECTION) {
					desc = status.getStatus().getStatus();
				} else {
					finalOutputGiven = true;
					status.setOutputGiven(finalOutputGiven);
					desc = status.getStatusDescription();
				}
				DownloaderUtil.displayOutput("Download Status for URL: " + url + 
											 "\n" + 
											 "Status: " + status.getStatus().status + ", " + desc +
											 "\n");
			}
		}
		return finalOutputGiven;
	}
	
	/**
	 * @return
	 */
	public SharedInfo getSharedInfo() {
		return SharedInfo.getInstance();
	}

}
