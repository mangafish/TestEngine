package com.gravitant.utils;

import static org.hamcrest.CoreMatchers.*;

import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;

import com.gravitant.pages.LoginPage;
import com.thoughtworks.selenium.Selenium;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.gravitant.utils.BrowserType.*;

public class TestSetup{		
	public static WebDriver driver;
	private static Logger log = LoggerFactory.getLogger(LoginPage.class);
	public TestSetup(){
		
	}
	@BeforeTest
	public static WebDriver selectBrowser(String browserName) throws Exception {
	/*	driver = LaunchBrowser.getDriver(browserName.toString());		
		driver = DriverFactory.getDriver(InternetExplorer.name());*/
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		log.info("Launching browser" + browserName);
		return driver;		
	}	
	@BeforeTest
	public static void selectUrl(String Url) {
		//QA Url
		if(Url == "QA"){
			driver.get("https://qa1.mygravitant.com");
		}else if(Url == "Prod"){
			driver.get("https://qa1.mygravitant.com");
		}
		log.info("Navigating to Url" + Url);
	}
	@AfterTest
	public static void tearDown() throws Exception {
		log.info("Closing browser");
		driver.quit();
	}	
}
