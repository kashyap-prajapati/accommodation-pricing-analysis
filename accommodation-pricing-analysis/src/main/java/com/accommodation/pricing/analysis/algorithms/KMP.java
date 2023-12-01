package com.accommodation.pricing.analysis.algorithms;

/**
 * KMP class implement knuth moris pratt algorithm to match the pattern in string.
 * 
 * @author Aditya bhate
 *
 */
public class KMP {
	
	/**
	 * Empty constructor
	 */
	public KMP() {
		
	}
	
	/**
	 * Search pattern in text  
	 * @param pattern : pattern
	 * @param text : text
	 * @return -1 :  pattern is not found | index : found pattern.
	 */
	public int searchKMP(String pattern, String text)
    {
        int M = pattern.length();
        int N = text.length();
        int lps[] = new int[M];
        int j = 0; 
        
        lps(pattern, M, lps);
 
        int i = 0; 
        while ((N - i) >= (M - j)) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                System.out.println("Found " +pattern + " at index "+ (i - j));
                j = lps[j - 1];
                return i - j;
            }
            else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return -1;
    }
 
	/**
	 * Search in LPS array
	 * @param pattern
	 * @param M
	 * @param lps
	 */
    void lps(String pattern, int M, int lps[]){
        int length = 0;
        int i = 1;
        lps[0] = 0; 
        while (i < M) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
            	length++;
                lps[i] = length;
                i++;
            }
            else 
            { 
                if (length != 0) {
                	length = lps[length - 1];
                }
                else {
                    lps[i] = length;
                    i++;
                }
            }
        }
    }

}
