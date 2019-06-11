package ssg.front.search.api.matcher

class PrefixStringMatcher : TrieStringMatcher {

    constructor(prefixes: Array<String>) : super() {
        for (i in prefixes.indices)
            addPatternForward(prefixes[i])
    }

    constructor(prefixes: Collection<*>) : super() {
        val iter = prefixes.iterator()
        while (iter.hasNext())
            addPatternForward(iter.next() as String)
    }

    override fun matches(input: String): Boolean {
        var node: TrieStringMatcher.TrieNode? = root
        for (i in 0 until input.length) {
            node = node!!.getChild(input[i])
            if (node == null)
                return false
            if (node.isTerminal())
                return true
        }
        return false
    }

    override fun shortestMatch(input: String): String? {
        var node: TrieStringMatcher.TrieNode? = root
        for (i in 0 until input.length) {
            node = node!!.getChild(input[i])
            if (node == null)
                return null
            if (node.isTerminal())
                return input.substring(0, i + 1)
        }
        return null
    }

    override fun longestMatch(input: String): String? {
        var node: TrieStringMatcher.TrieNode? = root
        var result: String? = null
        for (i in 0 until input.length) {
            node = node!!.getChild(input[i])
            if (node == null)
                break
            if (node.isTerminal())
                result = input.substring(0, i + 1)
        }
        return result
    }
}