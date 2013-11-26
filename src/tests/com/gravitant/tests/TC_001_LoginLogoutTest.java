package com.gravitant.tests;

import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.gravitant.utils.TestSetup; 
import com.gravitant.pages.*;
//import com.gravitant.LogoutPage;
//import com.gravitant.utils.*;

@Test
public class TC_001_LoginLogoutTest{
	WebDriver driver;
	private static String login = "testuser33";
	private static String pass = "password123";

	@BeforeClass
	public void setUp() throws Exception {
		 System.setProperty("webdriver.firefox.bin","C:\\Program Files (x86)\\Firefox.exe");
		 driver = new FirefoxDriver();
		 driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		 driver.get("https://qa1.mygravitant.com");
	}

	 /*@Test
	 public void testLandingPage() {
		 LandingPage landingPage_Dev = PageFactory.initElements(driver, LandingPage.class);
		 landingPage_Dev.verifyLandingPage();
		 landingPage_Dev.clickSigninButton();
	 }
	 
	 @Test
	 public void testLoginPage() {
		 LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		 loginPage.enterUsername("ramakanth.manga@gravitant.com");
		 loginPage.enterPassword("Gravitant123");
		 loginPage.clickLoginButton();
	 }*/
	 @AfterClass
	 public void tearDown() throws Exception {
	  driver.quit();
	 }
}
