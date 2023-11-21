package com.accommodation.pricing.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.script.MomondoScrapper;
import com.accommodation.pricing.analysis.script.Scrapper;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.accommodation.pricing.analysis.feignclient")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class AccommodationPricingAnalysisApplication implements CommandLineRunner {

	@Autowired private Scrapper scrapper;
	@Autowired private MomondoScrapper oneFineStayScrapper;
	@Autowired private static HotelRepository hotelRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(AccommodationPricingAnalysisApplication.class, args);
	}
	
	public static void searchByKeyword() {
		while (true) {
			System.out.println("please select option");
			System.out.println("|        1. Search By City                                         |");
			
			System.out.println("|        2. Search By Name                                            |");
			System.out.println("|        3. Press 3 for exit                                           |");
			
			List<Hotel> hotelListbyCity = new ArrayList<Hotel>();
			List<Hotel> hotelListByName = new ArrayList<Hotel>();
			Scanner scanner = new Scanner(System.in);
			int type = scanner.nextInt();
			if(type == 3) {
				break;
			}
			if(type == 1) {
				System.out.println("please enter city name");
				String city = scanner.next();
				 hotelListbyCity= hotelRepository.getHotelByCity(city);
				 for(Hotel hotel: hotelListbyCity ) {
						System.out.println("|==================================================================|");
						System.out.println("|Name :"+hotel.getName()+"|");
						System.out.println("|Price :"+hotel.getPrice()+"|");
						System.out.println("|Address :"+hotel.getAddress()+"|");
						System.out.println("|City :"+hotel.getCity()+"|");
						System.out.println("|Score :"+hotel.getScore()+"|");
						System.out.println("|Review Count :"+hotel.getReviewCount()+"|");
						System.out.println("|Amenties :"+hotel.getAmenities()+"|");
						
						System.out.println("|==================================================================|");
					}
			}
			else if(type == 2) {
				System.out.println("please enter hotel name");
				String name = scanner.next();
				 hotelListByName= hotelRepository.getHotelByName(name);
				 for(Hotel hotel: hotelListByName ) {
						System.out.println("|==================================================================|");
						System.out.println("|Name :"+hotel.getName()+"|");
						System.out.println("|Price :"+hotel.getPrice()+"|");
						System.out.println("|Address :"+hotel.getAddress()+"|");
						System.out.println("|City :"+hotel.getCity()+"|");
						System.out.println("|Score :"+hotel.getScore()+"|");
						System.out.println("|Review Count :"+hotel.getReviewCount()+"|");
						System.out.println("|Amenties :"+hotel.getAmenities()+"|");
						
						System.out.println("|==================================================================|");
					}
			}
			else {
				System.out.println("please enter valid input");
				continue;
			}
			
			}
	};

	
	@Override
	public void run(String... args) throws Exception {
		//oneFineStayScrapper.start();
		operationType();
		//searchByKeyword();
		
	}
	
	
	public void operationType() throws InterruptedException {
		Scanner scanUserInput = new Scanner(System.in);
		int operationType = 0 ;
		while(operationType!=5) {
			System.out.flush();
			System.out.println("\n\n");
			System.out.println("|==================================================================|");
			System.out.println("|                    MAIN MENU                                     |");
			System.out.println("|==================================================================|");
			System.out.println("| Operation:                                                       |");
			System.out.println("|        1. Crawl the site                                         |");
			
			System.out.println("|        5. Exit                                                   |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> crawlHotel();
				case 5 -> {
					System.out.println("Thank you for visiting !!");
					break;
				}
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
		scanUserInput.close();
	}
	
	public void crawlHotel() throws InterruptedException {
		Scanner scanUserInput = new Scanner(System.in);
		int operationType = 0 ;
		while(operationType!=5) {
			System.out.flush();
			System.out.println("\n\n");
			System.out.println("|==================================================================|");
			System.out.println("|              Select a Site to Scrap                              |");
			System.out.println("|==================================================================|");
			System.out.println("| Operation:                                                       |");
			System.out.println("|        1. www.kayal.com                                          |");
			System.out.println("|        2. www.onefinestay.com                                    |");
			System.out.println("|        3. www.priceline.com                                      |");
			System.out.println("|        9. Previous menu                                          |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> oneFineStayScrapper.start();
				case 2 -> oneFineStayScrapper.start();
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
		scanUserInput.close();
	}
	
	
	public void prepareSearchQueryForKayal() {
		System.out.flush();
		String location = "Toronto,Ontario,Canada,Pearson-Intl-c11592-lYYZ";
		Scanner scanUserInput = new Scanner(System.in);
		System.out.println("Please provide from date (YYYY-MM-DD) : ");
		String fromDate = scanUserInput.next();
		System.out.println("Please provide to date (YYYY-MM-DD) :");
		String toDate = scanUserInput.next();
		System.out.println("Please enter number of adults");
		String adults = scanUserInput.next()+"adults";
		URLParams urlParams = new URLParams(location,fromDate,toDate,adults);
		System.out.println(urlParams.generateQueryString());
		scrapper.start(urlParams);
	}
	
	public void prepareSearchQueryForOneFineStay() {
		System.out.flush();
		String location = "";
	}
	
	public void prepareSerachQueryForPriceline() {
		System.out.flush();
		String location = "Montreal, QC, Canada";
		Scanner scanUserInput = new Scanner(System.in);
		System.out.println("Please provide from date (YYYY-MM-DD) : ");
		String fromDate = scanUserInput.next();
		System.out.println("Please provide to date (YYYY-MM-DD) :");
		String toDate = scanUserInput.next();
		System.out.println("Please enter number of rooms");
		String adults = scanUserInput.next();
		
		
	}

}
