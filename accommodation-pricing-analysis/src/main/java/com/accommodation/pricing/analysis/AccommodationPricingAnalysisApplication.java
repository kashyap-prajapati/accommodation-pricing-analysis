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
import com.accommodation.pricing.analysis.script.VerboScrapper;
import com.accommodation.pricing.analysis.script.MomondoScrapper;
import com.accommodation.pricing.analysis.service.ApiService;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.accommodation.pricing.analysis.feignclient")
@ImportAutoConfiguration({FeignAutoConfiguration.class})

/**
 * Main application class for Accommodation Pricing Analysis.
 * @author sachreet kaur(110122441)
 */
public class AccommodationPricingAnalysisApplication implements CommandLineRunner {

	@Autowired private MomondoScrapper momondoScrapper;
	@Autowired private VerboScrapper hotwireScrapper;
	@Autowired private ApiService apiService;
	
	public static void main(String[] args) {
		SpringApplication.run(AccommodationPricingAnalysisApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		operationType();
	}
	
	// Method to perform word search using Inverted Index
	public void searchWordUsingInvertedIndex() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("|==================================================================|");
		System.out.println("|     Please enter the word ?                                      |");
		System.out.println("|==================================================================|");
	
		System.out.println("Please enter the word !!");
		String str = scanner.next();
		apiService.implementInvertedIndexSearch(str);
		scanner.close();
	}
	
	// Method to handle the main menu operations
	public void operationType() throws InterruptedException {
		int operationType = 0 ;
		while(operationType!=5) {
			Scanner scanUserInput = new Scanner(System.in);
			System.out.flush();
			System.out.println("\n\n");
			System.out.println("|==================================================================|");
			System.out.println("|                    MAIN MENU                                     |");
			System.out.println("|==================================================================|");
			System.out.println("|Operation:                                                       |");
			System.out.println("|        1. Crawl the site                                         |");
			System.out.println("|        2. Normal Database search                                 |");
			System.out.println("|        3. Search by word using inverted index                    |");
			System.out.println("|        5. Exit                                                   |");
			System.out.println("|==================================================================|");
			System.out.flush();
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> crawlHotel();
				case 2 -> normalDatabaseSearch();
				case 3 -> searchWordUsingInvertedIndex();
				case 5 -> {
					System.out.println("|==================================================================|");
					System.out.println("|         Thank you for visiting                                   |");
					System.out.println("|==================================================================|");
					break;
				}
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
	}
	
	// Method to perform scrapping
	public void crawlHotel() throws InterruptedException {
		Scanner scanUserInput = new Scanner(System.in);
		int operationType = 0 ;
		while(operationType!=4) {
			System.out.flush();
			System.out.println("\n\n");
			System.out.println("|==================================================================|");
			System.out.println("|              Please select a site to scrap                       |");
			System.out.println("|==================================================================|");
			System.out.println("| Operation:                                                       |");
			System.out.println("|        1. KAYAK (https://www.ca.kayak.com/)                      |");
			System.out.println("|        2. MOMONDO (https://www.momondo.ca/)                      |");
			System.out.println("|        3. VERBO (https://www.vrbo.com/)                          |");
			System.out.println("|        4. Previous Menu                                          |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> momondoScrapper.start(1);
				case 2 -> momondoScrapper.start(2);
				case 3 -> hotwireScrapper.start();
				case 4 -> {
					break;
				}
			}
		}
	}
	
	// Method to perform normal database search
	public void normalDatabaseSearch() {
		Scanner scanUserInput = new Scanner(System.in);
		int operationType = 0 ;
		while(operationType!=3) {
			System.out.println("|==================================================================|");
			System.out.println("|              Please select search criteria                       |");
			System.out.println("|==================================================================|");
			System.out.println("| Operation:                                                       |");
			System.out.println("|        1. Search by City                                         |");
			System.out.println("|        2. Search by Name                                         |");
			System.out.println("|        3. Previous Menu                                          |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> searchByCity();
				case 2 -> searchByName();
				case 3 -> {
					break;
				}
			}
		}
	}
	
	// Method to search hotels by city
	public void searchByCity() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("|==================================================================|");
		System.out.println("|              Please enter city name                              |");
		System.out.println("|==================================================================|");
		String city = scanner.next();
		List<Hotel> hotels = apiService.getHotelByCity(city);
		apiService.printHotels(hotels);
	}
	
	// Method to search hotels by name
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
