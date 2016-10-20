package com.downloader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.downloader.services.DownloaderService;
import com.downloader.services.IDownloadService;


public class DownloadManager {

	public static String DEAFULT_PATH = "C:" + File.separator + File.separator + "FileDownloader";
	
	public static void main(String a[]) {
		
		List<String> urls = Arrays.asList(new String[] {
												"http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip",
											    "ftp://arun:arun@192.168.1.3:8888/Device Storage (Motorola XT1033)/Movies/Ajith__10-04-2016_17-47-29.mp4",
											    "sftp://tester:password@10.74.212.55/testfile.txt"
							 										});
		
		IDownloadService service = new DownloaderService();
		service.downloadFiles(urls, null);
	}
}
