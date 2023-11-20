package com.accommodation.pricing.analysis.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Trie {
	
	Node rootNode;
	
	public Trie(List<String> words) {
		rootNode = new Node();
		for(String word:words) {
			rootNode.insertNode(word);
		}
	}
	
	public boolean findNode(String prefix,boolean position) {
		Node lastNode = rootNode;
		for(char character : prefix.toCharArray()) {
			lastNode = lastNode.childs.get(character);
			if(lastNode==null)
				return false;
		}
		return !position || lastNode.isWord;
	}
	
	public boolean find(String prefix) {
		return findNode(prefix, false);
	}
	
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
	
//	public static void main(String []args) {
//		List<String> words = List.of("hello", "dog", "hell", "cat", "a");
//		Trie trie = new Trie(words);
//		System.out.println(trie.getSuggestion("h"));
//	}
}
