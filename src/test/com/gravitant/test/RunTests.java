package com.gravitant.test;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.gravitant.test.RunTests;
import com.gravitant.utils.CSV_Reader;
import com.gravitant.utils.Util;

public class RunTests{
	public String testEnginePath  = null;
	public String testConfigFilePath  = null;
	public String testsToRunFilePath = null;
	public java.util.List<String>  testsToRun = null;
    public String objectMapFilePath;
    public String objectMapFileName = null;
    public String testDataFilePath;
    public String testDataFiles = null;
    public String testDataFileObjectName = null;
    public String testStepObjectName = null;
    public String[] componentAndTestCase = null;
    public String componentName = null;
    public String currentTest = null;
    public String testStepPageName = null;
    public String testStep = null;
    public int testStepNumber = 0;
	public int numberofTestSteps = 0;
    
    public String environment = null;
    public String browserType = null;
    public String browserPath = null;
    public String closeBrowser = null;
    public String automatedTestsFolderPath = null;
    public int globalWaitTime = 0;
    
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
 	}
	
	/**
	 * Method reads the TestsToRun text file and kicks off tests mentioned in the file
	 * @throws Exception
	 * @return null
	 */
	public void start() throws Exception{
		Util util = new Util();
		//*** Get location of Test_Config.txt file ***//
		testEnginePath = util.getTestEnginePath();
		testConfigFilePath  = util.findFile(testEnginePath, "Test_Config.txt");
		testsToRunFilePath = util.findFile(testEnginePath, "TestsToRun.txt");
		//*******Get test configuration properties from Test_Config.txt file ****************//
		environment = util.getTestConfigProperty("environment");
		browserType = util.getTestConfigProperty("browserType");
		automatedTestsFolderPath = util.getTestConfigProperty("Path to Automated Tests");
		globalWaitTime = Integer.parseInt(util.getTestConfigProperty("Wait time(seconds)"));
		closeBrowser = util.getTestConfigProperty("Close Browser at end of test");
		//******* Set log file location ****************//
		System.setProperty("TestEngineLog", automatedTestsFolderPath + "\\TestEngineLog\\TestEngineLog.log");
		//System.out.println(System.getProperty("TestEngineLog"));
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
		Logger LOGS = Logger.getLogger(RunTests.class.getName());
		util.setLogger();
		/*******Create test results folder ****************/
  		testResultsFolderName = util.createFolder(automatedTestsFolderPath, "Test_Results").toString();//create test results folder
		currentResultsFolderName = util.createFolder(testResultsFolderName, "Results_" + currentDate).toString();//create folder with todays date within above folder
		currentResultFilePath =util.createResultsFile(currentResultsFolderName, currentTime);//create test results file
		util.setCurrentResultFilePath(currentResultFilePath);
		currentResultFileName = currentResultFilePath;
		util.setCurrentResultFolderPath(currentResultsFolderName);
		util.setCurrentResultFileName(currentResultFileName);
		util.setCurrentDate(currentDate);
		util.setCurrentTime(currentTime);
		//******* Launch browser and navigate to Url *******//
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
		util.launchBrowser(browserType);
		util.navigateToUrl(environment);
		//*******Get test cases to run from TestsToRun.txt *************//
        testsToRun = util.getTestsToRun();
        //System.out.println(testsToRun);
        util.setTestDirectoryPath(automatedTestsFolderPath);
        util.setGlobalWaitTime(globalWaitTime);
		for(int i=0; i<=testsToRun.size()-1; i++){
			componentName = util.getComponentName(testsToRun.get(i));
			currentTest = util.getTestCaseName(testsToRun.get(i));
			testDataFiles = util.getTestDataFiles(testsToRun.get(i));
			//System.out.println(currentTest);
			/*******If test case exists in Test_Cases folder, read the file and get the page name and action for each object in the test steps*******/
			if(util.verifyTestCaseExists(currentTest) == true){
				util.setCurrentTestName(currentTest);
				util.setTotalTestNumber();
				String currentTestPath = util.getTestCasePath(currentTest);
				//System.out.println("currentTestPath: " + currentTestPath);
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
				    	if(!testStepRow[0].contains("#") && !testStepRow[0].contains("Step")){
				    		testStepNumber = Integer.parseInt(testStepRow[0]);
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
			    	}
			    	/************************************************************/
			    	int currentDatatestIteration = 1;
			    	for(int n = 1; n<=datatestIterations; n++){
			    		if(currentDatatestIteration > datatestIterations){
			    			break;
			    		}else{
			    			LOGS.info("*********** Beginning data test iteration: " + currentDatatestIteration + "************");
			    			for(int m=dataTestStartRow +1; m<dataTestEndRow; m++){
							    	testStepRow = testCaseContent.get(m);
							    	if(!testStepRow[0].contains("#") && !testStepRow[0].contains("Step")){
							    		testStepNumber = Integer.parseInt(testStepRow[0]);
									    testStepPageName = testStepRow[2];
									    testStepObjectName = testStepRow[3];
									    action = testStepRow[4];
									    testStep = testStepRow[1];
							        	util.setCurrentTestStep(testStep);
									    testData = util.getTestData(testStepPageName, testStepObjectName, currentDatatestIteration);
									    util.executeAction(testStepPageName, testStepObjectName, action, testData);
							    	}
			    			}
			    		}
			    		currentDatatestIteration++;
			    	}
			    	/************************************************************/
			    	for (int j=dataTestEndRow+1; j<testCaseContent.size(); j++){
				    	testStepRow = testCaseContent.get(j);
				    	if(!testStepRow[0].contains("#") && !testStepRow[0].contains("Step")){
				    		 testStepNumber = Integer.parseInt(testStepRow[0]);
							    testStepPageName = testStepRow[2];
							    testStepObjectName = testStepRow[3];
							    action = testStepRow[4];
							    testStep = testStepRow[1];
					        	util.setCurrentTestStep(testStep);
							    testData = util.getTestData(testStepPageName, testStepObjectName);
							    util.executeAction(testStepPageName, testStepObjectName, action, testData);
				    	}
					}
			    testCaseReader.close();
			    LOGS.info("*********** End data test ************");
			    }else{
			    	/*******If this is NOT a data test, execute test steps in the test case sequentially*******/
			    	for(int j=1; j<testCaseContent.size(); j++){
				    	testStepRow = testCaseContent.get(j);
				    	if(!testStepRow[0].contains("#") && !testStepRow[0].contains("Step")){
				    		testStepNumber = Integer.parseInt(testStepRow[0]);
						    testStepPageName = testStepRow[2];
						    testStepObjectName = testStepRow[3];
						    action = testStepRow[4];
						    testStep = testStepRow[1];
				        	util.setCurrentTestStep(testStep);
				        	util.setCurrentTestStepNumber(testStepNumber);
						    testData = util.getTestData(testStepPageName, testStepObjectName);
						    util.executeAction(testStepPageName, testStepObjectName, action, testData);
				    	}
					}
			    }
			}else{
				/*******If test case DOES NOT exist in Test_Cases folder, report it in the logs and move on to next test case*******/
				LOGS.info("**** Test case: " + "\"" + currentTest + "\"" +  " does not exist in Test_Cases folder ****");
				continue;
			}
		}
		util.setFailedTestsNumber();
		util.writeTestResultsFile();
		util.closeBrowser(closeBrowser);
	}
}
	 