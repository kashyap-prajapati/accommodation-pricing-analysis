package com.accommodation.pricing.analysis.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.accommodation.pricing.analysis.model.HotwireSearch;


@FeignClient(name="hotwireFeignClient",url="https://www.hotwire.com/api/v4")
public interface HotwireFeignClient {
	
	@GetMapping("/typeahead/{query}?browser=Chrome&client=Homepage&dest=true&device=Desktop&expuserid=-1&features=ta_hierarchy%7Cpostal_code%7Cgoogle%7Cconsistent_display&format=json&guid=72a46e0f-7bbe-4c4c-a013-b2ab3c3131be&listing=false&lob=HOTELS&locale=en_US&maxresults=8&personalize=true&regiontype=2047&siteid=30031&latlong=42.33%2C-83.03&ab=42716.1&trending=true")
	public HotwireSearch getSearchDropdownForHotwire(@PathVariable("query") String query);
}
