package com.accommodation.pricing.analysis.script;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.driver.Driver;
import com.accommodation.pricing.analysis.pageobjects.PageObject;
import com.accommodation.pricing.analysis.util.HtmlUtil;

@Service
public class Scrapper extends PageObject {
	
	private Driver driver;
	
	@FindBy(how = How.XPATH,using = "//div[@class='kzGk kzGk-mod-flexible-height kzGk-mod-responsive']/div[@class='yuAt yuAt-pres-rounded']/div[@class='kzGk-resultInner']")
	protected List<WebElement> webElements;
	
	public Scrapper() {
	}
	
	
	

	public void start() throws InterruptedException {
		this.driver = new Driver();
		PageFactory.initElements(driver.getDriver(),this);	
		driver.navigateToUrl("https://www.ca.kayak.com/hotels/Toronto%2CON%2CCanada-c11592/2023-11-20/2023-11-27/2adults?sort=rank_a");
		Thread.sleep(2000);
		
		for(int i=0;i<webElements.size();i++) {
			String htmlString = webElements.get(i).getAttribute("innerHTML");
			Document document = HtmlUtil.parse(htmlString);
			Elements nameElement = HtmlUtil.select(document, ".FLpo-big-name");
			Elements locationElement = HtmlUtil.select(document, ".FLpo-location-name");
			System.out.println(nameElement.get(0).text());
		}
		//driver.close();
	}
}
