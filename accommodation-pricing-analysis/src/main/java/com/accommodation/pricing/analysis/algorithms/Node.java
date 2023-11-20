package com.accommodation.pricing.analysis.algorithms;

import java.util.HashMap;
import java.util.Map;

public class Node {
	
	Map<Character,Node> childs;
	char character;
	boolean isWord;
	
	public Node() {
		childs = new HashMap<>();
	}
	
	public Node(char character) {
		this.character=character;
		childs =  new HashMap<>();
	}
	
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
