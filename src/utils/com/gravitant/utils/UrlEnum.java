package com.gravitant.utils;

public enum UrlEnum {
	QA(0), Stage(1), Prod(2);
	
	private int value;
	
	private UrlEnum(int value) {
		this.value = value;
	}
	
	public int intValue(){
		return value;
	}
}
