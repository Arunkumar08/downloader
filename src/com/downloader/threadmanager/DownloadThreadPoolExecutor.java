package com.downloader.threadmanager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadThreadPoolExecutor extends ThreadPoolExecutor {

	private static DownloadThreadPoolExecutor threadExecutorInst;
	
	private DownloadThreadPoolExecutor(int corePoolSize,
									  int maximumPoolSize,
									  long keepAliveTime,
									  TimeUnit unit,
									  BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public static DownloadThreadPoolExecutor getInstance() {
		if (threadExecutorInst == null) {
			synchronized (DownloadThreadPoolExecutor.class) {
				if (threadExecutorInst == null) {
					threadExecutorInst  = new DownloadThreadPoolExecutor(5, 20, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
				}
			}
		}
		return threadExecutorInst;
	}
	
	@Override
	public void execute(Runnable command) {
		super.execute(command);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
	}
	
}
