package com.gravitant.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import au.com.bytecode.opencsv.CSVReader;

import com.gravitant.test.RunTests;
import com.gravitant.utils.CSV_Reader;

public class Util extends CSV_Reader{
	static Logger LOGS =  Logger.getLogger(Util.class);
	RunTests runTest = new RunTests();
	public  WebDriver driver;
	public String filePath = null;
	public String testDirectoryPath  = null;
	public String testConfigFilePath  = null;
	public String testsToRunFilePath  = null;
	public String fileNameToSearch = null;
	public String objectMapFilePath = null;
	public String testDataFilePath = null;
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	public String locator_Type = null;
    public String locator_Value = null;
    public String testDataFileObjectName = null;
    public String[] objectInfo = null;
    public String action = null;
    public String pageName = null;
    public String currentResultFilePath = null;
    public String currentTestName = null;
    public String currentFilePath = null;
    public String currentFileName = null;
    public String currentTestStepName = null;
    public String currentPageName = null;
    public String currentTestObjectName = null;
    public String objectMapFileName = null;
    public String testDataFileName = null;
    public String testData = null;
    public String currenDate = null;
    public String currentTime = null;
    int currentTestStepNumber = 0;
    int currentTestStepRow;
    int totalTestNumber = 0;
    int failedStepCounter = 0;
    int failedTestsCounter = 0;
    int screenshotCounter = 0;
    		
	public Util() throws IOException {
		super();
	}
	/**Method gets the location of the exe jar file
	 * @return String - path to the automated tests directory 
	 */
	public String getTestDirectoryPath(){
		File jarFile = new File(RunTests.class.getProtectionDomain().getCodeSource().getLocation().getPath());   
		String jarFilePath = jarFile.getAbsolutePath();
		String jarRootDirectoryPath = jarFilePath.replace(jarFile.getName(), "");
		String testConfigDirectory = jarRootDirectoryPath.substring(0, jarRootDirectoryPath.indexOf("GravitantAutomatedTests"));
		this.setTestDirectoryPath(testConfigDirectory);
		return testConfigDirectory;
	}
	/**Method sets the path to the automated tests directory 
	 * @return null
	 */
	public void setTestDirectoryPath(String path){
		testDirectoryPath  = path;
	}
	public String findFile(String parentDirectory, String fileToFind){
		fileToFind = fileToFind.toLowerCase();
		String filePath = null;
		File root = new File(parentDirectory);
		setFileNameToSearch(fileToFind);
		if(root.isDirectory()) {
			filePath = search(root);
		} else {
		    System.out.println(root.getAbsoluteFile() + " is NOT a directory");
		}
		return filePath;
	}
	private String search(File file){
		String fileToSearch = getFileNameToSearch().trim();
		if(file.isDirectory()){
			for(File temp : file.listFiles()){
				if(temp.isDirectory()){
					search(temp);
				}else{
					if(temp.getName().trim().toLowerCase().equals(fileToSearch)) {	
						this.filePath = temp.getAbsolutePath();
				    }
				}
			}
		}
		return filePath;
	}
	 public void setFileNameToSearch(String fileNameToSearch) {
			this.fileNameToSearch = fileNameToSearch;
	}
	public String getFileNameToSearch() {
		return fileNameToSearch.toLowerCase();
	}
	
	public String findDirectory(String directoryToFind){
		String directoryPath = null;
		File root = new File(this.testDirectoryPath);
        File[] list = root.listFiles();
        for (File f : list) {
            if (f.isDirectory() && f!=null){
            	File[] filesInFolder = f.listFiles();
            	if(Arrays.toString(filesInFolder).contains(directoryToFind)){
            		directoryPath =f.getAbsolutePath() + "\\" + directoryToFind;
            	}
            }
        }
		return directoryPath;
	}
	
	public void setTestConfigFilePath(String testConfigPath){
		testConfigFilePath  = testConfigPath;
	}
	/**Method gets the value for the specified test property from 
	 * Test_Config.txt
	 * @return String property value
	 * @throws IOException
	 */
	public String getTestConfigProperty(String property) throws IOException{
		String testConfigFilePath = this.findFile(this.testDirectoryPath, "Test_Config.txt");
		BufferedReader readTestConfigFile = new BufferedReader(new FileReader(testConfigFilePath));
		String currentline = null;
		String propertyValue = null;
	    while((currentline = readTestConfigFile.readLine()) != null) {
	    	if(currentline.toLowerCase().contains(property.toLowerCase())){
	    		String[] split = currentline.split("=");
	    		propertyValue = split[1];
	    		break;
	    	}
	    }
		//System.out.println(propertyValue);
	    readTestConfigFile.close();
		return propertyValue;
	}
	/**Method gets the list of all tests specified in
	 * Tests_To_Run.txt
	 * @return ArrayList of tests to run
	 * @throws IOException
	 */
	public List<String> getTestsToRun() throws IOException{
		String testsToRunPath = this.findFile(this.testDirectoryPath, "TestsToRun.txt");
		BufferedReader readTestsToRunFile = new BufferedReader(new FileReader(testsToRunPath));
		String currentline = null;
		List<String> testsToRun = new ArrayList<>();
	    while((currentline = readTestsToRunFile.readLine()) != null) {
	    	//System.out.println(currentline);
	    	testsToRun.add(currentline);
	    }
	    readTestsToRunFile.close();
		return testsToRun;
	}
	/**
	 * Method verifies the test case listed in Tests_To_Run.txt exists in
	 * the Test_Cases folder.
	 * @param testName
	 * @return boolean
	 */
	public boolean verifyTestCaseExists(String testName){
		boolean testCaseExists = false;
		String testCasePath = this.findFile(this.testDirectoryPath + "\\Test_Cases", testName + ".csv");
		if(!testCasePath.equals(null) && testCasePath.contains(testName)){
       		testCaseExists = true;
       	}else{
       		testCaseExists = false;
       	}
		return testCaseExists;
	}
	public int getRowCount(List<?> testCaseContent) throws IOException{
		int numberOfRows = 0;
		for (Object object : testCaseContent){
			numberOfRows++;
		 }
		return numberOfRows;
	}
	
	public String setCurrentTestName(String currentTest){
		currentTestName = currentTest;
		return currentTestName;
	}
	public String setCurrentResultFilePath(String filePath){
		currentResultFilePath = filePath;
		return currentResultFilePath;
	}
	public String setCurrentResultFileName(String fileName){
		currentFileName = fileName;
		return currentFileName;
	}
	public String setCurrentTestStep(String testStepName){
		currentTestStepName = testStepName;
		return currentTestStepName;
	}
	public int setCurrentTestStepNumber(int stepNumber){
		currentTestStepNumber = stepNumber;
		return currentTestStepNumber;
	}
	public String setCurrentPageName(String pageName){
		currentPageName = pageName;
		return currentPageName;
	}
	public String setCurrentTestObjectName(String objectName){
		currentTestObjectName = objectName;
		return currentTestObjectName;
	}
	public void setTotalTestNumber(){
		totalTestNumber++;
	}
	public void setCurrentDate(String currentDate){
		this.currenDate = currentDate;
	}
	public void setCurrentTime(String currentTime){
		this.currentTime = currentTime;
	}
	
	/**
	 * Method returns path to the test case in Tests_Cases folder
	 * @param testName
	 * @return String path to test case.
	 */
	public String getTestCasePath(String testCaseName){
		String testCasePath = this.findFile(this.testDirectoryPath + "\\Test_Cases", testCaseName + ".csv");
		return testCasePath;
	}
	/**
	 * Method reads the 'Page' column in the test case CSV file and
	 * returns the filename in Object_Map folder where the object's properties are stored.
	 * @param page ame
	 * @return object map filename
	 * @throws IOException 
	 */
	public String getObjectMapFilePath(String pageName) throws IOException{
		objectMapFilePath = this.findFile(this.testDirectoryPath + "\\Object_Map", pageName + ".csv");
		return objectMapFilePath;
		
		/*String objectMapFilePath = null;
		for (int i=0; i<=runTest.objectMapsList.length-1; i++) {
			String objectMapFileName = runTest.objectMapsList[i].getName();
			//System.out.println(objectMapFileName);
			if(objectMapFileName.equals(pageName + ".csv")){
				objectMapFilePath = runTest.objectMapsList[i].getPath();
				//System.out.println(objectMapFilePath);
				break;
			}
		}
		System.out.println(objectMapFilePath);
		return objectMapFilePath;*/
	}
	
	public String findObjectMapFile(ArrayList<String> objectMapFileNames, File[] objectMapsList ){
		String objectMapFileName = null;
		for(int j=0; j<objectMapFileNames.size(); j++){
			objectMapFileName = objectMapFileNames.get(j) + ".csv";
			//System.out.println(objectMapFileName);
			for(int k=0;k<objectMapsList.length;k++){
				//System.out.println(objectMapsList[k].getName());
				if(objectMapsList[k].getName().equals(objectMapFileName)){
					System.out.println(objectMapFileName);
					break;
				}
			}
		}
		return objectMapFileName;
	}
	
	public String[] getObjectInfo(String pageName, String objectName) throws Exception{
		objectMapFileName = this.getObjectMapFilePath(pageName);
		String[] objectInfo = null;
		CSVReader objectMapFileReader = new CSVReader(new FileReader(objectMapFileName));
        String [] objectRow = null;
        while((objectRow = objectMapFileReader.readNext()) != null) {
        	if(!objectRow[0].equals("Object_Name") && objectRow[0].equals(objectName)){
        		objectInfo = objectRow;
        		break;
        	}else{
        		objectInfo = null;
        	}
        }
        objectMapFileReader .close();
		return objectInfo;
	}
	
	public String getObjectLocatorType(String[] objectInfo){
		//System.out.println(Arrays.toString(objectInfo));
		String locator_Type = null;
		if(Arrays.toString(objectInfo).equals("") || Arrays.toString(objectInfo).contentEquals("null")){
			locator_Type = null;
		}else{
			locator_Type = objectInfo[1];
		}
		return locator_Type;
	}
	
	public String getObjectLocatorValue(String[] objectInfo){
		String locator_Value = null;
		if(Arrays.toString(objectInfo).equals("") || Arrays.toString(objectInfo).contentEquals("null")){
			locator_Value = null;
		}else{
			locator_Value = objectInfo[2];
		}
		return locator_Value;
	}
	
	public boolean findIfDataTest(List<String[]> testCaseContent){
		boolean isDataTest = false;
		String[] testStepRow = null;
		for(int k=0;  k<testCaseContent.size(); k++){
    		testStepRow = testCaseContent.get(k);
    		if(testStepRow[4].equals("begin_dataTest")){
    			isDataTest = true;
    			break;
    		}
    	}
		return isDataTest;
	}
	public int getRowNumber(List<String[]> testCaseContent, String value){
		int rowNumber =0;
		String[] testStepRow = null;
		for(int k=0;  k<testCaseContent.size(); k++){
    		testStepRow = testCaseContent.get(k);
    		if(testStepRow[4].equals(value)){
    			rowNumber = k;
    			break;
    		}
    	}
		return rowNumber;
	}
	/**
	 * Method reads the 'Page' column in the test case CSV file and
	 * returns the filename in Test_Data folder where the page's test data is stored.
	 * @param page name
	 * @return test data file path
	 * @throws IOException 
	 */
	public String getTestDataFilePath(String pageName) throws IOException{
		testDataFilePath = this.findFile(this.testDirectoryPath + "\\Test_Data", "Data_" + pageName + ".csv");
		return testDataFilePath;
	}
	public void setTestDataFilePath(String path){
		testDirectoryPath  = path;
	}
	public String getTestData(String pageName, String objectName) throws Exception{
		testDataFileName = this.getTestDataFilePath(pageName);
    	CSVReader testDataFileReader = new CSVReader(new FileReader(testDataFileName));
        String[] testDataRow = null;
        while((testDataRow = testDataFileReader.readNext()) != null){
        	testDataFileObjectName = testDataRow[0];
        	if(!testDataFileObjectName.equals("Object_Name") && testDataFileObjectName.equals(objectName)){
       			testData = testDataRow[1];
    			break;
        	}
        }
        testDataFileReader.close();
		return testData;
	}
	
	public String getTestData(String pageName, String objectName, int dataTestIteration) throws Exception{
		testDataFileName = this.getTestDataFilePath(pageName);
    	CSVReader testDataFileReader = new CSVReader(new FileReader(testDataFileName));
        String[] testDataRow = null;
        while((testDataRow = testDataFileReader.readNext()) != null){
        	//System.out.println("Data test item number: " + testDataRow[dataTestIteration]);
        	testDataFileObjectName = testDataRow[0];
        	if(!testDataFileObjectName.equals("Object_Name") && testDataFileObjectName.equals(objectName) && !testDataRow[dataTestIteration].equals("null")){
       			testData = testDataRow[dataTestIteration];
    			break;
        	}
        }
        testDataFileReader.close();
		return testData;
	}
	public ArrayList<String> getDataTestData(String pageName, String objectName) throws Exception{
		ArrayList<String> dataTestData =  new ArrayList<String>();
		System.out.println(dataTestData);
		testDataFileName = this.getTestDataFilePath(pageName);
    	CSVReader testDataFileReader = new CSVReader(new FileReader(testDataFileName));
        String[] testDataRow = null;
        while((testDataRow = testDataFileReader.readNext()) != null){
        	testDataFileObjectName = testDataRow[0];
        	if(!testDataFileObjectName.equals("Object_Name") && testDataFileObjectName.equals(objectName)){
        		for(int i=1;i<testDataRow.length;i++){
        			System.out.println(testDataRow[i]);
        			dataTestData.add(testDataRow[i]); 
        		}
    			break;
        	}
        }
        testDataFileReader.close();
		return dataTestData;
	}
	public  void executeAction(String pageName, String objectName, String action, String testData) throws Exception{
		objectInfo = this.getObjectInfo(pageName, objectName);
		locator_Type = this.getObjectLocatorType(objectInfo);
		locator_Value = this.getObjectLocatorValue(objectInfo);
		switch(action.toLowerCase()){
			case "clickbutton":
				LOGS.info("> Clicking button: " + objectName + " on " + pageName);
				clickButton(locator_Type, locator_Value, testData);
				break;
			case "typeinput":
				LOGS.info("> Entering text in: " + objectName + " on " + pageName);
				enterText(locator_Type, locator_Value, testData);
				break;
			case "clicklink":
				LOGS.info("> Clicking link: " + objectName + " on " + pageName);
				clickLink(locator_Type, locator_Value);
				break;
			case "selectlistitem":
				LOGS.info("> Selecting combo item: " + "\"" + testData + "\"" + " in " + objectName);
				selectListBoxItem(locator_Type, locator_Value, testData);
				break;
			case "selectradiobuttonitem":
				LOGS.info("> Selecting radio item: " + testData + " in " + objectName);
				selectRadioButtonItem(locator_Type, locator_Value, testData);
				break;
			case "switchtopopup":
				LOGS.info("> Switching to popup" );
				switchToPopup();
				break;
			case "getmainwindowhandle":
				LOGS.info("> Getting main window handle");
				getMainWindowHandle();
				break;
			case "verifytextpresent":
				LOGS.info("> Verifying text displays: " + testData);
				verifyTextPresent(locator_Type, locator_Value, testData);
				break;
			case "verifytextnotpresent":
				LOGS.info("> Verifying text DOES NOT display: " + testData);
				verifyTextNotPresent(locator_Type, locator_Value, testData);
				break;
			case "wait":
				LOGS.info("> Waiting for: " + testData + " seconds");
				waitForObject(testData);
				break;
			case "scrolldown":
				LOGS.info("> Scrolling down");
				scrollDown(locator_Type, locator_Value);
				break;
			case "savescreenshot":
				LOGS.info("> Capturing screenshot: " + pageName);
				captureScreen(pageName);
				break;
			case "getCellData":
				LOGS.info("> Getting cell data: " + pageName);
				getCellData(locator_Type, locator_Value);
				break;
			case "clickmenuitem":
				LOGS.info("> Clicking menu item: " + objectName + " on " + pageName);
				clickMenuItem(locator_Type, locator_Value, testData);
				break;
			case "verifypagetitle":
				LOGS.info("> Verifying page title on: " + pageName);
				verifyPageTitle(testData);
				break;
		}
	}
	public String getCellData(String objectLocatorType, String locatorValue){
		String cellData = null;
		WebElement table = driver.findElement(findObject(objectLocatorType, locatorValue));
		List<WebElement> rows  = table.findElements(By.tagName("tr")); //find all tags with 'tr' (rows)
		System.out.println("Total Rows: " + rows.size()); //print number of rows
		for (int rowNum=1; rowNum<rows.size(); rowNum++) {
			List<WebElement> columns  = table.findElements(By.tagName("td")); //find all tags with 'td' (columns)
			System.out.println("Total Columns: " + columns.size()); //print number of columns
			 for (int colNum=0; colNum<columns.size(); colNum++){
				System.out.print(columns.get(colNum).getText() + " -- "); //print cell data
			}
			System.out.println();
		}
		return cellData;
	}
	public void clickButton(String objectLocatorType, String locatorValue, String buttonText) throws IOException{
		WebElement button = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(button.isEnabled()){
			button.click();
		}else{
			failedTestsCounter++;
			LOGS.info("Button: " + "\"" + buttonText + "\"" + " is not displayed or has changed position");
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Button is not displayed or has changed position"));
			this.captureScreen(this.currentTestName);
		}
	}	
	public   void clickLink(String objectLocatorType, String locatorValue) throws Exception{
		WebElement link = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(link.isDisplayed()){
			link.click();
		}else{
			failedTestsCounter++;
			LOGS.info("Link is not displayed or has changed position");
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Link is not displayed or has changed position"));
			this.captureScreen(this.currentTestName);
		}
	} 
	public  void clickMenuItem(String objectLocatorType, String locatorValue, String testData) throws IOException{
		WebElement menuItem = driver.findElement(findObject(objectLocatorType, locatorValue));
		((JavascriptExecutor)this.driver).executeScript("arguments[0].click()", menuItem);
	}
	public   void enterText(String objectLocatorType, String locatorValue, String text) throws IOException{
		WebElement textBox = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(textBox.isDisplayed()){
			textBox.sendKeys(text);
			String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
			if(!enteredText.trim().equals(text)){
				failedTestsCounter++;
				LOGS.info("Text does not match text entered");
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Text does not match text entered"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			failedTestsCounter++;
			LOGS.info("Text box is not displayed or has changed position");
			try {
				this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Text box is not displayed or has changed position"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.captureScreen(this.currentTestName);
		}
	}
	public void selectListBoxItem(String objectLocatorType, String locatorValue, String optionToSelect) throws IOException{
		Select selectBox = new Select(driver.findElement(findObject(objectLocatorType, locatorValue)));
		if(selectBox.getOptions() != null){
			selectBox.selectByVisibleText(optionToSelect);
			String selectedOption = selectBox.getFirstSelectedOption().getAttribute("selected");
			//System.out.println("selected option: " + selectedOption);
			if(!selectedOption.equals("true")){
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Option does not match option selected"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			LOGS.info("Select box is not displayed or has changed position");
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Select box is not displayed or has changed position"));
		}
	}
	public    void selectRadioButtonItem(String objectLocatorType, String locatorValue, String testData){
		WebElement radioButton = null;
		String locatorwithTestData = null;
		switch(objectLocatorType){
			case "id":
				radioButton = driver.findElement(By.id(locatorValue.trim()));
			case "xpath":
				locatorwithTestData = locatorValue.replace(locatorValue.substring(16, locatorValue.length()), testData) + "']";
				radioButton = driver.findElement(By.xpath(locatorwithTestData));
		}
		if(radioButton.isDisplayed()){
			radioButton.click();
			String selectedRadioButton = driver.findElement(By.xpath(locatorwithTestData)).getAttribute("value");
			//String selectedRadioButton = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
			if(!selectedRadioButton.trim().equals(testData)){
				failedTestsCounter++;
				LOGS.info("Selected radio button does not match item selected");
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Selected radio button does not match item selected"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public   void switchToPopup() throws InterruptedException{
		 Set<String> windowHandles = driver.getWindowHandles();
		 Iterator<String> windows = windowHandles.iterator();
	    while(windows.hasNext())
	    {
	         String popupHandle=windows.next().toString();
	         if(!popupHandle.contains(getMainWindowHandle()))
	         {
	             driver.switchTo().window(popupHandle);
	         }
	    }
	}
	
	public void verifyTextPresent(String objectLocatorType, String locatorValue, String testData){
		try{
			String textToVerify = driver.findElement(findObject(objectLocatorType, locatorValue)).getText();
			if(!textToVerify.equals(testData)){
				failedTestsCounter++;
				LOGS.info("Text displayed: " +  "\"" + textToVerify + "\""  + " does not match expected: " + testData);
				this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Text displayed: " +  "\"" + textToVerify + "\""  + " does not match expected: " + "\"" + testData + "\""));
				this.captureScreen(this.currentTestName);
			}else if(textToVerify.isEmpty()){
				failedTestsCounter++;
				LOGS.info("Expected text: " +  "\"" + textToVerify + "\""  + " is not displayed");
				this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Expected text: " +  "\"" + textToVerify + "\""  + " is not displayed"));
				this.captureScreen(this.currentTestName);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void verifyTextNotPresent(String objectLocatorType, String locatorValue, String testData) throws IOException {
		try{
			String text  = driver.findElement(findObject(objectLocatorType, locatorValue)).getText();
			if(text.equals(testData)){
				this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Text IS displayed: " +  "\"" + text + "\""));
				this.captureScreen(this.currentTestName);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public   void waitForObject(String time) throws Exception{
		int seconds = Integer.parseInt(time);
		Thread.sleep(seconds *1000);
	}
	public   void scrollDown(String objectLocatorType, String locatorValue){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,500)", "");
	}
	public  void verifyPageTitle(String pageTitle) throws IOException{
		String currentWindowTitle = driver.getTitle().toString();
		if(!currentWindowTitle.isEmpty() && !currentWindowTitle.equals(pageTitle)){
			failedTestsCounter++;
			LOGS.info("Current page title: " +  "\"" + currentWindowTitle + "\"" + " does not match expected title: " + "\"" + pageTitle + "\"");
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Current page title: " +  "\"" + currentWindowTitle + "\"" + " does not match expected title: " + "\"" + pageTitle + "\""));
			this.captureScreen(this.currentTestName);
		}
	}
	public CharSequence getMainWindowHandle(){
		String mainWindowHandle=driver.getWindowHandle();
		return mainWindowHandle;
	}
	public void captureScreen(String currentTestName) throws IOException {
		screenshotCounter++;
		String path;
	    try {
	        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        path = "./target/screenshots/" + source.getName();
	        String screenshotFolderName = createFolder("C:\\AutomatedTests", "Screenshots").toString();
	        String currentTestScreenshotFolderName = createFolder(screenshotFolderName, "\\" + currenDate).toString();
	        FileUtils.copyFile(source, new File(currentTestScreenshotFolderName + "\\" + currentTestName + "_" + screenshotCounter + ".png")); 
	    }
	    catch(IOException e) {
	        path = "Failed to capture screenshot: " + e.getMessage();
	        LOGS.info("Failed to capture screenshot");
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepNumber, this.currentTestStepName, "Failed to capture screenshot"));
			this.captureScreen(this.currentTestName);
	    }
	}
	public  void appendText(String objectLocatorType, String locatorValue, String text){ 
		driver.findElement(findObject(objectLocatorType, locatorValue)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
	}
	public  By findObject(String objectLocatorType, String locatorValue){
		switch (objectLocatorType.toUpperCase()){
			case "CLASS_NAME":
				return By.className(locatorValue);
			case "CSS":
				return By.cssSelector(locatorValue);
			case "ID":
				return By.id(locatorValue);
			case "LINK_TEXT":
				return By.linkText(locatorValue);
			case "NAME":
				return By.name(locatorValue);
			case "PARTIAL_LINK_TEXT":
				return By.partialLinkText(locatorValue);
			case "TAG_NAME":
				return By.tagName(locatorValue);
			case "XPATH":
				return By.xpath(locatorValue);
			default:
				throw new IllegalArgumentException(
						"Cannot determine how to locate element " + locatorValue);
		}
	}
	public  WebDriver launchBrowser(String browserName, String browserPath) throws URISyntaxException, IOException{
		browserName = browserName.toLowerCase();
		//String browserPath = getBrowserPath(browserName);
		switch (browserName){
			case "firefox":
				browserPath = browserPath + "\\" + "firefox.exe";
				System.setProperty("webdriver.firefox.bin", browserPath);
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************Launching Firefox ************");
				break;
			case "chrome":
				browserPath = browserPath + "\\" + "chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", browserPath);
				driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************ Launching Chrome browser ************");
				break;                     
			case "ie":
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				browserPath = browserPath + "\\" + "IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", browserPath);
				driver = new InternetExplorerDriver(capabilities);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************ Launching Internet Explorer ************");
				break;
			case "headless":
				driver = new HtmlUnitDriver();
				((HtmlUnitDriver)driver).setJavascriptEnabled(true);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************ Launching headless test ************");
				break;                     
			}
			return driver;
	}
	public  String navigateToUrl(String environment) throws MalformedURLException{
		String Url = "https://qa1.mygravitant.com";;
		environment = environment.trim().toLowerCase();
		Url = Url.replace("qa1", environment);
		driver.navigate().to(Url);
		return Url;
	}
	public  void closeBrowser(){
		driver.quit();
	}
	public  String createFolder(String path, String folderName){
		new File(path + "\\" + folderName).mkdir();
		return path + "\\" + folderName;
	}
	public String createTextFile(String parentFolderName, String currentTime) throws Exception{
		String filePath = parentFolderName + "\\" + "Results_" + currentTime + ".txt";
		//System.out.println(filePath);
		File textFile = new File(filePath);
		textFile.createNewFile();
		return filePath;
	}
	public String createResultsFile(String parentFolderName, String currentTime){
		String resultsFilePath = null;
		try {
			resultsFilePath = this.createTextFile(parentFolderName, currentTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultsFilePath;
	}
	public  void copyFile(String filePath, String newFilePath, String newFileName){
		InputStream inStream = null;
		OutputStream outStream = null;
		newFileName = newFilePath + "\\" + newFileName;
    	try{
    	    File file =new File(filePath);
    	    File newFile =new File(newFileName);
    	    inStream = new FileInputStream(file);
    	    outStream = new FileOutputStream(newFile);
    	    byte[] buffer = new byte[1024];
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = inStream.read(buffer)) > 0){
    	    	outStream.write(buffer, 0, length);
    	    }
    	    inStream.close();
    	    outStream.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
	}
	public void mouseOver(WebDriver driver, WebElement webElement) {
        String code = "var fireOnThis = arguments[0];"
                    + "var evObj = document.createEvent('MouseEvents');"
                    + "evObj.initEvent( 'mouseover', true, true );"
                    + "fireOnThis.dispatchEvent(evObj);";
        ((JavascriptExecutor) driver).executeScript(code, webElement);
    }
	public void  generateRandomWord (){	   	            
        Random myRandom = new Random();
        for (int i = 0; i < 4; i++) {         
           String Word = "" + 
                (char) (myRandom.nextInt(26) + 'A') +
                (char) (myRandom.nextInt(26) + 'a') +
                (char) (myRandom.nextInt(26) + 'a') +
                (char) (myRandom.nextInt(26) + 'a');               
        }
	}
	public String getBrowserPath(String browserName){
		String browserPath = null;
		browserName = browserName.toLowerCase() + ".exe";
		File root = new File("c:\\");
        File[] list = root.listFiles();
        for (File f : list) {
            if (f.isDirectory() && f!=null){
            	File[] filesInFolder = f.listFiles();
            	if(Arrays.toString(filesInFolder).contains(browserName)){
            		browserPath =f.getAbsolutePath() + "\\" + browserName;
            	}
            }
        }
		return browserPath;
	}
	public String getFilePath(String folderName, String fileName){
		String filePath = null;
		File folder = new File(folderName);
        File[] filesList = folder.listFiles();
        for (File f : filesList) {
            if (f.isDirectory() && f!=null){
            	File[] filesInFolder = f.listFiles();
            	if(Arrays.asList(filesInFolder).contains(fileName)){
            		//System.out.println("yes");
            	}
            }
        }
		return filePath;
	}
	
	public String[] reportEvent(String testCaseName, int testStepNumber, String testStep, String message) throws IOException{
		failedStepCounter++;
		String[] report = {testCaseName, String.valueOf(testStepNumber), testStep, message};
		return report;
	}

	public void writeFailedStepToResultsFile(String resultFilePath, String[] resultMessage){
		//System.out.println(Arrays.toString(resultMessage));
		BufferedWriter writer = null;        
	    try{
	        writer = new BufferedWriter(new FileWriter(resultFilePath));
	        writer.append("Test Case: " + resultMessage[0]);
    		writer.newLine();
    		writer.append("\t");
    		writer.append("Failed Test Step(s):");
    		writer.newLine();
    		writer.append("\t\t");
    		writer.append("Step No.");
    		writer.append(resultMessage[1]).
    		append(": ").
    		append(resultMessage[2]);
    		writer.newLine();
    		writer.append("\t\t\t").
    		append(" ACTUAL RESULT: ").
    		append(resultMessage[3]);
	    } catch (FileNotFoundException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (writer != null) {
	                writer.flush();
	                writer.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	public int getFailedTestNumber(){
		return failedTestsCounter;
	}
	public void writeTestResultsFile(){
		BufferedReader reader;
		StringBuilder sb = new StringBuilder();
		String string1 = null;
		String string2 = null;
		String string3 = null;
		String string4 = null;
		try {
			reader = new BufferedReader(new FileReader(this.currentResultFilePath));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
				    sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("String length:" + sb.length());
		
		if(!(sb.length()<1)){
			int position1 = sb.indexOf("Failed Test Step(s):");
			int position2 = sb.indexOf("1.");
			string1 = sb.substring(0, position1);
			string2 = sb.substring(position1, position2);
			string3 = sb.substring(position2, sb.length());
			
			BufferedWriter writer = null;        
		    try {           
		        writer = new BufferedWriter(new FileWriter(this.currentResultFilePath));            
		        writer.append("Tests Executed: " + this.totalTestNumber);
		        writer.newLine();
		        writer.append("Tests Passed: " + (this.totalTestNumber - this.failedTestsCounter));
		        writer.newLine();
		        writer.append("Tests Failed: " + this.failedTestsCounter);
		        writer.newLine();
		        writer.newLine();
		        writer.append(string1);
		        writer.newLine();
	    		writer.append("\t");
	    		writer.append(string2);
	    		writer.newLine();
	    		writer.append("\t\t");
	    		writer.append(string3);
		    } catch (FileNotFoundException ex) {
		        ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    } finally {
		        try {
		            if (writer != null) {
		                writer.flush();
		                writer.close();
		            }
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		}else{
			BufferedWriter writer = null;        
		    try {           
		        writer = new BufferedWriter(new FileWriter(this.currentResultFilePath));            
		        writer.append("Tests Executed: " + this.totalTestNumber);
		        writer.newLine();
		        writer.append("Tests Passed: " + (this.totalTestNumber - this.failedTestsCounter));
		        writer.newLine();
		        writer.append("Tests Failed: " + this.failedTestsCounter);
		    } catch (FileNotFoundException ex) {
		        ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    } finally {
		        try {
		            if (writer != null) {
		                writer.flush();
		                writer.close();
		            }
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		}
		
		
	}
	
	//stand alone runner
	/*public  static void main(String arg[]) throws IOException{
		//System.out.println(Util.getBrowserPath("firefox").toString());
	}*/
	
}
