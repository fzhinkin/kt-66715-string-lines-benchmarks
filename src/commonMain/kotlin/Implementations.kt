package org.example

// copied from stdlib as is
private class DelimitedRangesSequence(
    private val input: CharSequence,
    private val startIndex: Int,
    private val limit: Int,
    private val getNextMatch: CharSequence.(currentIndex: Int) -> Pair<Int, Int>?
) : Sequence<IntRange> {

    override fun iterator(): Iterator<IntRange> = object : Iterator<IntRange> {
        var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
        var currentStartIndex: Int = startIndex.coerceIn(0, input.length)
        var nextSearchIndex: Int = currentStartIndex
        var nextItem: IntRange? = null
        var counter: Int = 0

        private fun calcNext() {
            if (nextSearchIndex < 0) {
                nextState = 0
                nextItem = null
            } else {
                if (limit > 0 && ++counter >= limit || nextSearchIndex > input.length) {
                    nextItem = currentStartIndex..input.lastIndex
                    nextSearchIndex = -1
                } else {
                    val match = input.getNextMatch(nextSearchIndex)
                    if (match == null) {
                        nextItem = currentStartIndex..input.lastIndex
                        nextSearchIndex = -1
                    } else {
                        val (index, length) = match
                        nextItem = currentStartIndex until index
                        currentStartIndex = index + length
                        nextSearchIndex = currentStartIndex + if (length == 0) 1 else 0
                    }
                }
                nextState = 1
            }
        }

        override fun next(): IntRange {
            if (nextState == -1)
                calcNext()
            if (nextState == 0)
                throw NoSuchElementException()
            val result = nextItem as IntRange
            // Clean next to avoid keeping reference on yielded instance
            nextItem = null
            nextState = -1
            return result
        }

        override fun hasNext(): Boolean {
            if (nextState == -1)
                calcNext()
            return nextState == 1
        }
    }
}

private class LinesIterator(private val string: CharSequence) : Iterator<String> {
    private var state: Int = 0 // states 0: unknown, 1: hasNext, 2: !hasNext
    private var tokenStartIndex: Int = 0
    private var delimiterStartIndex: Int = 0
    private var delimiterLength: Int = 0 // serves as both a delimiter length and an end-of-input marker (with value < 0)

    override fun hasNext(): Boolean {
        if (state == 0) {
            if (delimiterLength < 0) {
                state = 2
                return false
            }
            state = 1
            var idx = tokenStartIndex
            delimiterLength = -1
            delimiterStartIndex = string.length
            while (idx < string.length) {
                val c = string[idx]
                if (c == '\n' || c == '\r') {
                    delimiterLength = if (c == '\r' && idx + 1 < string.length && string[idx + 1] == '\n') {
                        2
                    } else {
                        1
                    }
                    delimiterStartIndex = idx
                    break
                }
                idx++
            }

            return true
        }
        return state == 1
    }

    override fun next(): String {
        if (!hasNext()) throw NoSuchElementException()
        state = 0
        val lastIndex = delimiterStartIndex
        val firstIndex = tokenStartIndex
        tokenStartIndex = delimiterStartIndex + delimiterLength
        return string.substring(firstIndex, lastIndex)
    }
}

public fun CharSequence.lineSequenceCustomIterator(): Sequence<String> = object : Sequence<String> {
    override fun iterator(): Iterator<String> = LinesIterator(this@lineSequenceCustomIterator)
}

public fun CharSequence.lineSequenceCustomFind(): Sequence<String> {
    return DelimitedRangesSequence(this, 0, 0, { currentIndex ->
        var nextIndex = currentIndex
        var result: Pair<Int, Int>? = null
        while (nextIndex < length) {
            val c = this[nextIndex]
            if (c == '\n' || c == '\r') {
                val l = if (c == '\r' && nextIndex + 1 < length && this[nextIndex + 1] == '\n') 2 else 1
                result = nextIndex to l
                break
            }
            nextIndex++
        }
        result
    }).map { substring(it) }
}