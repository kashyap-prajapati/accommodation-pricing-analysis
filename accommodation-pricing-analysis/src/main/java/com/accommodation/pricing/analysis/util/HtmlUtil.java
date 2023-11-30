package com.accommodation.pricing.analysis.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * 
 * This is the HtmlUtil class to parse HTML string to get Document object.
 * 
 * @author KASHYAP PRAJAPATI 110126934
 *
 */
public class HtmlUtil {
	
	private HtmlUtil() {
		
	}

	/**
	 * This method is used to parse the html string and get document objects
	 * @param htmlString
	 * @return
	 */
	public static Document parse(String htmlString) {
		return Jsoup.parse(htmlString);
	}
	
	
	/**
	 * This method is used to find element based on CSS Selector
	 * @param document
	 * @param selectedHref
	 * @return
	 */
	public static Elements select(Document document,String selectedHref) {
		return document.select(selectedHref);
	}
	
	
	/**
	 * This method is used to get first element text based on CSS selector
	 * @param document
	 * @param selectedHref
	 * @return
	 */
	public static String getFirstElementText(Document document,String selectedHref) {
		Elements  elements = document.select(selectedHref);
		if(elements.isEmpty())
			return "not found";
		return elements.first().text();
	}
	
	
	/**
	 * This method is used to load document from the url.
	 * @param url
	 * @return
	 */
	public static Document loadDoument(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			return null;
		}
	}
}
