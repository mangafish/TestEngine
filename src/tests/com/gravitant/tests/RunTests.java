package com.gravitant.tests;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.gravitant.utils.HTMLReportGenerator;
import com.gravitant.utils.XL_Reader;
import com.gravitant.utils.CSV_Reader;
import com.gravitant.utils.Util;

public class RunTests{
	static Logger LOGS = Logger.getLogger(RunTests.class);
	public String automatedTestsFolder = "C:\\AutomatedTests";
	public String testConfigFilePath =  "C:\\AutomatedTests\\Test_Config\\Test_Config.txt";
	public String testsToRun =  "C:\\AutomatedTests\\Tests_To_Run\\TestsToRun.txt";
	
	public File testCasesFolder = new File("C:\\AutomatedTests\\Test_Cases");
    public File[] testCasesList = testCasesFolder.listFiles();
    
    public File objectMapFolder = new File("C:\\AutomatedTests\\Object_Map");
    public File[] objectMapsList = objectMapFolder.listFiles();
    public String objectMapFilePath;
    public String objectMapFileName = null;
    public String[] objectInfo = null;
    
    public File testDataFolder = new File("C:\\AutomatedTests\\Test_Data");
    public File[] testDataFilesList = testDataFolder.listFiles();
    public String testDataFilePath;
    public String testDataFileName = null;
    public String testDataFileObjectName = null;
    public String testStepObjectName = null;
    public String currentTest = null;
    public String testStepNumber = null;
    public String testStepPageName = null;
    public String testStep = null;
	public int numberofTestSteps = 0;
    
    public String environment = null;
    public String browserType = null;
    public String userName = null;
    public String password = null;
    public String pageName = null;
    
    public String[] testStepRow = null;
    public String action = null;
    public String locator_Type = null;
    public String locator_Value = null;
    public String testData = null;
    public List<String> datatestData =  null;
    
	public String testResultsFolderName;
	public String currentResultsFolderName;
	public String currentResultFileName;
	public String currentResultFilePath;
	public int totalTestsExecuted= 0;
	public int datatestIterations = 0;
	
	public String currentDate = FastDateFormat.getInstance("dd-MMM-yyyy").format(System.currentTimeMillis( ));
	public Calendar cal = Calendar.getInstance();
	public SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	public String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	
	/******************************************************************************************************/
	public static void main(String[] args) throws Exception{
		RunTests test = new RunTests(); 
		test.start();
		Util util = new Util();
		util.generateRandomWord();
 	}
	
	/**
	 * @throws Exception
	 */
	public void start() throws Exception{
		Util util = new Util();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
		/*******Create test results folder ****************/
		testResultsFolderName = util.createFolder(automatedTestsFolder, "Test_Results").toString();//create test results folder
		currentResultsFolderName = util.createFolder(testResultsFolderName, "Results_" + currentDate).toString();//create folder with todays date within above folder
		currentResultFilePath =util.createResultsFile(currentResultsFolderName, currentTime);
		util.setCurrentResultFilePath(currentResultFilePath);
		currentResultFileName = currentResultFilePath;
		util.setCurrentResultFileName(currentResultFileName);
		util.setCurrentDate(currentDate);
		util.setCurrentTime(currentTime);
		/*******Get environment & browser type from test config file & launch browser, navigate to Url****************/
		environment = util.getTestConfigProperty("environment");
		browserType = util.getTestConfigProperty("browserType");
		userName = util.getTestConfigProperty("username");
		password = util.getTestConfigProperty("password");
		util.launchBrowser(browserType);
		util.navigateToUrl(environment);
		util.login(userName, password);
		/*******Get test cases to run from TestsToRun.txt*****************************************************/
        java.util.List<String> testsToRun = util.getTestsToRun();
		for(int i=0; i<=testsToRun.size()-1; i++){
			currentTest = testsToRun.get(i);
			//System.out.println(currentTest);
			/*******If test case exists in Test_Cases folder, read the file and get the page name for each step*******/
			if(!util.verifyTestCaseExists(currentTest).equals(null)){
				util.setCurrentTestName(currentTest);
				util.setTotalTestNumber();
				String currentTestPath = util.getTestCasePath(currentTest);
	        	LOGS.info("-------------------->> STARTING TEST CASE: " + currentTest + " <<--------------------");
				CSVReader testCaseReader = new CSVReader(new FileReader(currentTestPath));
			    List<String[]> testCaseContent = testCaseReader.readAll();
			    /*******Find if this is a data test *******/
			    int dataTestStartRow = 0;
		    	int dataTestEndRow = 0;
			    if(util.findIfDataTest(testCaseContent)){
			    	dataTestStartRow = util.getRowNumber(testCaseContent, "begin_dataTest");
			    	dataTestEndRow = util.getRowNumber(testCaseContent, "end_dataTest");
			    }
			    /*******If this is a data test execute test steps for number of iterations specified*******/
			    if(dataTestStartRow!=0){
			    	for (int j=1; j<=dataTestStartRow; j++){
					    	testStepRow = testCaseContent.get(j);
						    testStepNumber = testStepRow[0];
						    testStepPageName = testStepRow[2];
						    testStepObjectName = testStepRow[3];
						    action = testStepRow[4];
						    testStep = testStepRow[1];
				        	util.setCurrentTestStep(testStep);
						    if(action.equals("begin_dataTest")){
						    	datatestIterations = Integer.parseInt(util.getTestData(testStepPageName, testStepObjectName));
						    	util.setCurrentTestStepNumber(j);
						    }else{
						    	testData = util.getTestData(testStepPageName, testStepObjectName);
							    util.executeAction(testStepPageName, testStepObjectName, action, testData);
						    }
			    	}
			    	/************************************************************/
			    	int currentDatatestIteration = 1;
			    	for(int n = 1; n<=datatestIterations; n++){
			    		if(currentDatatestIteration > datatestIterations){
			    			break;
			    		}else{
			    			LOGS.info("*** Beginning data test iteration: " + currentDatatestIteration + "***");
			    			for (int m=dataTestStartRow +1; m<dataTestEndRow; m++){
							    	testStepRow = testCaseContent.get(m);
								    testStepNumber = testStepRow[0];
								    testStepPageName = testStepRow[2];
								    testStepObjectName = testStepRow[3];
								    action = testStepRow[4];
								    testStep = testStepRow[1];
						        	util.setCurrentTestStep(testStep);
								    testData = util.getTestData(testStepPageName, testStepObjectName, currentDatatestIteration);
								    util.executeAction(testStepPageName, testStepObjectName, action, testData);
			    			}
			    		}
			    		currentDatatestIteration++;
			    	}
			    	/************************************************************/
			    	for (int j=dataTestEndRow+1; j<testCaseContent.size(); j++){
				    	testStepRow = testCaseContent.get(j);
					    testStepNumber = testStepRow[0];
					    testStepPageName = testStepRow[2];
					    testStepObjectName = testStepRow[3];
					    action = testStepRow[4];
					    testStep = testStepRow[1];
			        	util.setCurrentTestStep(testStep);
					    testData = util.getTestData(testStepPageName, testStepObjectName);
					    util.executeAction(testStepPageName, testStepObjectName, action, testData);
					}
			    testCaseReader.close();
			    }
		util.writeTestResultsFile();
			}
		}
	}
}
	 

