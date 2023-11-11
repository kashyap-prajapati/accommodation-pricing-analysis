package com.accommodation.pricing.analysis.script;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.driver.Driver;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.util.HtmlUtil;

@Service
public class Scrapper{
	
	@Autowired private HotelRepository hotelRepository;
	
	private Driver driver;
	
	private static final String DOMAIN_NAME="https://www.ca.kayak.com";
	
	@FindBy(how = How.XPATH,using = "//div[@class='c8JmD']//div[@class='kzGk kzGk-mod-flexible-height kzGk-mod-responsive']//div[@class='yuAt yuAt-pres-rounded']//div[@class='kzGk-resultInner']")
	protected List<WebElement> webElements;
	
	@FindBy(how = How.XPATH, using ="//div[@class='c8JmD']/div[2]/div[@class='c71z4']/div/div[@class='kzGk kzGk-mod-flexible-height kzGk-mod-responsive']/div[@class='yuAt yuAt-pres-rounded']/div[@class='kzGk-resultInner']/div[@class='kzGk-middleSection']/div[@class='FLpo FLpo-mod-flexible-height FLpo-mod-responsive']/div[@class='FLpo-info-top']/div[@class='FLpo-hotel-name']/a[@class='FLpo-big-name']")
	protected List<WebElement> hotelHyperLink;
	
	@FindBy(how = How.XPATH, using = "//div[@class='NITa NITa-location NITa-hasValue NITa-mod-presentation-expanded']/span[@class='NITa-value']")
	private WebElement city;
	
	@FindBy(how = How.XPATH, using = "//div[@class='ejnn ejnn-mod-standalone ejnn-mod-responsive']//div[@class='Joiu-buttons']//button[last()-1]")
	private WebElement lastPageNumber;
	
	@FindBy(how = How.XPATH, using = "//div[@class='ejnn ejnn-mod-standalone ejnn-mod-responsive']//div[@class='Joiu-buttons']//button[last()]")
	private WebElement nextPageButton;
	
	@FindBy(how = How.XPATH, using ="//span[@class='tYfO-amenity-name']" )
	private List<WebElement> amenitiesElement;
	
	@FindBy(how = How.XPATH,using="//span[@class='c9fNw-address']")
	private WebElement addressElement;
	
	@FindBy(how=How.XPATH,using = "//div[@class='b40a-desc-wrap--full b40a-expanded b40a-mod-desc-display']")
	private WebElement overViewElement;
	
	@FindBy(how=How.XPATH,using = "//button[@class='b40a-inline-read-more-button']")
	private WebElement readMoreButton;
	
	public Scrapper() {

	}
	
	public void start(URLParams urlParams){
		this.driver = new Driver(true);
		PageFactory.initElements(driver.getDriver(),this);	
		driver.navigateToUrl(DOMAIN_NAME+"/hotels"+urlParams.generateQueryString());
		traverseThroughAllPage();
	}
	
	public void waitInSeconds(int seconds) throws InterruptedException {
		Thread.sleep(1000*seconds);
	}
	
	public void traverseThroughAllPage() {
		try {
			waitInSeconds(10);
			int i=1;//current page
			while(i<=Integer.parseInt(lastPageNumber.getText())) {
				nextPageButton.click();
				waitInSeconds(10);
				extractingPageData();
				i++;
				break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void extractingPageData() {
		try {
			for(int i=0;i<webElements.size();i++) {
				String htmlString = webElements.get(i).getAttribute("innerHTML");
				
				Document document = HtmlUtil.parse(htmlString); 
				Elements nameElement = HtmlUtil.select(document, ".FLpo-big-name"); 
				Elements locationElement = HtmlUtil.select(document, ".FLpo-location-name");
				Elements priceElement = HtmlUtil.select(document, ".zV27-price");
				Elements reviewDescription = HtmlUtil.select(document, ".FLpo-score-description");
				Elements score = HtmlUtil.select(document, ".FLpo-score");
				Elements reviewCount = HtmlUtil.select(document, ".FLpo-review-count");
				
			    Hotel hotel = new Hotel();
			    hotel.setName(nameElement.first().text());
			    hotel.setPrice(priceElement.first().text());
			    hotel.setLocation(locationElement.first().text());
			    hotel.setCity(city.getText());
			    hotel.setReviewDescription(reviewDescription.first().text());
			    hotel.setScore(score.text());
			    hotel.setReviewCount(reviewCount.first().text());
			    
			    hotelHyperLink.get(i).click();
				
			    String linkURL = nameElement.attr("href");
				hotel.setUrl(DOMAIN_NAME+linkURL);
				 
			    ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
			    driver.getDriver().switchTo().window(tabs.get(1));
			    
				Thread.sleep(2000);
				
				List<String> list =  new ArrayList<>();
				for(WebElement  amenityElement :amenitiesElement) {
					list.add(amenityElement.getText());
				}
				
			    hotel.setAmenities(list);
				hotel.setAddress(addressElement.getText());
				
				readMoreButton.click();
				driver.getDriver().close();
			    driver.getDriver().switchTo().window(tabs.get(0));
				
			    hotelRepository.insert(hotel);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
