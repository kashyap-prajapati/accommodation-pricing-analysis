package com.accommodation.pricing.analysis;

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
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.script.HotwireScrapper;
import com.accommodation.pricing.analysis.script.MomondoScrapper;
import com.accommodation.pricing.analysis.script.Scrapper;
import com.accommodation.pricing.analysis.service.ApiService;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.accommodation.pricing.analysis.feignclient")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class AccommodationPricingAnalysisApplication implements CommandLineRunner {

	@Autowired private Scrapper scrapper;
	@Autowired private MomondoScrapper momondoScrapper;
	@Autowired private HotwireScrapper hotwireScrapper;
	@Autowired private static HotelRepository hotelRepository;
	@Autowired private ApiService apiService;
	
	public static void main(String[] args) {
		SpringApplication.run(AccommodationPricingAnalysisApplication.class, args);
	}
	
	public static void searchByKeyword() {
		while (true) {
			System.out.println("please select option");
			System.out.println("|        1. Search By City                                         |");
			
			System.out.println("|        2. Search By Name                                            |");
			System.out.println("|        3. Press 3 for exit                                           |");
			
			List<Hotel> hotelListbyCity ;
			List<Hotel> hotelListByName ;
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
		//operationType();
		searchWordUsingInvertedIndex();
		//searchByKeyword();
		//hotwireScrapper.start();
	}
	
	public void searchWordUsingInvertedIndex() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the word !!");
		String str = scanner.next();
		apiService.implementInvertedIndexSearch(str);
		//scanner.close();
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
			System.out.println("|        2. Normal Database search                                 |");
			System.out.println("|        3. Search by word using inverted index                    |");
			System.out.println("|        5. Exit                                                   |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> crawlHotel();
				case 2 -> normalDatabaseSearch();
				case 3 -> searchWordUsingInvertedIndex();
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
			System.out.println("|        1. www.kayak.com                                          |");
			System.out.println("|        2. www.momondo.ca                                         |");
			System.out.println("|        3. www.vrbo.com                                           |");
			System.out.println("|        5. exit                                                   |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> momondoScrapper.start(1);
				case 2 -> momondoScrapper.start(2);
				case 3 -> hotwireScrapper.start();
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
		scanUserInput.close();
	}
	
	
	public void normalDatabaseSearch() {
		Scanner scanUserInput = new Scanner(System.in);
		int operationType = 0 ;
		while(operationType!=5) {
			System.out.println("|==================================================================|");
			System.out.println("|              Please select search criteria                       |");
			System.out.println("|==================================================================|");
			System.out.println("| Operation:                                                       |");
			System.out.println("|        1. Search by City                                         |");
			System.out.println("|        2. Search by Name                                         |");
			System.out.println("|        3. Exit                                                   |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> searchByCity();
				case 2 -> searchByName();
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
		scanUserInput.close();
	}
	
	public void searchByCity() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("|==================================================================|");
		System.out.println("|              Please enter city name                              |");
		System.out.println("|==================================================================|");
		String city = scanner.next();
		List<Hotel> hotels = apiService.getHotelByCity(city);
		apiService.printHotels(hotels);
	}
	
	public void searchByName() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("|==================================================================|");
		System.out.println("|              Please enter hotel name                             |");
		System.out.println("|==================================================================|");
		String name = scanner.next();
		List<Hotel> hotels = apiService.getHotelByName(name);
		apiService.printHotels(hotels);
	}

}
