package com.imooc.security.core.properties;

public class ImageCodeProperties extends SmsCodeProperties {

	// 图形验证码默认配置
	private int width = 67;
	private int height = 23;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ImageCodeProperties() {
		setLength(4);
	}
}
