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
		public static int nearest_word_cost_calculator(char word_ele_1, char word_ele_2) {
			//Check if words are same or different
	        return word_ele_1 == word_ele_2 ? 0 : 1;
	    }
		
		/**
		 * This function finds the min number from a range of values
		 * @param input_nums: range of values 
		 * @return
		 */
		public static int calculate_operations(int... range_input_vals) {
			//Function to determine minimal number of conversional operations
	        return Arrays.stream(range_input_vals)
	          .min().orElse(Integer.MAX_VALUE);
	    }
		
		/**
		 * This functions suggests corrections by calculating Levenshtein distance between 2 given words
		 * @param user_literal: Word to compare
		 * @param valid_words: List of valid given words in repository
		 * @param threshold_val: The threshold number of suggestions provided
		 */
		public static void suggest_corrections(String user_supplied_misspelled_literal, ArrayList<String> valid_words, int threshold_val){
			//Function to implement correctional suggestions
			//user word length dtermination
			int user_misspelled_word_length = user_supplied_misspelled_literal.length();
			String []atomic_words_hotels;
			//Declaring hashmap to store the suggested words and their conversion cost
			LinkedHashMap<String, Integer> sorted_suggestions = new LinkedHashMap<String, Integer>(); 
			//Variable to store intermediate list of words
			String compare_to_candidate_word = "";
			//Checking individual words conversion cost length
		    for(int list_counter=0; list_counter<valid_words.size(); list_counter++) {  
		    	compare_to_candidate_word = valid_words.get(list_counter);
		    	atomic_words_hotels = compare_to_candidate_word.split(" ");
		    	int min_set_dist = 9999;
		    	for(int atomic_counter = 0; atomic_counter<atomic_words_hotels.length; atomic_counter++ )
		    	{
		    		String single_word = atomic_words_hotels[atomic_counter];
		    		int comparator_word_length = single_word.length();
		    		int[][] comparator_Levenshtein_array = new int[user_misspelled_word_length+1][comparator_word_length+1];
		    		
		    		//Dtermination of Levenshtein distance between 2 words
		    			for(int i=0; i<=user_misspelled_word_length; i++){
		    				for(int j=0; j<=comparator_word_length; j++){
		    					if (i == 0) {
		    						comparator_Levenshtein_array[i][j] = j;
		    		            }
		    		            else if (j == 0) {
		    		            	comparator_Levenshtein_array[i][j] = i;
		    		            }
		    		            else {
		    		            	//Levenshtein distance by dynamic programming
		    		            	comparator_Levenshtein_array[i][j] = calculate_operations(comparator_Levenshtein_array[i - 1][j - 1] 
		    		                 + nearest_word_cost_calculator(user_supplied_misspelled_literal.charAt(i - 1), single_word.charAt(j - 1)), 
		    		                 comparator_Levenshtein_array[i - 1][j] + 1, 
		    		                 comparator_Levenshtein_array[i][j - 1] + 1);
		    		            }
		    				}
		    			}
		    			if(comparator_Levenshtein_array[user_misspelled_word_length][comparator_word_length]<min_set_dist)
		    				min_set_dist = comparator_Levenshtein_array[user_misspelled_word_length][comparator_word_length];
		    	}
		    			
		    			//Insertion into a Linked HashMap
		    			sorted_suggestions.put(compare_to_candidate_word, min_set_dist);
		    	}
		    //Leveraging arraylist to sort the elements of LinkedHashMap
		    List<Map.Entry<String, Integer> > words_arranger_array = new ArrayList<Map.Entry<String, Integer> >(
		    		sorted_suggestions.entrySet());
		    
		    //Ascending arrangement of words in arraylist
		    Collections.sort(
		    		words_arranger_array,
		            new Comparator<Map.Entry<String, Integer> >() {
		                // value based comparison of 2 elements
		                public int compare(
		                    Map.Entry<String, Integer> entry1,
		                    Map.Entry<String, Integer> entry2)
		                {
		 
		                    //Arrnaging the atomic elements
		                    return entry1.getValue()
		                        - entry2.getValue();
		                }
		            });
		    
		    int suggestor_threshold = 1;
		    for (Map.Entry<String, Integer> item : words_arranger_array) {
		    	 
	            // Suggesting until the set threshold
	            System.out.println(suggestor_threshold+" "+item.getKey()+" "+item.getValue());
	            
	            suggestor_threshold++;
	            if(suggestor_threshold==threshold_val)
	            	break;
	        }
		    
		    
		}
		


}
