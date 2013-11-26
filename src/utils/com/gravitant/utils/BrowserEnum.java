package com.gravitant.utils;

public enum BrowserEnum {
	firefox(0), InternetExplorer(1), Chrome(2), Opera(3);
	
	private int value;
	
	private BrowserEnum(int value) {
		this.value = value;
	}
	
	public int intValue(){
		return value;
	}
}
