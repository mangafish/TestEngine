package com.gravitant.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.time.FastDateFormat;

public class HTMLReportGenerator{
	public static String currentDate = FastDateFormat.getInstance("dd-MMM-yyyy").format(System.currentTimeMillis( ));
	public static String currentResultsFolderName = "C:\\AutomatedTests\\TestResults";
	public static XL_Reader suiteXLS;
	
	public HTMLReportGenerator(String currentDate, String currentResultsFolderName, XL_Reader suiteXLS){
		this.currentDate = currentDate;
		this.currentResultsFolderName = currentResultsFolderName;
		this.suiteXLS = suiteXLS;
	}
	
	public static boolean generateHtmlReport() throws IOException{
		System.out.println("executing");
		
		// create index.html
		String indexHtmlPath=currentResultsFolderName + "\\index.html";
		new File(indexHtmlPath).createNewFile();
		try{
			  FileWriter fstream = new FileWriter(indexHtmlPath);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write("<html><HEAD> <TITLE>Automation Test Results</TITLE></HEAD>"
			  		+ "<body>"
			  		+ "<h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b>Test Execution Results</b></h4>"
			  		+ "<table  border=1 cellspacing=1 cellpadding=1 >"
			  		+ "<tr><h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Details :</u></h4>"
			  		+ "<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Date</b></td>"
			  		+ "<td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>");
			  out.write(currentDate.toString());
			  /*out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Environment</b></td><td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>");
			  out.write(environment);
			  out.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Release</b></td><td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>");
			  out.write(release);*/
			  out.write("</b></td></tr></table><h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>Report: </u></h4><table  border=1 cellspacing=1 cellpadding=1 width=100%><tr><td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>SUITE NAME</b></td><td width=40% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>DESCRIPTION</b></td><td width=10% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>EXECUTION RESULT</b></td></tr>");

			 // out.write("<tr><td width=20% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>TC04</b></td><td width=40% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>TC04</b></td><td width=10% align=center  bgcolor=yellow><FONT COLOR=153E7E FACE=Arial SIZE=2><b>Skip</b></td></tr>");
			 
			  out.write("</table>");
			  out.close();
			  }catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
				  e.printStackTrace();
			  }
		//SendMail.execute(CONFIG.getProperty("report_file_name"));
		return true;
	}
	
	//stand alone runner
		public  static void main(String arg[]) throws IOException{
			System.out.println(HTMLReportGenerator.generateHtmlReport());
		}
}
