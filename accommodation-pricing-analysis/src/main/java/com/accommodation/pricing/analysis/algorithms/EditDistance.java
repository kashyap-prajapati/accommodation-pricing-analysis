	package com.accommodation.pricing.analysis.algorithms;
/**
 * This program is based on LCS dynamic programming technique to suggest words based on Levenshtein distance.
 * It handles difference between 2 words in terms of insertion, deletion and substitution of characters among them.
 * @author Subhram Satyajeet (110127932)
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EditDistance {
		
	/**
	 * This function checks if 2 words are same or different
	 * @param a: word 1 
	 * @param b: word 2
	 * @return
	 */
		public static int cost_to_nearest_word(char a, char b) {
			//Function to check the cost of converting one word to another
	        return a == b ? 0 : 1;
	    }
		
		/**
		 * This function finds the min number from a range of values
		 * @param input_nums: range of values 
		 * @return
		 */
		public static int find_min_operations(int... input_nums) {
			//Function to calculate the minimum number of operations to convert one word to another
	        return Arrays.stream(input_nums)
	          .min().orElse(Integer.MAX_VALUE);
	    }
		
		/**
		 * This functions suggests corrections by calculating Levenshtein distance between 2 given words
		 * @param user_literal: Word to compare
		 * @param valid_words: List of valid given words in repository
		 * @param threshold_val: The threshold number of suggestions provided
		 */
		public static void suggest_corrections(String user_literal, ArrayList<String> valid_words, int threshold_val){
			//Function to suggest the nearest word based on the misspelled word
			//Variable to take user provided word
			int user_word_length = user_literal.length();
			String []atomic_words_hotels;
			//Declaring hashmap to store the suggested words and their conversion cost
			LinkedHashMap<String, Integer> sorted_suggestions = new LinkedHashMap<String, Integer>(); 
			//Variable to store each word from the list of words
			String word_to_compare = "";
			//Looping through each word in the list to calculate the cost of conversion
		    for(int list_counter=0; list_counter<valid_words.size(); list_counter++) {  
		    	word_to_compare = valid_words.get(list_counter);
		    	atomic_words_hotels = word_to_compare.split(" ");
		    	int min_dist = 9999;
		    	for(int atomic_counter = 0; atomic_counter<atomic_words_hotels.length; atomic_counter++ )
		    	{
		    		String single_word = atomic_words_hotels[atomic_counter];
		    		int comparator_word_length = single_word.length();
		    		int[][] comparator_array = new int[user_word_length+1][comparator_word_length+1];
		    		//System.out.println(comparator_array);
		    		//Code to calculate the Levenshtein distance based on LCS programming technique
		    			for(int i=0; i<=user_word_length; i++){
		    				for(int j=0; j<=comparator_word_length; j++){
		    					if (i == 0) {
		    						comparator_array[i][j] = j;
		    		            }
		    		            else if (j == 0) {
		    		            	comparator_array[i][j] = i;
		    		            }
		    		            else {
		    		            	//Calculating the Levenshtein distance
		    		            	comparator_array[i][j] = find_min_operations(comparator_array[i - 1][j - 1] 
		    		                 + cost_to_nearest_word(user_literal.charAt(i - 1), single_word.charAt(j - 1)), 
		    		                 comparator_array[i - 1][j] + 1, 
		    		                 comparator_array[i][j - 1] + 1);
		    		            }
		    				}
		    			}
		    			if(comparator_array[user_word_length][comparator_word_length]<min_dist)
		    				min_dist = comparator_array[user_word_length][comparator_word_length];
		    	}
		    			
		    			//Inserting the words and their distances into the hashmap
		    			sorted_suggestions.put(word_to_compare, min_dist);
		    	}
		    //Declaring an arraylist to sort the words in Linked Hash Map based on their distances in ascending order
		    List<Map.Entry<String, Integer> > sorter_array = new ArrayList<Map.Entry<String, Integer> >(
		    		sorted_suggestions.entrySet());
		    
		    //Sorting the words in the Linked Hash Map
		    Collections.sort(
		    		sorter_array,
		            new Comparator<Map.Entry<String, Integer> >() {
		                // Comparing two entries by value
		                public int compare(
		                    Map.Entry<String, Integer> entry1,
		                    Map.Entry<String, Integer> entry2)
		                {
		 
		                    // Subtracting the entries
		                    return entry1.getValue()
		                        - entry2.getValue();
		                }
		            });
		    
		    int display_counter = 1;
		    for (Map.Entry<String, Integer> item : sorter_array) {
		    	 
	            // Printing the sorted map till the threshold value
	            System.out.println(display_counter+" "+item.getKey()+" "+item.getValue());
	            
	            display_counter++;
	            if(display_counter==threshold_val)
	            	break;
	        }
		    
		    
		}
		


}
