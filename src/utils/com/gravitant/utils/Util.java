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

import com.gravitant.tests.RunTests;

public class Util{
	static Logger LOGS =  Logger.getLogger(Util.class);
	RunTests runTest = new RunTests();
	public  WebDriver driver;
	public  int currentTestStepRow;
	private  CSVReader testCase = new CSVReader(null);
	private XL_Reader objectId;
	private  Calendar cal = Calendar.getInstance();
	private  SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	private  String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	
	/*public Util(WebDriver driver, String path) {
		this.driver = driver;
		//path = this.path;
	}*/
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
	public boolean verifyTestCaseExists(String testName){
		for (int i=0; i<=runTest.testCasesList.length-1; i++) {
    		String testCaseName = runTest.testCasesList[i].getName(); 
    		testCasePath= testCasesList[i].getAbsolutePath();
           	if(testCaseName.equals(testCaseName)){
           		
           	}
		}
		return false;
	}
	public String getAction(String testName){
		testName  = testName + ".csv";
		File[] testCasesList = runTest.testCasesList;
		for (int i=0; i<=testCasesList.length-1;i++){
			if(testName.equals(testCasesList[i].getName())){
				//System.out.println(testCasesList[i].getName());
				
			}
		}
		return currentTime;
	}
	public int getNoOfSteps(String testcaseName){
		testCase = testcaseName
		return currentTestStepRow;
	}
	public String getPath(){
		String path =  getClass().getClassLoader().getResource(".").getPath().toString();
		return path;
	}
	public  By findObject(String objectLocatorType, String locator) {
		//switch (How.valueOf(objectLocatorType.toUpperCase())) {
		switch (objectLocatorType.toUpperCase()) {
			case "CLASS_NAME":
				return By.className(locator);
			case "CSS":
				return By.cssSelector(locator);
			case "ID":
				return By.id(locator);
			case "LINK_TEXT":
				return By.linkText(locator);
			case "NAME":
				return By.name(locator);
			case "PARTIAL_LINK_TEXT":
				return By.partialLinkText(locator);
			case "TAG_NAME":
				return By.tagName(locator);
			case "XPATH":
				return By.xpath(locator);
			default:
				throw new IllegalArgumentException(
						"Cannot determine how to locate element " + locator);
			}
		}
	
	public  void executeAction(CSV_Reader action) throws Exception{
		int rowCountTestSteps = testCase.getRowCount("Action");
		System.out.println(rowCountTestSteps);
		/*switch (keyword.toLowerCase()){
			case "openbrowser":
				launchBrowser(testData);
				break;
			case "navigate":
				String navigateToUrl = navigateToUrl(testData).toString();
				driver.navigate().to(navigateToUrl);
				String currentUrl = driver.getCurrentUrl().substring(0, (driver.getCurrentUrl().length()-1));
				if(currentUrl.equals(navigateToUrl)){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
				break;
			case "verifypagetitle":
				if(driver.getTitle() !=null && driver.getTitle().equals(testData)){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
				break;
			case "clickbutton":
				clickButton(objectLocatorType, objectLocator);
				break;
			case "typeinput":
				enterText(objectLocatorType, objectLocator, testData);
				break;
			case "clicklink":
				clickLink(objectLocatorType, objectLocator);
				break;
			case "selectlistitem":
				selectListItem(objectLocatorType, objectLocator, testData);
				break;
			case "selectradiobuttonitem":
				selectRadioButtonItem(objectLocatorType, objectLocator, testData);
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
				scrollDown(objectLocatorType, objectLocator);
				break;
			case "savescreenshot":
				captureScreen();
				break;*/
		}
	
	/*public  WebDriver launchBrowser(String browserName) throws URISyntaxException, IOException{
		browserName = browserName.toLowerCase();
		System.out.println(browserName);
		String browserPath = getBrowserPath(browserName);
		switch (browserName){
			case "firefox":
				System.setProperty("webdriver.firefox.bin", browserPath );
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
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
				if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
				LOGS.debug("Launching Internet Explorer");
				break;
			case "chrome":
				File pathToChromeDriver = new File(getClass().getResource("/com/gravitant/utils/chromedriver.exe").toURI());
				System.out.println(pathToChromeDriver.getCanonicalPath());
				System.setProperty("webdriver.chrome.driver", pathToChromeDriver.getAbsolutePath());
				driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
				LOGS.debug("Chrome");
				break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			default:
				System.setProperty("webdriver.firefox.bin", browserPath);
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				if(driver.getCurrentUrl().toString().equals("about:blank")){
					suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
				}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
				break;
			}
			return driver;
	}*/
	
	public   String navigateToUrl(String environment) throws MalformedURLException{
		String Url = null;
		if(environment.equals("QA1")){
			Url = "https://qa1.mygravitant.com";
			LOGS.debug("Navigating to QA1");
		}else if(environment.equals("QA2")){
			Url = "https://qa2.mygravitant.com";
			LOGS.debug("Navigating to QA2");
		}
		return Url;
	}
	public  void verifyPageTitle(String pageTitle){
		
	}
	/*public   void clickButton(String objectLocatorType, String locator){
		driver.findElement(findObject(objectLocatorType, locator)).click();
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}
	public   void clickLink(String objectLocatorType, String locator){
		driver.findElement(findObject(objectLocatorType, locator)).click();
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}
	public  void clickLink(WebDriver driver, WebElement webElement){
		((JavascriptExecutor)driver).executeScript("arguments[0].click()", webElement);
	}
	public   void enterText(String objectLocatorType, String locator, String text){
		driver.findElement(findObject(objectLocatorType, locator)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locator)).getAttribute("value");
		if(enteredText.equals(text)){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "PASS");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "FAIL");}
	}
	public   void selectListItem(String objectLocatorType, String locator, String itemToSelect){
		Select selectBox = new Select(driver.findElement(findObject(objectLocatorType, locator)));
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
	/*public    void selectRadioButtonItem(String objectLocatorType, String locator, String testData){
		WebElement radioButton = null;
		switch(objectLocatorType){
			case "id":
				radioButton = driver.findElement(By.id(locator.trim()));
			case "xpath":
				String locatorwithTestData = locator.replace(locator.substring(16, locator.length()), testData) + "']";
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
	/*public   void scrollDown(String objectLocatorType, String objectLocator){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,500)", "");
		suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "NA");
	}*/
	/*public  void checkErrorMessage(String objectLocatorType, String locator, String testData){
		WebElement errorMessage = driver.findElement(By.xpath(locator.trim()));
		if(errorMessage.getText().isEmpty()){
			suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "No error message");
		}else{suiteXLS.setCellData("Test_Steps", "Result", currentTestStepRow, "Message exists");}
	}*/
	/*public  void appendText(String objectLocatorType, String locator, String text){
		driver.findElement(findObject(objectLocatorType, locator)).sendKeys(text);
		String enteredText = driver.findElement(findObject(objectLocatorType, locator)).getAttribute("value");
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
