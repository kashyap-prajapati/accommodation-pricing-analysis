package com.accommodation.pricing.analysis.validator;

import java.util.regex.Pattern;

/**
 * Utility class for validating and processing text.
 * 
 * @author sachreet kaur(110122441)
 */

public class Validator {
	
	// Regular expression pattern to exclude all non-alphanumeric characters and spaces.
	private static final String EXCLUDE_ALPHANUM_PATTERN = "[^A-Za-z0-9\\s]";
	
	// Regular expression pattern to exclude all non-alphabetic characters.
	private static final String EXCLUDE_ALPHANUM_SPACE_PATTERN="[^A-Za-z]";
	
	// Regular expression pattern to exclude all non-numeric characters.
	private static final String EXCLUDE_NUMBER_PATTERN = "[^0-9]";
	
	// Regular expression pattern to match whitespace characters.
	private static final String WHITESPACE_PATTERN ="[\\s]";
	
	// Pattern to check if a string consists only of numeric characters.
	private static final Pattern numberPattern = Pattern.compile("^[0-9]+$");

	/**
	 * Private constructor that prevent to make instance of the class.
	 * 
	 * @author sachreet kaur(110122441)
	 * 
	 */
	private Validator() {
		
	}
	
	/**
	 * Checks if a given string consists only of numeric characters.
	 *
	 * @param inputNumber :  checking the input string.
	 * @return True if the input consists only of numeric characters, false otherwise.
	 * 
	 *  @author sachreet kaur(110122441)
	 */
	public static boolean checkValidNumber(String inputNumber) {
		return numberPattern.matcher(inputNumber).matches();
	}
	
	
	/**
	 * Removes all non-alphanumeric and space characters from a given text.
	 *
	 * @param text : The input text.
	 * @return Special characters and spaces have been removed from the text.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public static String removeSpecialCharacterFromText(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_ALPHANUM_PATTERN, "");
	}
	
	
	/**
	 * Removes all non-alphabetic characters from a given text.
	 *
	 * @param text: The input text.
	 * @return Non-alphabetic characters have been removed from the text.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public static String removeSpecicalCharacterWithSpaceFromText(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_ALPHANUM_SPACE_PATTERN, "");
	}
	

	/**
	 * Removes all non-numeric characters from a given text.
	 *
	 * @param text The input text.
	 * @return Text with non-numeric characters removed.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public static String removeSpecialCharacterFromNumber(String text) {
		if(text==null)
			return text;
		return text.replaceAll(EXCLUDE_NUMBER_PATTERN, "");
	}
	
	/**
	 * Removes all whitespace characters from a given text.
	 *
	 * @param text The input text.
	 * @return Text with whitespace characters removed.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public static String removeWhiteSpace(String text) {
		if(text==null)
			return text;
		return text.replaceAll(WHITESPACE_PATTERN, "");
	}
}
