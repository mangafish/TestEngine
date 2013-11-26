package com.gravitant.pages;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static junit.framework.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gravitant.utils.Util;

public class LandingPage{
	final WebDriver driver;
	 
	 @FindBy(how = How.XPATH, using = "html/body/div[1]/div[2]/a")
	 private WebElement loginButton;
	 @FindBy(how = How.CSS, using = ".transparentColor>img")
	 private WebElement whiteLabelLogo;
	 
	 public LandingPage(WebDriver driver) {
	  this.driver = driver;
	 }
	 
	 public void verifyLandingPage() {
		if(whiteLabelLogo.isDisplayed()==false){
			System.out.println("not displayed");
		}
		driver.getTitle();
	 }
	 
	 public void clickSigninButton() {
		 loginButton.click();
	 }
	 
	
	 
	 /*public LoginPage login() {
		  //enterUsername(login);
		  //enterPassword(password);
		  clickSigninButton();
		  return PageFactory.initElements(driver, LoginPage.class);
	 }*/
	
}
