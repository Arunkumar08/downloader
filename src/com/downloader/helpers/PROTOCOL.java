package com.downloader.helpers;


public enum PROTOCOL {

	HTTP("http"),
	HTTPS("https"),
	FTP("ftp"),
	SFTP("sftp"),
	;
	
	public String name;
	
	private PROTOCOL(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * @param input
	 * @return
	 */
	public static PROTOCOL getProtocolFromString(final String input) {
		
		/*Default*/
		PROTOCOL protocol = null;
		
		for(PROTOCOL pr : PROTOCOL.values()) {
			if(pr.name.equals(input)) {
				protocol = pr;
			}
		}
		return protocol;
	}
}
