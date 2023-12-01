package com.accommodation.pricing.analysis.algorithms;

import java.util.HashMap;
import java.util.Map;


/**
 * Represents a node in a Trie data structure.
 * 
 * @author sachreet kaur(110122441)
 */

public class Node {
	// Map to store child nodes, where the character is the key and the corresponding node is the value.
	Map<Character,Node> childs;
	
	// The character that is linked to the current node.
	char character;
	
	// Indicates whether the node is at the end of a word.
	boolean isWord;
	
	/**
	 * A Trie node's default constructor. Initializes the map of child nodes.
	 * 
	 *  @author sachreet kaur(110122441)
	 */
	public Node() {
		childs = new HashMap<>();
	}
	
	/**
	 * Constructs a Trie node with a given character. Initializes the map of child nodes. 
	 *
	 * @param character The character associated with the node.
	 * 
	 *  @author sachreet kaur(110122441)
	 */
	public Node(char character) {
		this.character=character;
		childs =  new HashMap<>();
	}
	
	/**
	 * Starts from the current node and inserts a word into the Trie.
	 *
	 * @param searchWord The word to be inserted into the Trie.
	 * 
	 * @author sachreet kaur(110122441)
	 */
	public void insertNode(String searchWord) {
		if(searchWord == null || searchWord.isEmpty())
			return;
		char firstCharacter = searchWord.charAt(0);
		Node childNode = childs.get(firstCharacter);
		if(childNode == null) {
			childNode = new Node(firstCharacter);
			childs.put(firstCharacter, childNode);
		}
		
		if(searchWord.length()>1) 
			childNode.insertNode(searchWord.substring(1));
		else
			childNode.isWord = true;
	}
}
