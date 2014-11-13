package org.outstudio.wxtest;

public class SiteInfo {

	private String url;
	private String token;

	public SiteInfo() {
	}

	public SiteInfo(String url, String token) {
		this.url = url;
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "SiteInfo [url=" + url + ", token=" + token + "]";
	}

}
