package com.accommodation.pricing.analysis.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accommodation.pricing.analysis.model.Hotel;

public class InvertedIndex {

	private Map<String,Set<Hotel>> invertedIndexData;
	
	private InvertedIndex() {
		
	}
	
	private void checkForInvertedIndexPresentOrNot(String key,Hotel hotel) {
		if(key!=null) {
			if(!invertedIndexData.containsKey(key)) {
				Set<Hotel> set = new HashSet<>();
				set.add(hotel);
				invertedIndexData.put(key,set);
			}else {
				Set<Hotel> set = invertedIndexData.get(key);
				set.add(hotel);
				invertedIndexData.put(key,set);
			}
		}
	}
	
	private void checkForInvertedIndexPresentOrNot(String[] keys,Hotel hotel) {
		for(String key : keys) {
			checkForInvertedIndexPresentOrNot(key,hotel);
		}
	}
	
	
	private void checkForInvertedIndexPresentOrNot(List<String> keys,Hotel hotel) {
		for(String key : keys) {
			checkForInvertedIndexPresentOrNot(key,hotel);
		}
	}
	
	public InvertedIndex(List<Hotel> hotels) {
		invertedIndexData = new LinkedHashMap<>();
		for(Hotel hotel:hotels) {
			String[] nameWords = hotel.getName().split("\\s");
			checkForInvertedIndexPresentOrNot(nameWords,hotel);
			checkForInvertedIndexPresentOrNot(hotel.getAmenities(), hotel);
			checkForInvertedIndexPresentOrNot(hotel.getAllAmenities(), hotel);
		}
	}
	
	
	public List<Hotel> getInvertedIndexSearchData(String userInput) {
		List<Hotel> list = new ArrayList<>();
		for(String key : invertedIndexData.keySet()){
			if(key.startsWith(userInput)) {
				list.addAll(invertedIndexData.get(key));
			}
		}
		System.out.println("list"+list);
		return list;
	}
	
	
}
