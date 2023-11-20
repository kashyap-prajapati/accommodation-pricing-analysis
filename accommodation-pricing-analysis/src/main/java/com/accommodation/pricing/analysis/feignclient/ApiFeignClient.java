package com.accommodation.pricing.analysis.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.accommodation.pricing.analysis.model.LocationSearch;

@FeignClient(name="apiFeignClient",url="https://www.momondo.ca/mvm/smartyv2")
public interface ApiFeignClient {

	@PostMapping("/search?f=j&s=50&lc_cc=CA&lc=en&sv=5&cv=undefined&c=undefined&searchId=undefined&v=undefined")
	public List<LocationSearch> getSearchDropdown(@RequestParam("where") String where);
}
