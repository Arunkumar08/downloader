package com.downloader.junit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.downloader.services.DownloaderService;

public class DownloadServiceTester extends TestCase {

	@Test
	public void testStatusForInvalidURL() {
		DownloaderService service = new DownloaderService();
		List<String> invalidURLs = new ArrayList<String>();
		invalidURLs.add("132fdjskfsfsf");
		invalidURLs.add("/adjhadk/.adjadkj");
        service.downloadFiles(invalidURLs, "C:\\Arun");
        assertEquals(null, service.checkDownloadStatus("132fdjskfsfsf"));
	}
	
	@Test
	public void testStatusForValidURL() {
		DownloaderService service = new DownloaderService();
		List<String> validURLs = new ArrayList<String>();
		validURLs.add("http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip");
        service.downloadFiles(validURLs, "C:\\Arun");
        assertEquals(Boolean.TRUE, service.checkDownloadStatus("http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip"));
	}
	
	@Test
	public void testStatusForValidAndInValidURL() {
		DownloaderService service = new DownloaderService();
		List<String> both = new ArrayList<String>();
		both.add("http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip");
		both.add("invalid");
        service.downloadFiles(both, "C:\\Arun");
        assertEquals(Boolean.TRUE, service.checkDownloadStatus("http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip"));
        assertEquals(null, service.checkDownloadStatus("invalid"));
	}
	
	public static void main(String a[]) {
		 Result result = JUnitCore.runClasses(DownloadServiceTester.class);
	     System.out.println(result.wasSuccessful());
	}
}
