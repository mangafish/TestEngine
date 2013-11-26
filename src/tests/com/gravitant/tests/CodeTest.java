package com.gravitant.tests;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.*;

//import com.gravitant.pages.LoginPage;

@Test
public class CodeTest {
	public WebDriver driver;	
	//private static Logger log = LoggerFactory.getLogger(LoginPage.class);
	@Test
	public void test() throws InterruptedException, IOException {
		String browserPath = getBrowserPath("c:\\","firefox");
		System.setProperty("webdriver.firefox.bin",browserPath);
		driver=new FirefoxDriver();
		driver.get("https://qa1.mygravitant.com");
		System.out.println(driver.getTitle());
		driver.close();
	}
	
	@Test
	public void test1() throws InterruptedException, IOException {
		//String browserPath = getBrowserPath("c:\\","firefox");
		File dir = new File("c:\\");

		System.out.println("Getting all files in " + dir.getCanonicalPath() + " including those in subdirectories");
		List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : files) {
			//System.out.println("file: " + file.getCanonicalPath());
			if(file.getCanonicalPath().toString().toLowerCase().contains("firefox.exe")){
				System.out.println("file found: " + file.getCanonicalPath());
				System.setProperty("webdriver.firefox.bin",file.getCanonicalPath());
				driver=new FirefoxDriver();
				driver.get("https://qa1.mygravitant.com");
				System.out.println(driver.getTitle());
				driver.close();
			}
			break;
		}
	}
	public static String getBrowserPath(String directoryName,String browserName) throws IOException{
		String browserlocation = "";
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
	    if(fList!=null){
	        for (File file : fList) {
	        	if(file.isFile() && file.getAbsolutePath().endsWith(browserName + ".exe")){
	        		System.out.println("Browser location: " + file.getPath());
	        		browserlocation = file.getPath();
	        	}
	        	else{
	        		File[] filesinDirectory = file.listFiles();
	        		String filePath = file.getAbsolutePath().toString() + "\\";
	        		for(File fileList : filesinDirectory){
	        			if(fileList.equals(filePath + browserName + ".exe")){
	        				System.out.println("Browser location: " + file.getPath());
			        		browserlocation = file.getPath();
			        		break;
	        			}
	        		}
	        	}
	        }
	    }
		return browserlocation;
	}
}
	
		/*@FindBy(how = How.XPATH, using = "//*[@id='fLogin']/div[2]/div/h3")
		private WebElement loginText;
		@FindBy(how = How.ID, using = "j_username")
		private WebElement usernameTextBox;	
		@FindBy(how = How.ID, using = "j_password")
		private WebElement passwordTextBox;	
		@FindBy(how = How.CSS, using = "#loginBtn>span")
		private WebElement loginButton;
		@FindBy(how = How.XPATH, using = "//*[@id='fLogin']/div[2]/div/div/div[3]/span/a")
		private WebElement forgotPasswordLink;*/
	


