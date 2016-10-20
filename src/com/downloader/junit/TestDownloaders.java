package com.downloader.junit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.downloader.helpers.FileDetails;
import com.downloader.impl.HTTPFileDownloader;

public class TestDownloaders extends TestCase {
	
	HTTPFileDownloader httpDownloader;
	
	@Before
	private void beforeExe() {
		httpDownloader = new HTTPFileDownloader(new FileDetails("http://redrockdigimark.com/apachemirror/commons/net/binaries/commons-net-3.5-bin.zip"));
	}

	/*@Test
	public void testDownloadFileWithoutCredentials() {
		
	}
	
	@Test
	public void testInvalidConnection() {
		
	}*/
	
	public static void main(String a[]) {
		Result result = JUnitCore.runClasses(TestDownloaders.class);
	    System.out.println(result.wasSuccessful());
	}

}
