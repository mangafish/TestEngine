package com.gravitant.utils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class BrowserPath {
	public static String getBrowserPath(String browserName){
		String browserlocation = "";
		File root = new File("c:\\");
	    String fileName = browserName + ".exe";
	    try {
	        boolean recursive = true;
	        Collection<File> files = FileUtils.listFiles(root, null, recursive);

	        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
	            File file = (File) iterator.next();
	            if (file.getName().equals(fileName))
	            	browserlocation = file.getAbsolutePath();
	                System.out.println(browserlocation);
	                break;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return fileName;
	}
}
