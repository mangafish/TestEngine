package com.gravitant.tests;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	public String testConfigFilePath =  "C:\\AutomatedTests\\Test_Config\\Test_Config.txt";
	public String testsToRun =  "C:\\AutomatedTests\\Tests_To_Run\\TestsToRun.txt";
	
	public File testCasesFolder = new File("C:\\AutomatedTests\\Test_Cases");
    public File[] testCasesList = testCasesFolder.listFiles();
    public String currentTestCase;
    public String testCasePath;
    
    public File objectMapFolder = new File("C:\\AutomatedTests\\Object_Map");
    public File[] objectMapsList = objectMapFolder.listFiles();
    public String objectMapFilePath;
    
    public File testDataFolder = new File("C:\\AutomatedTests\\Test_Data");
    public File[] testDataFilesList = testDataFolder.listFiles();
    public String testDataFilePath;
    
    public String environment = null;
    public String browserType = null;
    public String username = null;
    public String password = null;
    public String objectName = null;
    public String locator_Type = null;
    public String locator_Value = null;
    public String testDataFileObjectName = null;
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
		Util util = new Util();
		/*environment = util.getTestConfigProperty("environment");
		browserType = util.getTestConfigProperty("browserType");
		username = util.getTestConfigProperty("username");
		password = util.getTestConfigProperty("password");
		util.launchBrowser(browserType);
		util.navigateToUrl(environment);*/
		String objectMapFileName = null;
		String testDataFileName = null;
		//Get test cases to run from TestsToRun.txt
		for(int i=0; i<=util.getTestsToRun().size()-1; i++){
			String currentTest = util.getTestsToRun().get(i);
			if(!util.verifyTestCaseExists(currentTest).equals(null)){
				String currentTestPath = util.getTestCasePath(currentTest);
				//System.out.println(currentTestPath);
				CSVReader testCaseReader = new CSVReader(new FileReader(currentTestPath));
			        String [] testStep = null;
			        String pageName = null;
			        String objectId = null;
			        String action = null;
			        while((testStep = testCaseReader.readNext()) != null) {
			        	pageName = testStep[2];
			        	objectId = testStep[3];
			        	action = testStep[4];
			        	objectMapFileName = util.getObjectMapFilePath(pageName);
			        	testDataFileName = util.getTestDataFilePath(pageName);
			        	//System.out.println(objectMapFileName);
			        	if(objectMapFileName !=null){
			        		CSVReader objectMapFileReader = new CSVReader(new FileReader(objectMapFileName));
					        String [] objectRow = null;
					        while((objectRow = objectMapFileReader.readNext()) != null) {
						        objectName = objectRow[0];
						        locator_Type = objectRow[1];
						        locator_Value = objectRow[2];
						        if(testDataFileName !=null){
						        	CSVReader testDataFileReader = new CSVReader(new FileReader(testDataFileName));
							        String [] testDataRow = null;
							        while((testDataRow = testDataFileReader.readNext()) != null) {
							        	testDataFileObjectName = testDataRow[0];
							        	if(!testDataFileObjectName.equals("Object")){
							        			String testData = testDataRow[1];
								        		System.out.println(testData);
							        	}
							        }
							        testDataFileReader.close();
						        }
					        }
					        objectMapFileReader .close();	
			        	}
			        	/*System.out.println(locator_Type);
			        	util.executeAction(objectId, action, locator_Type, locator_Value);*/
			        }
			        testCaseReader.close();
				/*objectMapFileNames = util.getObjectMapFilenames(currentTestPath);
				objectIds = util.getObjectIds(currentTestPath);
				actions = util.getActions(currentTestPath);
				//System.out.println(actions.toString());
				//System.out.println(objectMapFileName.toString());
				//System.out.println(objectId.toString());
				//Find the object map file in Object_Map folder
				util.findObjectMapFile(objectMapFileNames, objectMapsList);*/
			}
		}
		//System.out.println(Arrays.toString(objectMapsList));
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

	 

