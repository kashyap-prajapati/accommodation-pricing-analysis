package com.accommodation.pricing.analysis.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PageRanking {

    public static void main(String[] args) {
        try {
            // Read the input file
            BufferedReader reader = new BufferedReader(new FileReader("G:\\University_Of_Windsor_Assignments\\ACC\\ACC_Project\\Data\\input"));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }

            // Close the reader
            reader.close();

            // Split the content into words
            String[] words = content.toString().toLowerCase().split("\\s+");

            // Count occurrences of each word
            int[] keywordOccurrences = new int[words.length];
            for (int i = 0; i < words.length; i++) {
                for (int j = i + 1; j < words.length; j++) {
                    if (words[i].equals(words[j])) {
                        keywordOccurrences[i]++;
                    }
                }
            }

            // Rank the keywords
            int[] keywordRanks = new int[words.length];
            for (int i = 0; i < words.length; i++) {
                for (int j = 0; j < words.length; j++) {
                    if (keywordOccurrences[i] > keywordOccurrences[j]) {
                        keywordRanks[i]++;
                    }
                }
            }

            // Create an array of Keyword objects for sorting
            Keyword[] keywordArray = new Keyword[words.length];
            for (int i = 0; i < words.length; i++) {
                keywordArray[i] = new Keyword(words[i], keywordOccurrences[i], keywordRanks[i]);
            }

            // Sort the array based on rank
            Arrays.sort(keywordArray);

            // Print the sorted result
            //System.out.println("Keyword\tOccurrences\tRank");
            //for (Keyword keyword : keywordArray) {
            //    System.out.println(keyword.getWord() + "\t" + keyword.getOccurrences() + "\t\t" + keyword.getRank());
            //}

            // Store the sorted result in a CSV file
            FileWriter csvWriter = new FileWriter("output.csv");
            csvWriter.append("Keyword,Occurrences,Rank\n");
            for (Keyword keyword : keywordArray) {
                csvWriter.append(keyword.getWord()).append(",").append(Integer.toString(keyword.getOccurrences())).append(",")
                        .append(Integer.toString(keyword.getRank())).append("\n");
            }
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            // Compare based on rank (descending order)
            return Integer.compare(other.rank, this.rank);
        }
    }
}
