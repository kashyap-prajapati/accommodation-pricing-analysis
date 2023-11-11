package com.accommodation.pricing.analysis;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.script.Scrapper;



@SpringBootApplication
public class AccommodationPricingAnalysisApplication implements CommandLineRunner {

	@Autowired private Scrapper scrapper;
	
	public static void main(String[] args) {
		SpringApplication.run(AccommodationPricingAnalysisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		operationType();
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
			System.out.println("|        9. Previous menu                                          |");
			System.out.println("|==================================================================|");
			operationType = scanUserInput.nextInt();
			switch (operationType) {
				case 1 -> prepareSearchQuery();
				default -> throw new IllegalArgumentException("Unexpected value: " + operationType);
			}
		}
		scanUserInput.close();
	}
	
	
	public void prepareSearchQuery() {
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

}
