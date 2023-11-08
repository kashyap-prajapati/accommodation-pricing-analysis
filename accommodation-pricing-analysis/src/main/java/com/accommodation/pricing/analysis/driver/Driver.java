package com.accommodation.pricing.analysis.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Driver {
	private WebDriver driver;
	public Driver() {
		System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver-117.exe");
		this.driver = new ChromeDriver();
	}
	
	public void navigateToUrl(String url) {
		driver.navigate().to(url);
		driver.manage().window().maximize();
	}
	
	public WebDriver getDriver() {
		return this.driver;
	}
}

