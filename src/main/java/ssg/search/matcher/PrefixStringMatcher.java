package ssg.search.matcher;

import java.util.Collection;
import java.util.Iterator;

public class PrefixStringMatcher extends TrieStringMatcher{

	/**
	 * Creates a new <code>PrefixStringMatcher</code> which will match
	 * <code>String</code>s with any prefixVo in the supplied array. Zero-length
	 * <code>Strings</code> are ignored.
	 */
	public PrefixStringMatcher(String[] prefixes) {
		super();
		for(int i = 0; i < prefixes.length; i++)
			addPatternForward(prefixes[i]);
	}

	/**
	 * Creates a new <code>PrefixStringMatcher</code> which will match
	 * <code>String</code>s with any prefixVo in the supplied
	 * <code>Collection</code>.
	 *
	 * @throws ClassCastException
	 *             if any <code>Object</code>s in the collection are not
	 *             <code>String</code>s
	 */
	public PrefixStringMatcher(Collection prefixes) {
		super();
		Iterator iter = prefixes.iterator();
		while(iter.hasNext())
			addPatternForward((String) iter.next());
	}

	/**
	 * Returns true if the given <code>String</code> is matched by a prefixVo in
	 * the trie
	 */
	public boolean matches(String input){
		TrieNode node = root;
		for(int i = 0; i < input.length(); i++){
			node = node.getChild(input.charAt(i));
			if(node == null)
				return false;
			if(node.isTerminal())
				return true;
		}
		return false;
	}

	/**
	 * Returns the shortest prefixVo of <code>input<code> that is matched, or
	 * <code>null<code> if no match exists.
	 */
	public String shortestMatch(String input){
		TrieNode node = root;
		for(int i = 0; i < input.length(); i++){
			node = node.getChild(input.charAt(i));
			if(node == null)
				return null;
			if(node.isTerminal())
				return input.substring(0, i + 1);
		}
		return null;
	}

	/**
	 * Returns the longest prefixVo of <code>input<code> that is matched, or
	 * <code>null<code> if no match exists.
	 */
	public String longestMatch(String input){
		TrieNode node = root;
		String result = null;
		for(int i = 0; i < input.length(); i++){
			node = node.getChild(input.charAt(i));
			if(node == null)
				break;
			if(node.isTerminal())
				result = input.substring(0, i + 1);
		}
		return result;
	}
}