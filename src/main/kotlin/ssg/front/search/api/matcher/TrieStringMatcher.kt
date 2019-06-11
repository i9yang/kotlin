package ssg.front.search.api.matcher

import java.util.*

abstract class TrieStringMatcher protected constructor() {
    protected var root: TrieNode

    init {
        this.root = TrieNode('\u0000', false)
    }

    protected inner class TrieNode : Comparable<Any> {
        protected var children: Array<TrieNode>? = null
        protected var childrenList: LinkedList<TrieNode>? = null
        protected var nodeChar: Char = ' '
        protected var terminal: Boolean = false

        constructor(nodeChar: Char, isTerminal: Boolean) {
            this.nodeChar = nodeChar
            this.terminal = isTerminal
            this.childrenList = LinkedList()
        }

        fun isTerminal(): Boolean {
            return terminal
        }

        fun getChildAddIfNotPresent(nextChar: Char, isTerminal: Boolean): TrieNode {
            if (childrenList == null) {
                childrenList = LinkedList(Arrays.asList<TrieNode>(*children!!))
                children = arrayOf()
            }
            if (childrenList!!.size == 0) {
                val newNode = TrieNode(nextChar, isTerminal)
                childrenList!!.add(newNode)
                return newNode
            }
            val iter = childrenList!!.listIterator()
            var node = iter.next()
            while (node.nodeChar < nextChar && iter.hasNext())
                node = iter.next()
            if (node.nodeChar == nextChar) {
                node.terminal = node.terminal or isTerminal
                return node
            }

            if (node.nodeChar > nextChar)
                iter.previous()
            val newNode = TrieNode(nextChar, isTerminal)
            iter.add(newNode)
            return newNode
        }

        fun getChild(nextChar: Char): TrieNode? {
            if (children == null) {
                children = childrenList!!.toTypedArray()
                childrenList = null
                Arrays.sort(children)
            }

            var min = 0
            var max = children!!.size - 1
            var mid = 0
            while (min < max) {
                mid = (min + max) / 2
                if (children!![mid].nodeChar == nextChar)
                    return children!![mid]
                if (children!![mid].nodeChar < nextChar)
                    min = mid + 1
                else
                // if (children[mid].nodeChar > nextChar)
                    max = mid - 1
            }
            if (min == max)
                if (children!![min].nodeChar == nextChar)
                    return children!![min]

            return null
        }

        override fun compareTo(o: Any): Int {
            val other = o as TrieNode
            if (this.nodeChar < other.nodeChar)
                return -1
            return if (this.nodeChar == other.nodeChar) 0 else 1
        }
    }

    protected fun matchChar(node: TrieNode, s: String, idx: Int): TrieNode? {
        return node.getChild(s[idx])
    }

    protected fun addPatternForward(s: String) {
        var node = root
        val stop = s.length - 1
        var i: Int
        if (s.length > 0) {
            i = 0
            while (i < stop) {
                node = node.getChildAddIfNotPresent(s[i], false)
                i++
            }
            node = node.getChildAddIfNotPresent(s[i], true)
        }
    }

    protected fun addPatternBackward(s: String) {
        var node = root
        if (s.length > 0) {
            for (i in s.length - 1 downTo 1)
                node = node.getChildAddIfNotPresent(s[i], false)
            node = node.getChildAddIfNotPresent(s[0], true)
        }
    }

    abstract fun matches(input: String): Boolean

    abstract fun shortestMatch(input: String): String?

    abstract fun longestMatch(input: String): String?
}
