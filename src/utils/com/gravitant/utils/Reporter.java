package com.gravitant.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Reporter{
	public void reportEvent(String resultFileName, String testCaseName, String testStep, String object, String expected, String actual) throws IOException{
		if(!expected.equals(actual)){
			FileUtils.writeStringToFile(new File(resultFileName), testCaseName + testStep + object, true);	
		}
	}

}
