package com.accommodation.pricing.analysis.driver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accommodation.pricing.analysis.constant.Constants;

/**
 * Driver class is responsible for performing operation selenium web driver and web driver wait instance.
 * This class contains various methods that are useful to performing scrap operations.
 * for example:
 * navigateToUrl(String url) :  naviagte to url inside selenium browser window
 * 
 * @author Kashyap Prajapati (110126934)
 *
 */
public class Driver {
	
	private static final Duration EXPLICIT_WAIT_TIME_IN_SECONDS=Duration.ofSeconds(10);
	
	private WebDriver driver;
	private WebDriverWait webDriverWait;
	
	/**
	 * Constructor is used to initialized the driver and web driver instance of 
	 * the selenium.
	 * Also set the system properties of chrome driver and chromer driver path.
	 * 
	 */
	public Driver() {
		System.setProperty(Constants.CHROME_DRIVER,"src/main/resources/chromedriver-117.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.addArguments("start-maximized");
        opt.addArguments("disable-infobars");
        opt.addArguments("--disable-extensions");
        opt.addArguments("--remote-allow-origins=*");
        opt.addArguments("--ignore-certificate-errors");
        opt.addArguments("--allow-insecure-localhost");
        opt.addArguments("acceptInsecureCerts");
		this.driver=new ChromeDriver(opt);
		this.webDriverWait = new WebDriverWait(driver, EXPLICIT_WAIT_TIME_IN_SECONDS);
	}
	

	/**
	 * This method is used to navigate to url inside browser.
	 * @param url :  input url 
	 */
	public void navigateToUrl(String url) {
		driver.navigate().to(url);
		driver.manage().window().maximize();
	}
	
	/**
	 * This method is used to get the instance of the driver.
	 * @return the web driver instance of the selenium
	 */
	public WebDriver getDriver() {
		return this.driver;
	}	
	
	
	/**
	 * close the driver instance of selenium.
	 * 
	 */
	public void close() {
		this.driver.close();
	}
	
	/**
	 * This method is used to find single element in browsers 
	 * @param xPath : x path of the elements
	 * @return : single element of browsers
	 */
	public WebElement waitForElementToVisible(String xPath) {
		return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
	}
	
	/**
	 * This method is used to find multiple elements in browsers
	 * @param xPath : xpath of the respective elements
	 * @return : the list of web elements that represent x path
	 */
	public List<WebElement> findElements(String xPath){
		return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).findElements(By.xpath(xPath));
	}
	
	/**
	 * This method is used to check element is displayed or not in browser:
	 * @param xPath : xPath of the respective elements.
	 * @return : true element is available | false element is not available or throwing timeout exception.
	 */
	public boolean isElementDisplayed(String xPath) {
		try {
			return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).isDisplayed();
		}catch (TimeoutException e) {
			return false;
		}
	}
	
	/**
	 * This method is used to perform click operation on web elements.
	 * Use until method of web driver wait to check element is available or not before performing click operations.
	 * @param xPath :  XPATH of respective element where you want to perform click operations
	 */
	public void clickOnWebElement(String xPath) {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).click();
	}
	
	
	/**
	 * This method is used to open a new tab in browsers
	 */
	public void openNewTab() {
		driver.switchTo().newWindow(WindowType.TAB);
	}
	
	/**
	 * This method close the current tab
	 */
	public void closeCurrentTab() {
		ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}
	
	/**
	 * This method is used to switch the tabs in browsers
	 * @param tab :  name of tab where you want switch
 	 */
	public void switchToTab(String tab) {
		driver.switchTo().window(tab);
	}
	
	
	/**
	 * This method is used to close the current tab and switch to parent tab.
	 * return void
	 */
	public void closeCurrentAndSwitchToParent() {
		 ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		 if(tabs.size()>1) {
			 driver.close();
			 driver.switchTo().window(tabs.get(0));
		 }
	}
}

