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
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import com.gravitant.tests.RunTests;

public class Util extends CSV_Reader{
	static Logger LOGS =  Logger.getLogger(Util.class);
	RunTests runTest = new RunTests();
	public  WebDriver driver;
	public  int currentTestStepRow;
	//public  CSV_Reader testCase = new CSV_Reader();
	private XL_Reader objectId;
	private  Calendar cal = Calendar.getInstance();
	private  SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	private  String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	
	public Util() throws IOException {
		super();
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
		for (int i=0; i<=runTest.testCasesList.length-1; i++) {
    		testCasepath = runTest.testCasesList[i].getPath();
    		//System.out.println(testCasepath);
		}
		//System.out.println(testCasepath);
		return testCasepath;
	}
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
	public  void executeAction(String objectName, String action, String objectLocatorType, String locatorValue, String testData) throws Exception{
		switch(action.toLowerCase()){
			case "clickbutton":
				clickButton(objectLocatorType, locatorValue);
				break;
			/*case "typeinput":
				enterText(objectLocatorType, objectName, testData);
				break;
			case "clicklink":
				clickLink(objectLocatorType, objectName);
				break;
			case "selectlistitem":
				selectListItem(objectLocatorType, objectName, testData);
				break;
			case "selectradiobuttonitem":
				selectRadioButtonItem(objectLocatorType, objectName, testData);
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
				scrollDown(objectLocatorType, objectName);
				break;
			case "savescreenshot":
				captureScreen();
				break;*/
		}
	}
	public void clickButton(String objectLocatorType, String locatorValue){
		driver.findElement(findObject(objectLocatorType, locatorValue)).click();
	}	
	public String getPath(){
		String path =  getClass().getClassLoader().getResource(".").getPath().toString();
		return path;
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
		System.out.println(browserName);
		String browserPath = getBrowserPath(browserName);
		switch (browserName){
			case "firefox":
				System.setProperty("webdriver.firefox.bin", browserPath );
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				/*if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
				LOGS.debug("Launching Firefox");
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
				/*if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
				LOGS.debug("Launching Internet Explorer");
				break;
			case "chrome":
				File pathToChromeDriver = new File(getClass().getResource("/com/gravitant/utils/chromedriver.exe").toURI());
				System.out.println(pathToChromeDriver.getCanonicalPath());
				System.setProperty("webdriver.chrome.driver", pathToChromeDriver.getAbsolutePath());
				driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				/*if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
				LOGS.debug("Chrome");
				break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			default:
				System.setProperty("webdriver.firefox.bin", browserPath);
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				/*if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}*/
				break;
			}
			return driver;
	}
	
	public  String navigateToUrl(String environment) throws MalformedURLException{
		String Url = null;
		if(environment.equals("QA1")){
			Url = "https://qa1.mygravitant.com";
			LOGS.debug("Navigating to QA1");
		}else if(environment.equals("QA2")){
			Url = "https://qa2.mygravitant.com";
			LOGS.debug("Navigating to QA2");
		}
		driver.navigate().to(Url);
		return Url;
	}
	public  void verifyPageTitle(String pageTitle){
		
	}
	
	/*public   void clickLink(String objectLocatorType, String locatorValue){
		driver.findElement(findObject(objectLocatorType, locatorValue)).click();
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}
	public  void clickLink(WebDriver driver, WebElement webElement){
		((JavascriptExecutor)driver).executeScript("arguments[0].click()", webElement);
	}
	public   void enterText(String objectLocatorType, String locatorValue, String text){
		driver.findElement(findObject(objectLocatorType, locatorValue)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
		if(enteredText.equals(text)){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
	}
	public   void selectListItem(String objectLocatorType, String locatorValue, String itemToSelect){
		Select selectBox = new Select(driver.findElement(findObject(objectLocatorType, locatorValue)));
		selectBox.selectByVisibleText(itemToSelect);
		String selected = selectBox.getFirstSelectedOption().getAttribute("selected");
		if(selected.equals("true")){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
		
	}*/
	public   CharSequence getMainWindowHandle(){
		String mainWindowHandle=driver.getWindowHandle();
		return mainWindowHandle;
	}
	/*public   void switchToPopup() throws InterruptedException{
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
         suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}*/
	/*public   void verifyPopupDisplays(){
		if(!driver.getWindowHandle().isEmpty()){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
	}*/
	/*public    void selectRadioButtonItem(String objectLocatorType, String locatorValue, String testData){
		WebElement radioButton = null;
		switch(objectLocatorType){
			case "id":
				radioButton = driver.findElement(By.id(locatorValue.trim()));
			case "xpath":
				String locatorwithTestData = locatorValue.replace(locatorValue.substring(16, locatorValue.length()), testData) + "']";
				//System.out.println(locatorwithTestData);
				radioButton = driver.findElement(By.xpath(locatorwithTestData));
				radioButton.click();
				if(radioButton.isSelected()){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
		}
	}*/
	/*public   void waitForObject(String time) throws Exception{
		int seconds = Integer.parseInt(time);
		System.out.println(seconds);
		Thread.sleep(seconds *1000);
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}*/
	/*public   void scrollDown(String objectLocatorType, String objectName){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,500)", "");
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}*/
	/*public  void checkErrorMessage(String objectLocatorType, String locatorValue, String testData){
		WebElement errorMessage = driver.findElement(By.xpath(locatorValue.trim()));
		if(errorMessage.getText().isEmpty()){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "No error message");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "Message exists");}
	}*/
	/*public  void appendText(String objectLocatorType, String locatorValue, String text){
		driver.findElement(findObject(objectLocatorType, locatorValue)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locatorValue)).getAttribute("value");
		if(enteredText.equals(text)){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
	}*/
	public   void captureScreen() {
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
	
	public void executeTestSteps(String testCase) throws IOException{
		
	}
	
	//stand alone runner
	/*public  static void main(String arg[]) throws IOException{
		//System.out.println(Util.getBrowserPath("firefox").toString());
	}*/
	
}
