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

public class Driver {
	private static final Duration EXPLICIT_WAIT_TIME_IN_SECONDS=Duration.ofSeconds(10);
	private WebDriver driver;
	private WebDriverWait webDriverWait;
	
	public Driver(boolean openWindow) {
		System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver-117.exe");
		ChromeOptions opt = new ChromeOptions();
        opt.addArguments("start-maximized");
        opt.addArguments("disable-infobars");
        opt.addArguments("--disable-extensions");
        opt.addArguments("--remote-allow-origins=*");
		this.driver=new ChromeDriver(opt);
		this.webDriverWait = new WebDriverWait(driver, EXPLICIT_WAIT_TIME_IN_SECONDS);
	}
	

	public void navigateToUrl(String url) {
		driver.navigate().to(url);
		driver.manage().window().maximize();
	}
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
	
	public void close() {
		this.driver.close();
	}
	
	public WebElement waitForElementToVisible(String xPath) {
		return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
	}
	
	public List<WebElement> findElements(String xPath){
		return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).findElements(By.xpath(xPath));
	}
	
	public boolean isElementDisplayed(String xPath) {
		try {
			return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).isDisplayed();
		}catch (TimeoutException e) {
			return false;
		}
	}
	
	public void clickOnWebElement(String xPath) {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).click();
	}
	
	public void openNewTab() {
		driver.switchTo().newWindow(WindowType.TAB);
	}
	
	public void closeCurrentTab() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}
	public void switchToTab(String tab) {
		driver.switchTo().window(tab);
	}

}

