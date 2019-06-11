package ssg.search.matcher;

import java.util.Collection;
import java.util.Iterator;

public class SuffixStringMatcher extends TrieStringMatcher{

	/**
	 * Creates a new <code>PrefixStringMatcher</code> which will match
	 * <code>String</code>s with any suffix in the supplied array.
	 */
	public SuffixStringMatcher(String[] suffixes) {
		super();
		for(int i = 0; i < suffixes.length; i++)
			addPatternBackward(suffixes[i]);
	}

	/**
	 * Creates a new <code>PrefixStringMatcher</code> which will match
	 * <code>String</code>s with any suffix in the supplied
	 * <code>Collection</code>
	 */
	public SuffixStringMatcher(Collection suffixes) {
		super();
		Iterator iter = suffixes.iterator();
		while(iter.hasNext())
			addPatternBackward((String) iter.next());
	}

	/**
	 * Returns true if the given <code>String</code> is matched by a suffix in
	 * the trie
	 */
	public boolean matches(String input){
		TrieNode node = root;
		for(int i = input.length() - 1; i >= 0; i--){
			node = node.getChild(input.charAt(i));
			if(node == null)
				return false;
			if(node.isTerminal())
				return true;
		}
		return false;
	}

	/**
	 * Returns the shortest suffix of <code>input<code> that is matched, or
	 * <code>null<code> if no match exists.
	 */
	public String shortestMatch(String input){
		TrieNode node = root;
		for(int i = input.length() - 1; i >= 0; i--){
			node = node.getChild(input.charAt(i));
			if(node == null)
				return null;
			if(node.isTerminal())
				return input.substring(i);
		}
		return null;
	}

	/**
	 * Returns the longest suffix of <code>input<code> that is matched, or
	 * <code>null<code> if no match exists.
	 */
	public String longestMatch(String input){
		TrieNode node = root;
		String result = null;
		for(int i = input.length() - 1; i >= 0; i--){
			node = node.getChild(input.charAt(i));
			if(node == null)
				break;
			if(node.isTerminal())
				result = input.substring(i);
		}
		return result;
	}
}
