package com.accommodation.pricing.analysis.algorithms;

import java.util.Arrays;
import java.util.List;

import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.validator.Validator;

/**
 * 
 * @author SAMIKSHA ARORA
 *
 */
public class PageRanking {
    
    public void implementPageRank(List<Hotel> hotelList) {
    	String content = "";
    	for(Hotel hotel : hotelList) {
    		content = content+ " " +Validator.removeSpecialCharacterFromText(hotel.getOverView());
    	}
    	 String[] words = content.toLowerCase().split("\\s+");

    	 int[] keywordOccurrences = new int[words.length];
         for (int i = 0; i < words.length; i++) {
             for (int j = i + 1; j < words.length; j++) {
                 if (words[i].equals(words[j])) {
                     keywordOccurrences[i]++;
                 }
             }
         }
         
         int[] keywordRanks = new int[words.length];
         for (int i = 0; i < words.length; i++) {
             for (int j = 0; j < words.length; j++) {
                 if (keywordOccurrences[i] > keywordOccurrences[j]) {
                     keywordRanks[i]++;
                 }
             }
         }
         Keyword[] keywordArray = new Keyword[words.length];
         for (int i = 0; i < words.length; i++) {
             keywordArray[i] = new Keyword(words[i], keywordOccurrences[i], keywordRanks[i]);
         }
         Arrays.sort(keywordArray);
     	 System.out.println("|==================================================================|");
     	 System.out.println("|   Keyword       |    Occerrences    |        Rank                |");
    	 System.out.println("|==================================================================|");
         for (Keyword keyword : keywordArray) {
        	 System.out.println("|             "+keyword.getWord()+"       |  "+Integer.toString(keyword.getOccurrences())+"        |      "+Integer.toString(keyword.getRank()));
         }
     	 System.out.println("|==================================================================|");
    }

    static class Keyword implements Comparable<Keyword> {
        private String word;
        private int occurrences;
        private int rank;

        public Keyword(String word, int occurrences, int rank) {
            this.word = word;
            this.occurrences = occurrences;
            this.rank = rank;
        }

        public String getWord() {
            return word;
        }

        public int getOccurrences() {
            return occurrences;
        }

        public int getRank() {
            return rank;
        }

        @Override
        public int compareTo(Keyword other) {
            return Integer.compare(other.rank, this.rank);
        }
    }
}
