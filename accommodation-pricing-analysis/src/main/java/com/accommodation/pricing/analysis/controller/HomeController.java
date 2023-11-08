package com.accommodation.pricing.analysis.controller;

import java.io.IOException;

import org.checkerframework.checker.units.qual.s;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accommodation.pricing.analysis.script.Scrapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@RestController
public class HomeController {
	
	@Autowired Scrapper s;
	

	@GetMapping("/index")
	public ResponseEntity<Object> getPageHtml() throws IOException, InterruptedException{
		
		s.start();
//		String connectionString = "mongodb://localhost:27017/test-admin?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";
//		ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(connectionString))
//                .serverApi(serverApi)
//                .build();
//        try (MongoClient mongoClient = MongoClients.create(settings)) {
//            try {
//                MongoDatabase database = mongoClient.getDatabase("test-admin");
//                database.getCollection("hotel-collections").insertOne(new org.bson.Document("ping",1));
//              
//            } catch (MongoException e) {
//                e.printStackTrace();
//            }
//        }
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
