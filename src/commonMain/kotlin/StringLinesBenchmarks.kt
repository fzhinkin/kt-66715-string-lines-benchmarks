package org.example

import kotlinx.benchmark.*

val text = listOf(
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "Sed velit turpis, eleifend quis dictum sit amet, consequat in massa.",
    "Sed ante elit, viverra in suscipit in, tincidunt suscipit mi.",
    "Nunc eget lectus dapibus nunc fringilla viverra a sed eros.",
    "Maecenas laoreet erat vitae nunc finibus imperdiet nec id risus.",
    "Maecenas suscipit velit vitae odio sollicitudin, quis commodo risus rhoncus.",
    "Etiam hendrerit porta leo, ut faucibus risus convallis nec.",
    "Morbi ullamcorper risus eget eleifend eleifend.",
    "Cras iaculis orci malesuada nisl auctor, nec luctus arcu placerat.",
    "Donec ultricies nunc non pharetra laoreet.",
    "Praesent pharetra metus consequat, pulvinar tellus rutrum, vestibulum dui.",
    "Nam semper justo eu lorem tristique, vitae fringilla dui ornare.",
    "Praesent purus nibh, rutrum id nibh ac, commodo pellentesque purus.",
    "Proin ac eros ipsum.",
    "Maecenas euismod porttitor orci non hendrerit.",
    "Suspendisse euismod tortor at libero convallis gravida."
)

@State(Scope.Benchmark)
open class EmptyStringLineSequenceBenchmark {
    private val string = ""

    @Benchmark
    fun noop(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence())
    }

    @Benchmark
    fun takeFirst(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().first())
    }

    @Benchmark
    fun takeLast(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().last())
    }

    @Benchmark
    fun count(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().count())
    }

    @Benchmark
    fun takeAll(blackhole: Blackhole) {
        string.lineSequence().forEach { blackhole.consume(it) }
    }
}

@State(Scope.Benchmark)
open class StringLineSequenceBenchmark {
    @Param("LF", "CRLF", "NONE")
    var delimitersStyle: String = "LF"

    private var string: String = ""

    @Setup
    fun generateText() {
        when (delimitersStyle) {
            "LF" -> string = text.joinToString("\n")
            "CRLF" -> string = text.joinToString("\r\n")
            "NONE" -> string = text.joinToString("")
            else -> throw IllegalArgumentException("Unknown delimitersStyle=$delimitersStyle")
        }
    }

    @Benchmark
    fun noop(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence())
    }

    @Benchmark
    fun takeFirst(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().first())
    }

    @Benchmark
    fun takeLast(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().last())
    }

    @Benchmark
    fun count(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().count())
    }

    @Benchmark
    fun takeAll(blackhole: Blackhole) {
        string.lineSequence().forEach { blackhole.consume(it) }
    }
}