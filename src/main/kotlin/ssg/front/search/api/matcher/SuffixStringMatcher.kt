package ssg.front.search.api.matcher

class SuffixStringMatcher : TrieStringMatcher {
    constructor(suffixes: Array<String>) : super() {
        for (i in suffixes.indices)
            addPatternBackward(suffixes[i])
    }

    constructor(suffixes: Collection<*>) : super() {
        val iter = suffixes.iterator()
        while (iter.hasNext())
            addPatternBackward(iter.next() as String)
    }

    override fun matches(input: String): Boolean {
        var node: TrieStringMatcher.TrieNode? = root
        for (i in input.length - 1 downTo 0) {
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
        for (i in input.length - 1 downTo 0) {
            node = node!!.getChild(input[i])
            if (node == null)
                return null
            if (node.isTerminal())
                return input.substring(i)
        }
        return null
    }

    override fun longestMatch(input: String): String? {
        var node: TrieStringMatcher.TrieNode? = root
        var result: String? = null
        for (i in input.length - 1 downTo 0) {
            node = node!!.getChild(input[i])
            if (node == null)
                break
            if (node.isTerminal())
                result = input.substring(i)
        }
        return result
    }
}
