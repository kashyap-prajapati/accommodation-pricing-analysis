package com.accommodation.pricing.analysis.algorithms;

import java.util.ArrayList;
import java.util.List;
/**
 * This class implements the Trie data structure for word suggestion purposes that
 * enabling autocomplete and word prediction functions, 
 * it offers functionality for effectively storing and retrieving words.
 * 
 * @author sachreet kaur(110122441)
 *
 */
public class Trie {
	
	Node rootNode; 
	
	/**
	 * Constructor for Trie that initializes the Trie with a list of words.
	 * 
	 * @param words List of words to be inserted into the Trie.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public Trie(List<String> words) {
		rootNode = new Node();
		for(String word:words) {
			rootNode.insertNode(word);
		}
	}
	/**
	 * 
	 * @param prefix : The prefix  used to find for searching in Trie.
	 * @param position : If the last node is the end of a word, see if it is true. 
	 * @return True if the Trie contains the node with the specified prefix.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public boolean findNode(String prefix,boolean position) {
		Node lastNode = rootNode;
		for(char character : prefix.toCharArray()) {
			lastNode = lastNode.childs.get(character);
			if(lastNode==null)
				return false;
		}
		return !position || lastNode.isWord;
	}
	
	/**
	 * 
	 * @param prefix : The prefix in the Trie that has to be looked up. 
	 *  
	 * @return If the last node is the end of a word, see if it is true. 
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public boolean find(String prefix) {
		return findNode(prefix, false);
	}
	
	/**
	 * Generates word recommendations recursively, beginning at the specified node.
	 * 
	 * @param rootNode : the initial suggestion-generating node.
	 * @param list : The list where the produced suggestions are stored.
	 * @param buffer  :To create the recommendations, use StringBuffer.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public void getSuggestion(Node rootNode,List<String> list, StringBuffer buffer){
		if(rootNode.isWord)
			list.add(buffer.toString());
		
		if(rootNode.childs == null || rootNode.childs.isEmpty())
			return;
		
		for(Node childNode : rootNode.childs.values()) {
			getSuggestion(childNode, list, buffer.append(childNode.character));
			buffer.setLength(buffer.length()-1);
		}
	}
	
	/**
	 * Based on the supplied prefix, word recommendations are obtained.
	 * 
	 * @param prefix :The prefix that needs recommendations. 
	 * @return :A list of recommended words.x.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public List<String> getSuggestion(String prefix){
		List<String> list = new ArrayList<>();
		Node lastNode = rootNode;
		StringBuffer buffer = new StringBuffer();
		for(char character : prefix.toCharArray()) {
			lastNode = lastNode.childs.get(character);
			if(lastNode == null)
				return list;
			buffer.append(character);
		}
		getSuggestion(lastNode, list, buffer);
		return list;
	}

}
