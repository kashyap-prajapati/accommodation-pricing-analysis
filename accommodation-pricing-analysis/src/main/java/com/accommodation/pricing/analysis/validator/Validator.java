package com.accommodation.pricing.analysis.validator;

import java.util.regex.Pattern;

public class Validator {
	
	private static final String EXCLUDE_ALPHANUM_PATTERN = "[^A-Za-z0-9\\s]";
	private static final String EXCLUDE_ALPHANUM_SPACE_PATTERN="[^A-Za-z]";
	private static final String EXCLUDE_NUMBER_PATTERN = "[^0-9]";
	private static final String WHITESPACE_PATTERN ="[\\s]";
	private static final Pattern numberPattern = Pattern.compile("^[0-9]+$");

	
	private Validator() {
		
	}
	
	public static boolean checkValidNumber(String inputNumber) {
		return numberPattern.matcher(inputNumber).matches();
	}
	
	public static String removeSpecialCharacterFromText(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_ALPHANUM_PATTERN, "");
	}
	
	public static String removeSpecicalCharacterWithSpaceFromText(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_ALPHANUM_SPACE_PATTERN, "");
	}
	
	public static String removeSpecialCharacterFromNumber(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_NUMBER_PATTERN, "");
	}
	public static String removeWhiteSpace(String text) {
		if(text==null)
			return text;
		return text.replaceAll(WHITESPACE_PATTERN, "");
	}
}
