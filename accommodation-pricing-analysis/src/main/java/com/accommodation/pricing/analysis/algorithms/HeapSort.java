package com.accommodation.pricing.analysis.algorithms;
/**
 * This program performs heapsort to find the best deals for users from hotel price list.
 * @author Subhram Satyajeet (110127932)
 *
 */

//Code to sort the data on prices
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HeapSort {

	
	//sorting functionality
	/**
	 * This function performs heapsort on the list of hotels and their prices using recursion
	 * @param entryList: The list of hotel names and prices
	 */
    public static void sortHotelPriceFuction(List<Map.Entry<Integer, String>> entryList) {
        int dim_size = entryList.size();

        //recursively sort the functionality
        for (int heapsize = dim_size / 2 - 1; heapsize >= 0; heapsize--) {
            remain_heapify(entryList, dim_size, heapsize);
        }

        
        for (int heapsize = dim_size - 1; heapsize > 0; heapsize--) {
            
            Collections.swap(entryList, 0, heapsize);

           
            remain_heapify(entryList, heapsize, 0);
        }
    }

    /**
     * This function performs heapify after insertion of elements
     * @param remainList: the remaining list passed after sorting
     * @param heapsize: the size of the remaining heap
     * @param node_pos: the root node value
     */
    private static void remain_heapify(List<Map.Entry<Integer, String>> remainList, int heapsize, int node_pos) {
        int node_large_val = node_pos; 
        int left_small_val = 2 * node_pos + 1; 
        int right_small_val = 2 * node_pos + 2;

        // Check if left child is greater than the root
        if (left_small_val < heapsize && remainList.get(left_small_val).getKey() > remainList.get(node_large_val).getKey()) {
        	node_large_val = left_small_val;
        }

        // Check if right child is greater
        if (right_small_val < heapsize && remainList.get(right_small_val).getKey() > remainList.get(node_large_val).getKey()) {
        	node_large_val = right_small_val;
        }

        // If the value of root is not the largest
        if (node_large_val != node_pos) {
            Collections.swap(remainList, node_pos, node_large_val);

            // Recursively heapify the affected sub-tree
            remain_heapify(remainList, heapsize, node_large_val);
        }
    }
	

}
