package com.downloader.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.downloader.FileDownloaderFactory;
import com.downloader.impl.FTPFileDownloader;
import com.downloader.impl.HTTPFileDownloader;
import com.downloader.impl.SFTPFileDownloader;

public class DownloaderFactoryTester {

	@Test
	public void testFactoryWithInvalidURL() {
		assertEquals(null, FileDownloaderFactory.getFileDownloader("invalidURL"));
	}
	
	@Test
	public void testFactoryWithValidURLPatterns() {
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://user:name@10.1.1.23:8080/sample.file").getClass());
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://user:name@10.1.1.23/sample.file").getClass());
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://10.1.1.23:8080/sample.file").getClass());
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://10.1.1.23/sample.file").getClass());
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://www.example.com/sample.file").getClass());
	}
	
	@Test
	public void testFactoryWithUnSupportedProtocol() {
		assertSame(null, FileDownloaderFactory.getFileDownloader("http123://user:name@10.1.1.23:8080/sample.file"));
	}
	
	@Test
	public void testFactoryWithSupportedProtocol() {
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("http://www.example.com/sample.file").getClass());
		assertSame(SFTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("sftp://www.example.com/sample.file").getClass());
		assertSame(HTTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("https://www.example.com/sample.file").getClass());
		assertSame(FTPFileDownloader.class, FileDownloaderFactory.getFileDownloader("ftp://www.example.com/sample.file").getClass());
	}
	
	public static void main(String a[]) {
		 Result result = JUnitCore.runClasses(DownloaderFactoryTester.class);
	     System.out.println(result.wasSuccessful());
	}
}
