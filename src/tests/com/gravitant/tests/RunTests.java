package com.gravitant.tests;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import au.com.bytecode.opencsv.CSVReader;

import com.gravitant.utils.HTMLReportGenerator;
import com.gravitant.utils.XL_Reader;
import com.gravitant.utils.CSV_Reader;
import com.gravitant.utils.Util;

import org.apache.log4j.Logger;

public class RunTests{
	static Logger LOGS = Logger.getLogger(RunTests.class);
	public String testsToRun =  "C:\\AutomatedTests\\Tests_To_Run\\TestsToRun.txt";
	public String currentTestCase;
	public File testCasesFolder = new File("C:\\AutomatedTests\\Test_Cases");
    public File[] testCasesList = testCasesFolder.listFiles();
    public File objectMapFolder = new File("C:\\AutomatedTests\\Object_Map");
    public File[] objectMapsList = objectMapFolder.listFiles();
    public String testCasePath;
    public String objectMapFilePath;
	/*public String currentDate = FastDateFormat.getInstance("dd-MMM-yyyy").format(System.currentTimeMillis( ));
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();*/
	
	public static void main(String[] args) throws Exception{
		RunTests test = new RunTests(); 
		test.start();
	}
	public void start() throws Exception{
		Util getUtil = new Util();
		//Get test cases to run from TestsToRun.txt
		//listOfTests = getUtil.getTestsToRun();
		for(int i=0; i<=getUtil.getTestsToRun().size()-1; i++){
			String currentTest = getUtil.getTestsToRun().get(i);
			//System.out.println(currentTest);
			getUtil.getAction(currentTest);
		}
		/*BufferedReader readTestsToRunFile = new BufferedReader(new FileReader(testsToRun));
        String currentline = null;
        while((currentline = readTestsToRunFile.readLine()) != null) {
        	currentTestCase = currentline;
        	LOGS.info("--------------------------------------------------------");
        	LOGS.info("STARTING TEST CASE: " + currentTestCase);
        	//If the test case in test cases to run list is in the Test Cases folder, execute the test steps for the test case
        	for (int i=0; i<=testCasesList.length-1; i++) {
        		String testCaseName = testCasesList[i].getName();
        		testCasePath= testCasesList[i].getAbsolutePath();
               	if(testCaseName.equals(currentTestCase + ".csv")){
               		CSVReader testCaseReader = new CSVReader(new FileReader(testCasePath));
               		String[] row = null;
               		while((row = testCaseReader.readNext()) != null) {
               		    String pageObject = row[2];
               		    //System.out.println(pageObject + ".csv");
               		    for(int j=0; j<=objectMapsList.length-1; j++){
               		    	String objectMapName = objectMapsList[j].getName();
               		    	//System.out.println(objectMapName);
                    		objectMapFilePath= objectMapsList[j].getAbsolutePath();
                    		if(objectMapName.equals(pageObject + ".csv")){
                    			CSVReader objectMapReader = new CSVReader(new FileReader(objectMapFilePath));
                    			String[] objectRow = null;
                           		while((objectRow = objectMapReader.readNext()) != null) {
                           			String objectId = objectRow[0];
                           			System.out.println(objectId);
                           		}
                           		objectMapReader.close();
                    		}
               		    }
               		}
               		testCaseReader.close();
               		//getUtil.executeActions(testCase);
               	}
            }
        }
        readTestsToRunFile.close();*/
	}
}

	 

