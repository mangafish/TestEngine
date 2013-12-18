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
	private  Calendar cal = Calendar.getInstance();
	private  SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	private  String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	public String locator_Type = null;
    public String locator_Value = null;
    public String testData = null;
    public String testDataFileObjectName = null;
    public String action = null;
    public String pageName = null;
    public String currentResultFilePath = null;
    public String currentTestName = null;
    public String currentFilePath = null;
    public String currentFileName = null;
    public String currentTestStepName = null;
    public String currentPageName = null;
    public String currentTestObjectName = null;
    int totalTestNumber = 0;
    int counter = 0;
    		
	public Util() throws IOException {
		super();
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
	public String setCurrentPageName(String pageName){
		currentPageName = pageName;
		return currentPageName;
	}
	public String setCurrentTestObjectName(String objectName){
		currentTestObjectName = objectName;
		return currentTestObjectName;
	}
	public int incrementTotalTestNumber(){
		totalTestNumber++;
		return totalTestNumber;
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
	public String getTestCasePath(String testName){
		String testCasepath = null;
		//System.out.println(Arrays.toString(runTest.testCasesList));
		for (int i=0; i<=runTest.testCasesList.length-1; i++) {
			//System.out.println(runTest.testCasesList[i].getName());
			if(runTest.testCasesList[i].getName().contains(testName)){
				testCasepath = runTest.testCasesList[i].getPath();
			}
    		//System.out.println(testCasepath);
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
	
	public String[] getObjectInfo(String objectMapFileName, String testStepObjectName) throws Exception{
		String[] objectInfo = null;
		//LOGS.info("--------->Reading Object Map file: " + objectMapFileName);
		if(!testStepObjectName.equals("")){
			CSVReader objectMapFileReader = new CSVReader(new FileReader(objectMapFileName));
	        String [] objectRow = null;
	        while((objectRow = objectMapFileReader.readNext()) != null) {
	        	if(!objectRow[0].equals("Object_Name") && objectRow[0].equals(testStepObjectName)){
	        		objectInfo = objectRow;
	        		break;
	        	}
	        }
	        objectMapFileReader .close();
		}
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
	
	public String getTestData(String testDataFileName, String testStepObjectName) throws Exception{
		String testDataFileObjectName = null;
		String testData = null;
		//LOGS.info("-------------------->Reading Test Data file: " + testDataFileName);
    	CSVReader testDataFileReader = new CSVReader(new FileReader(testDataFileName));
        String [] testDataRow = null;
        while((testDataRow = testDataFileReader.readNext()) != null){
        	testDataFileObjectName = testDataRow[0];
        	if(!testDataFileObjectName.equals("Object_Name") && testDataFileObjectName.equals(testStepObjectName)){
    			testData = testDataRow[1];
    			break;
        	}
        }
        testDataFileReader.close();
		return testData;
	}
	
	public  void executeAction(String objectName, String action, String objectLocatorType, String locatorValue, String testData) throws Exception{
		switch(action.toLowerCase()){
			case "clickbutton":
				clickButton(objectLocatorType, locatorValue, testData);
				break;
			case "typeinput":
				enterText(objectLocatorType, locatorValue, testData);
				break;
			case "clicklink":
				clickLink(objectLocatorType, locatorValue);
				break;
			case "selectlistitem":
				selectListItem(objectLocatorType, locatorValue, testData);
				break;
			case "selectradiobuttonitem":
				selectRadioButtonItem(objectLocatorType, locatorValue, testData);
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
				scrollDown(objectLocatorType, locatorValue);
				break;
			case "savescreenshot":
				captureScreen();
				break;
		}
	}
	public void clickButton(String objectLocatorType, String locatorValue, String buttonText) throws IOException{
		WebElement button = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(button.getText().equals(buttonText) && button.isEnabled()){
			button.click();
		}else{
			this.reportEvent(this.currentResultFilePath, this.currentTestName, this.currentTestStepName, this.currentTestObjectName, buttonText, button.getText());
		}
	}	
	public   void clickLink(String objectLocatorType, String locatorValue) throws Exception{
		WebElement link = driver.findElement(findObject(objectLocatorType, locatorValue));
		if(link.isDisplayed()){
			link.click();
		}else{
			this.reportEvent(this.currentResultFilePath, this.currentTestName, this.currentTestStepName, this.currentTestObjectName, "true", "false");
		}
	} 
	public  void clickLink(WebDriver driver, WebElement webElement){
		((JavascriptExecutor)driver).executeScript("arguments[0].click()", webElement);
	}
	public   void enterText(String objectLocatorType, String locatorValue, String text){
		driver.findElement(findObject(objectLocatorType, locatorValue)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
	}
	public   void selectListItem(String objectLocatorType, String locatorValue, String itemToSelect){
		Select selectBox = new Select(driver.findElement(findObject(objectLocatorType, locatorValue)));
		selectBox.selectByVisibleText(itemToSelect);
		String selected = selectBox.getFirstSelectedOption().getAttribute("selected");
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
	public    void selectRadioButtonItem(String objectLocatorType, String locatorValue, String testData){
		WebElement radioButton = null;
		switch(objectLocatorType){
			case "id":
				radioButton = driver.findElement(By.id(locatorValue.trim()));
			case "xpath":
				String locatorwithTestData = locatorValue.replace(locatorValue.substring(16, locatorValue.length()), testData) + "']";
				//System.out.println(locatorwithTestData);
				radioButton = driver.findElement(By.xpath(locatorwithTestData));
				radioButton.click();
				/*if(radioButton.isSelected()){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
		}
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
		String Url = null;
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
	/**
	 * Method reads the Object column in the test case CSV file and
	 * returns the object on which an action is to be performed
	 * @param testCaseName
	 * @return the Object
	 * @throws IOException 
	 */
	/*public ArrayList<String> getObjectIds(String testCaseName) throws IOException{
		ArrayList<String> objectId = testCase.getColumnData(testCaseName, "Object");
		return objectId;
	}*/
	/**
	 * Method reads the Action column in the test case CSV file and
	 * returns the action to be performed
	 * @param testCaseName
	 * @return the Action
	 * @throws IOException 
	 */
	/*public ArrayList<String> getActions(String testCaseName) throws IOException{
		ArrayList<String> actions = testCase.getColumnData(testCaseName, "Action");
		return actions;
	}*/
	/**
	 * Method reads the 'Page' column in the test case CSV file and
	 * returns the filename in Object_Map folder where the object's properties are stored.
	 * @param testCaseName
	 * @return object map filename
	 * @throws IOException 
	 */
	/*public ArrayList<String> getObjectMapFilenames(String testCaseName) throws IOException{
		ArrayList<String> objectMapFilename = testCase.getColumnData(testCaseName, "Page");
		return objectMapFilename;
	}*/
	public CharSequence getMainWindowHandle(){
		String mainWindowHandle=driver.getWindowHandle();
		System.out.println("windowhandle:" + mainWindowHandle);
		return mainWindowHandle;
	}
	
	public void captureScreen() {
		String path;
	    try {
	        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        path = "./target/screenshots/" + source.getName();
	        String screenshotFolderName = createFolder("C:\\AutomatedTests", "Screenshots").toString();
	        FileUtils.copyFile(source, new File(screenshotFolderName + "\\Screenshot_" + currentTime + ".png")); 
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
	public  String getBrowserPath(String browserName){
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
		    		objectInfo = this.getObjectInfo(objectMapFileName, testStepObjectName);
		    		locator_Type = this.getObjectLocatorType(objectInfo);
		    		locator_Value = this.getObjectLocatorValue(objectInfo);
	    			testData = this.getTestData(testDataFileName, testStepObjectName);
	        		if(testDataFileName != null){
	        			testData = this.getTestData(testDataFileName, testStepObjectName);
		        		System.out.println(testData);
	        		}
	    			this.executeAction(testDataFileObjectName, action, locator_Type, locator_Value, testData);
		    	}
			}
		}
		LOGS.info("************Logging into QA1************");
		testCaseReader.close();
	}
	public void reportEvent(String resultFileName, String testCaseName, String testStep, String object, String expected, String actual) throws IOException{
		counter++;
		if(!expected.equals(actual)){
			FileUtils.writeStringToFile(new File(resultFileName), counter + ". " + testCaseName + "Test Step: " + testStep + object, true);	
			FileUtils.writeStringToFile(new File(resultFileName), "Test Step: " + testStep, true);
			FileUtils.writeStringToFile(new File(resultFileName), "Expected: " + expected, true);
			FileUtils.writeStringToFile(new File(resultFileName), "Actual: " + expected, true);
		}
	}
	public void appendToResultsFile(String resultFileName, String string){
		BufferedWriter bufferedWriter = null;        
	    try {           
	        bufferedWriter = new BufferedWriter(new FileWriter(resultFileName));            
	        bufferedWriter.append(string);
	        bufferedWriter.newLine();
	    } catch (FileNotFoundException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (bufferedWriter != null) {
	                bufferedWriter.flush();
	                bufferedWriter.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	public void appendToResultsFile(String resultFileName, int number) {
		BufferedWriter bufferedWriter = null;        
	    try {           
	        bufferedWriter = new BufferedWriter(new FileWriter(resultFileName));            
	        bufferedWriter.append(String.valueOf(number));
	        bufferedWriter.newLine();
	    } catch (FileNotFoundException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (bufferedWriter != null) {
	                bufferedWriter.flush();
	                bufferedWriter.close();
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
