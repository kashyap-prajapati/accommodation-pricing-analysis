package com.accommodation.pricing.analysis.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlUtil {
	
	private HtmlUtil() {
		
	}

	public static Document parse(String htmlString) {
		return Jsoup.parse(htmlString);
	}
	
	public static Elements select(Document document,String selectedHref) {
		return document.select(selectedHref);
	}
}
