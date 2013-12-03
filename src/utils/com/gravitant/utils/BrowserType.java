package com.gravitant.utils;

public enum BrowserType {
	firefox(0), InternetExplorer(1), Chrome(2), HTMLUNIT(3);
	
	private int value;
	private BrowserType(int value) {
		this.value = value;
	}
	public int intValue(){
		return value;
	}
}
