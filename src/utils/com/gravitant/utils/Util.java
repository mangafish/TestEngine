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
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import com.gravitant.utils.CSV_Reader;
import com.gravitant.utils.Reporter;
import com.gravitant.pages.LandingPage;
import com.gravitant.pages.LoginPage;
import com.gravitant.tests.RunTests;

public class Util extends CSV_Reader{
	static Logger LOGS =  Logger.getLogger(Util.class);
	RunTests runTest = new RunTests();
	Reporter resultReporter = new Reporter();
	//public  CSV_Reader testCase = new CSV_Reader();
	public  WebDriver driver;
	public  int currentTestStepRow;
	public File testCasesFolder = new File("C:\\AutomatedTests\\Test_Cases\\");
	private XL_Reader objectId;
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
    public int currentTestStepNumber = 0;
    public String currentPageName = null;
    public String currentTestObjectName = null;
    public String objectMapFileName = null;
    public String testDataFileName = null;
    public String testData = null;
    public ArrayList<String> dataTestData =  new ArrayList<String>();;
    public String currenDate = null;
    public String currentTime = null;
    int totalTestNumber = 0;
    int failedStepCounter = 0;
    int failedTestsCounter = 0;
    int screenshotCounter = 0;
    		
	public Util() throws IOException {
		super();
	}
	
	public int getRowCount(List<?> testCaseContent) throws IOException{
		int numberOfRows = 0;
		for (Object object : testCaseContent){
			numberOfRows++;
		 }
		return numberOfRows;
	}
	
	public boolean findIfDataTest(List<?> testCaseContent){
		boolean isDataTest = false;
		String[] row;
		int lineNumber = 0;
		 for (Object object : testCaseContent){
             row = (String[]) object;
             System.out.println(row[2]);
             if(row[4].equals("Begin_DataTest")){
            	 isDataTest = true;
             }
             /*for (int j = 0; j < row.length; j++){
             	lineNumber++;
	                System.out.println("Cell column index: " + j);
	                System.out.println("Cell Value: " + row[j]);
	                System.out.println("-------------");
             	if(row[j].equals("Begin_DataTest")){
             		isDataTest = true;
             		//System.out.println("Data Test Begins at row: " + lineNumber/5);
             		//System.out.println(row[j]);
             	}else{
             		isDataTest = false;
             		//System.out.println("Data Test ends at row: " + lineNumber/5);
             	}
             }*/
	     }
		return isDataTest;
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
	
	/**Method gets the value for the specified property from 
	 * Test_Config.txt
	 * @return String property value
	 * @throws IOException
	 */
	public String getTestConfigProperty(String property) throws IOException{
		BufferedReader readTestConfigFile = new BufferedReader(new FileReader(runTest.testConfigFilePath));
		String currentline = null;
		String propertyValue = null;
	    while((currentline = readTestConfigFile.readLine()) != null) {
	    	if(currentline.contains(property)){
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
		BufferedReader readTestsToRunFile = new BufferedReader(new FileReader(runTest.testsToRun));
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
	public String verifyTestCaseExists(String testName){
		boolean testCaseExists = false;
		String testCasepath = null;
		for (int i=0; i<=runTest.testCasesList.length-1; i++) {
    		String testCaseName = runTest.testCasesList[i].getName();
    		testCasepath = runTest.testCasesList[i].getPath();
           	if(testCaseName.equals(testName + ".csv")){
           		testCaseExists = true;
           		break;
           	}else{
           		testCaseExists = false;
           	}
		}
		if(!testCaseExists){
			LOGS.info("Test Case: " + testName + " does not exist");
		}
		//System.out.println(testCasepath);
		return testCasepath;
	}
	/**
	 * Method returns path to the test case in Tests_Cases folder
	 * @param testName
	 * @return String path to test case.
	 */
	public String getTestCasePath(String testCaseName){
		String testCasepath = null;
		//System.out.println(Arrays.toString(runTest.testCasesList));
		for (int i=0; i<=runTest.testCasesList.length-1; i++) {
			String testName = runTest.testCasesList[i].getName();
			//System.out.println(testName);
			if(testName.equals(testCaseName + ".csv")){
				testCasepath = runTest.testCasesList[i].getPath();
				break;
			}
			
		}
		//System.out.println(testCasepath);
		return testCasepath;
	}
	/**
	 * Method reads the 'Page' column in the test case CSV file and
	 * returns the filename in Object_Map folder where the object's properties are stored.
	 * @param page ame
	 * @return object map filename
	 * @throws IOException 
	 */
	public String getObjectMapFilePath(String pageName) throws IOException{
		String objectMapFilePath = null;
		for (int i=0; i<=runTest.objectMapsList.length-1; i++) {
			String objectMapFileName = runTest.objectMapsList[i].getName();
			//System.out.println(objectMapFileName);
			if(objectMapFileName.equals(pageName + ".csv")){
				objectMapFilePath = runTest.objectMapsList[i].getPath();
				//System.out.println(objectMapFilePath);
				break;
			}
		}
		//System.out.println(objectMapFilePath);
		return objectMapFilePath;
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
		//LOGS.info("--------->Reading Object Map file: " + objectMapFileName);
		CSVReader objectMapFileReader = new CSVReader(new FileReader(objectMapFileName));
        String [] objectRow = null;
        while((objectRow = objectMapFileReader.readNext()) != null) {
        	if(!objectRow[0].equals("Object_Name") && objectRow[0].equals(objectName)){
        		objectInfo = objectRow;
        		break;
        	}
        }
        objectMapFileReader .close();
		return objectInfo;
	}
	
	public String getObjectLocatorType(String[] objectInfo){
		//System.out.println(Arrays.toString(objectInfo));
		String locator_Type = null;
		if(Arrays.toString(objectInfo).equals("")){
			locator_Type = null;
		}else{
			locator_Type = objectInfo[1];
		}
		return locator_Type;
	}
	
	public String getObjectLocatorValue(String[] objectInfo){
		String locator_Value = null;
		if(objectInfo[0] == null){
			locator_Value = null;
		}else{
			locator_Value = objectInfo[2];
		}
		return locator_Value;
	}
	/**
	 * Method reads the 'Page' column in the test case CSV file and
	 * returns the filename in Test_Data folder where the page's test data is stored.
	 * @param page name
	 * @return test data filename
	 * @throws IOException 
	 */
	public String getTestDataFilePath(String pageName) throws IOException{
		String testDataFilePath = null;
		for (int i=0; i<=runTest.testDataFilesList.length-1; i++) {
			String testDataFileName = runTest.testDataFilesList[i].getName();
			//System.out.println(objectMapFileName);
			if(testDataFileName.equals("Data_" +pageName + ".csv")){
				testDataFilePath = runTest.testDataFilesList[i].getPath();
				//System.out.println(testDataFilePath);
				break;
			}
		}
		//System.out.println(objectMapFilePath);
		return testDataFilePath;
	}
	
	public ArrayList<String> getDataTestData(String pageName, String objectName) throws Exception{
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
	public  void executeAction(String pageName, String objectName, String action, String testData) throws Exception{
		objectInfo = this.getObjectInfo(pageName, objectName);
		locator_Type = this.getObjectLocatorType(objectInfo);
		locator_Value = this.getObjectLocatorValue(objectInfo);
		switch(action.toLowerCase()){
			case "clickbutton":
				clickButton(locator_Type, locator_Value, testData);
				break;
			case "typeinput":
				enterText(locator_Type, locator_Value, testData);
				break;
			case "clicklink":
				clickLink(locator_Type, locator_Value);
				break;
			case "selectlistitem":
				selectListItem(locator_Type, locator_Value, testData);
				break;
			case "selectradiobuttonitem":
				selectRadioButtonItem(locator_Type, locator_Value, testData);
				break;
			case "switchtopopup":
				switchToPopup();
				break;
			case "getmainwindowhandle":
				getMainWindowHandle();
				break;
			case "verifypopupdisplays":
				verifyPopupDisplays();
				break;
			case "wait":
				waitForObject(testData);
				break;
			case "scrolldown":
				scrollDown(locator_Type, locator_Value);
				break;
			case "savescreenshot":
				captureScreen(pageName);
				break;
		}
	}
	public void executeDataTest(String pageName, String objectName, String action) throws Exception{
		objectInfo = this.getObjectInfo(pageName, objectName);
		locator_Type = this.getObjectLocatorType(objectInfo);
		locator_Value = this.getObjectLocatorValue(objectInfo);
		dataTestData = this.getDataTestData(pageName, objectName);
		for(int i=0; i<dataTestData.size(); i++){
			testData = dataTestData.get(i);
		}
		/*switch(action.toLowerCase()){
			case "clickbutton":
				clickButton(locator_Type, locator_Value, testData);
				break;
			case "typeinput":
				enterText(locator_Type, locator_Value, testData);
				break;
			case "clicklink":
				clickLink(locator_Type, locator_Value);
				break;
			case "selectlistitem":
				selectListItem(locator_Type, locator_Value, testData);
				break;
			case "selectradiobuttonitem":
				selectRadioButtonItem(locator_Type, locator_Value, testData);
				break;
			case "switchtopopup":
				switchToPopup();
				break;
			case "getmainwindowhandle":
				getMainWindowHandle();
				break;
			case "verifypopupdisplays":
				verifyPopupDisplays();
				break;
			case "wait":
				waitForObject(testData);
				break;
			case "scrolldown":
				scrollDown(locator_Type, locator_Value);
				break;
			case "savescreenshot":
				captureScreen(pageName);
				break;
			case "begin_dataTest":
				executeDataTest();
				break;
		}*/
	}
	public void clickButton(String objectLocatorType, String locatorValue, String buttonText) throws IOException{
		WebElement button = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(button.getText().equals(buttonText) && button.isEnabled()){
			button.click();
		}else{
			failedTestsCounter++;
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Button is not displayed or has changed position"));
			this.captureScreen(this.currentTestName);
		}
	}	
	public   void clickLink(String objectLocatorType, String locatorValue) throws Exception{
		WebElement link = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(link.isDisplayed()){
			link.click();
		}else{
			failedTestsCounter++;
			this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Link is not displayed or has changed position"));
			this.captureScreen(this.currentTestName);
		}
	} 
	public  void clickLink(WebDriver driver, WebElement webElement){
		((JavascriptExecutor)driver).executeScript("arguments[0].click()", webElement);
	}
	public   void enterText(String objectLocatorType, String locatorValue, String text){
		WebElement textBox = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(textBox.isDisplayed()){
			textBox.sendKeys(text);
			String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
			if(!enteredText.trim().equals(text)){
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Text does not match text entered"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			failedTestsCounter++;
			try {
				this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Link is not displayed or has changed position"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.captureScreen(this.currentTestName);
		}
	}
	public void selectListItem(String objectLocatorType, String locatorValue, String optionToSelect){
		Select selectBox = new Select(driver.findElement(findObject(objectLocatorType, locatorValue)));
		if(selectBox.getOptions() != null){
			selectBox.selectByVisibleText(optionToSelect);
			String selectedOption = selectBox.getFirstSelectedOption().getAttribute("selected");
			if(!selectedOption.equals(optionToSelect)){
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Option does not match option selected"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Select box is not displayed or has changed position"));
				}catch (IOException e) {
					e.printStackTrace();
				}
				this.captureScreen(this.currentTestName);
			}
		}
	}
	public    void selectRadioButtonItem(String objectLocatorType, String locatorValue, String testData){
		WebElement radioButton = null;
		switch(objectLocatorType){
			case "id":
				radioButton = driver.findElement(By.id(locatorValue.trim()));
			case "xpath":
				String locatorwithTestData = locatorValue.replace(locatorValue.substring(16, locatorValue.length()), testData) + "']";
				radioButton = driver.findElement(By.xpath(locatorwithTestData));
		}
		if(radioButton.isDisplayed()){
			radioButton.click();
			String selectedRadioButton = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
			if(!selectedRadioButton.trim().equals(testData)){
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Selected radio does not match radio selected"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				failedTestsCounter++;
				try {
					this.writeFailedStepToResultsFile(currentResultFilePath, this.reportEvent(this.currentTestName, this.currentTestStepName, "Radio button is not displayed or has changed position"));
				}catch (IOException e) {
					e.printStackTrace();
				}
				this.captureScreen(this.currentTestName);
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
	public   void verifyPopupDisplays(){
		/*if(!driver.getWindowHandle().isEmpty()){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
	}
	
	public void verifyTextPresent(String objectLocatorType, String locatorValue, String testData){
		WebElement text = null;
		driver.findElement(findObject(objectLocatorType, locatorValue)).getText();
	}
	public   void waitForObject(String time) throws Exception{
		int seconds = Integer.parseInt(time);
		//System.out.println(seconds);
		Thread.sleep(seconds *1000);
	}
	public   void scrollDown(String objectLocatorType, String locatorValue){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,500)", "");
	}
	public  void checkErrorMessage(String objectLocatorType, String locatorValue, String testData){
		WebElement errorMessage = driver.findElement(By.xpath(locatorValue.trim()));
	}
	public  void appendText(String objectLocatorType, String locatorValue, String text){ 
		driver.findElement(findObject(objectLocatorType, locatorValue)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
	}
	public  By findObject(String objectLocatorType, String locatorValue) {
		//switch (How.valueOf(objectLocatorType.toUpperCase())) {
		switch (objectLocatorType.toUpperCase()) {
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
	public  WebDriver launchBrowser(String browserName) throws URISyntaxException, IOException{
		browserName = browserName.toLowerCase();
		String browserPath = getBrowserPath(browserName);
		switch (browserName){
			case "firefox":
				System.setProperty("webdriver.firefox.bin", browserPath );
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************Launching Firefox************");
				break;
			case "ie":
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				File getIEDriver = new File(getClass().getResource("/com/gravitant/utils/IEDriverServer.exe").toURI());
				//URL getIEDriver = getClass().getResource("/com/gravitant/utils/IEDriverServer.exe");
				System.out.println("Browser path: " + getIEDriver.getAbsolutePath());
				System.setProperty("webdriver.ie.driver", getIEDriver.getAbsolutePath());
				driver = new InternetExplorerDriver(capabilities);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************Launching Internet Explorer************");
				break;
			case "chrome":
				File pathToChromeDriver = new File(getClass().getResource("/com/gravitant/utils/chromedriver.exe").toURI());
				System.out.println(pathToChromeDriver.getCanonicalPath());
				System.setProperty("webdriver.chrome.driver", pathToChromeDriver.getAbsolutePath());
				driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				LOGS.info("************ Launching Chrome browser************");
				break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			default:
				System.setProperty("webdriver.firefox.bin", browserPath);
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				break;
			}
			return driver;
	}
	public  String navigateToUrl(String environment) throws MalformedURLException{
		String Url = "https://qa1.mygravitant.com";;
		switch (environment){
			case "QA1":
				Url = Url.replace("qa1", "qa3");
				LOGS.info("************Navigating to QA1************");
			case "QA3":
				Url = Url.replace("qa1", "qa3");
				LOGS.info("************Navigating to QA3************");
			default:
				Url = Url;
				LOGS.info("************Navigating to QA1************");
		}
		
			
		if(environment.equals("QA1")){
			Url = "https://qa1.mygravitant.com";
			LOGS.info("************Navigating to QA1************");
		}else if(environment.equals("QA2")){
			Url = "https://qa2.mygravitant.com";
			LOGS.info("************Navigating to QA2************");
		}
		driver.navigate().to(Url);
		return Url;
	}
	
	public  void verifyPageTitle(String pageTitle){
		
	}
	public String getPath(){
		String path =  getClass().getClassLoader().getResource(".").getPath().toString();
		return path;
	}
	public CharSequence getMainWindowHandle(){
		String mainWindowHandle=driver.getWindowHandle();
		System.out.println("windowhandle:" + mainWindowHandle);
		return mainWindowHandle;
	}
	public void captureScreen(String currentTestName) {
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
	    }
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
            		System.out.println("yes");
            	}
            	/*if(Arrays.toString(filesInFolder).contains(fileName)){
            		filePath =f.getAbsolutePath() + "\\" + fileName;
            	}*/
            }
        }
		return filePath;
	}
	public void login(String userName, String password) throws Exception{
		LandingPage landingPage = PageFactory.initElements(driver, LandingPage.class);
		landingPage.clickSigninButton();
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.enterUsername(userName);
		loginPage.enterPassword(password);
		loginPage.clickLoginButton();
	}
	
	public void login() throws Exception{
		CSVReader testCaseReader = new CSVReader(new FileReader(this.getTestCasePath("Login")));
		String objectMapFileName = null;
		String testDataFileName = null;
		String [] testStepRow = null;
		String testStep = null;
		String testStepObjectName = null;
		while((testStepRow = testCaseReader.readNext()) != null){
			testStep = testStepRow[1];
        	pageName = testStepRow[2];
        	testStepObjectName = testStepRow[3];
        	action = testStepRow[4];
        	objectMapFileName = this.getObjectMapFilePath(pageName);
        	testDataFileName = this.getTestDataFilePath(pageName);
        	String[] objectInfo = null;
			if(!testStep.equals("Step")){
				if(objectMapFileName != null){
					//objectInfo = this.getObjectInfo(pageName, testStepObjectName);
		    		objectInfo = this.getObjectInfo(objectMapFileName, testStepObjectName);
		    		locator_Type = this.getObjectLocatorType(objectInfo);
		    		locator_Value = this.getObjectLocatorValue(objectInfo);
	    			testData = this.getTestData(testDataFileName, testStepObjectName);
	        		if(testDataFileName != null){
	        			testData = this.getTestData(testDataFileName, testStepObjectName);
		        		System.out.println(testData);
	        		}
	    			this.executeAction(pageName, testStepObjectName, action,testData);
		    	}
			}
		}
		LOGS.info("************Logging into QA1************");
		testCaseReader.close();
	}
	public String[] reportEvent(String testCaseName, String testStep, String message) throws IOException{
		failedStepCounter++;
		String[] report = {testCaseName, String.valueOf(failedStepCounter), testStep, message};
		return report;
	}
	/*public void writeFailedStepToResultsFile(String resultFileName, String[] string){
		BufferedWriter bufferedWriter = null;  
		String formattedFailedStep = null;
		formattedFailedStep = String.format("Test Case: " + 
									string[0] +
									'\n' +
									'\t' +
									"Failed Test Step(s): " +
									"\n" +
									string[1] +
									". " + 
									string[2] +
									'\t' +
									'\t' +
									string[3]);
		System.out.println(formattedFailedStep);
		 try {
			bufferedWriter = new BufferedWriter(new FileWriter(resultFileName));
			bufferedWriter.append(formattedFailedStep);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	public void writeFailedStepToResultsFile(String resultFileName, String[] string){
		BufferedWriter writer = null;        
	    try {   
	        writer = new BufferedWriter(new FileWriter(resultFileName));
	        writer.append("Test Case: " + string[0]);
    		writer.newLine();
    		writer.append("\t");
    		writer.append("Failed Test Step(s): ");
    		writer.newLine();
    		writer.append("\t\t");
    		writer.append(string[1]).
    		append(". ").
    		append(string[2]).
    		append(" - ").
    		append(string[3]);
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
	        writer.append(sb);
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
	
	//stand alone runner
	/*public  static void main(String arg[]) throws IOException{
		//System.out.println(Util.getBrowserPath("firefox").toString());
	}*/
	
}
