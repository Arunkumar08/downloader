package com.downloader.helpers;

public class FileDetails {

	private String fullPath;
	private String fileName;
	private String filePath;
	private PROTOCOL protocol;
	private String userName = "DEFAULT";
	private String password = "DEFAULT";
	private int port;
	private String hostName;
	private boolean validFile = true;
	private long fileSize = 0;
	private String outputFilePath;
	
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public boolean isValidFile() {
		return validFile;
	}

	public void setValidFile(boolean validFile) {
		this.validFile = validFile;
	}

	/**
	 * @param filePath2
	 */
	public FileDetails(final String filePath2) {
		

		if(filePath2 == null || filePath2.equals("")) {
			setValidFile(false);
		} else {
			
			setFullPath(filePath2);
			
			/* Split it for protocol */
			String[] splitted1 = filePath2.split("//");
			
			if(splitted1.length == 0
					|| splitted1.length == 1) {
				/* This is invalid URL */
				setValidFile(false);
			}
			
			if(splitted1.length == 2) {
				String protocol = splitted1[0].split(":")[0];
				setProtocol(PROTOCOL.getProtocolFromString(protocol));
				
				if(getProtocol() == null)
					setValidFile(false);
				
				String pathAndAddress = splitted1[1];
				
				String address = pathAndAddress.substring(0, pathAndAddress.indexOf("/"));
				
				String[] addressSplit = address.split("@");
				
				if(addressSplit.length == 2) {
					/* We have some username and password. */
					String credential = addressSplit[0];
					String[] userpass = credential.split(":");
					setUserName(userpass[0]);
					if(userpass.length == 2) {
						setPassword(userpass[1]);
					}
					
					String hostName = addressSplit[1];
					String[] hostport = hostName.split(":");
					setHostName(hostport[0]);
					if(hostport.length == 2) {
						setPort(Integer.parseInt(hostport[1]));
					}
				} else {
					/*Check for the port in it. */
					String hostName = addressSplit[0];
					String[] hostport = hostName.split(":");
					setHostName(hostport[0]);
					if(hostport.length == 2) {
						setPort(Integer.parseInt(hostport[1]));
					}
				}
				
				String path = pathAndAddress.substring((pathAndAddress.indexOf("/")), pathAndAddress.length());
				
				setFilePath(path);
				
				String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
				setFileName(fileName);
			}
			
		}
	}
	
	/**
	 * Default Constructor
	 */
	public FileDetails() {
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public PROTOCOL getProtocol() {
		return protocol;
	}

	public void setProtocol(PROTOCOL protocol) {
		this.protocol = protocol;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
}
