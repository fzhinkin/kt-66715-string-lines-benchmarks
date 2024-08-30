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
abstract class StringLineSequenceBenchmark {
    @Param("LF", "CRLF", "NONE", "EMPTY")
    var delimitersStyle: String = "LF"

    protected var string: String = ""

    @Setup
    fun generateText() {
        when (delimitersStyle) {
            "LF" -> string = text.joinToString("\n")
            "CRLF" -> string = text.joinToString("\r\n")
            "NONE" -> string = text.joinToString("")
            "EMPTY" -> string = ""
            else -> throw IllegalArgumentException("Unknown delimitersStyle=$delimitersStyle")
        }
    }
}

@State(Scope.Benchmark)
open class NoOpBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence())
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomFind())
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomIterator())
    }
}

@State(Scope.Benchmark)
open class TakeFirstBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().first())
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomFind().first())
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomIterator().first())
    }
}

@State(Scope.Benchmark)
open class TakeLastBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().last())
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomFind().last())
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomIterator().last())
    }
}

@State(Scope.Benchmark)
open class CountBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        blackhole.consume(string.lineSequence().count())
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomFind().count())
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomIterator().count())
    }
}

@State(Scope.Benchmark)
open class TakeAllBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        string.lineSequence().forEach { blackhole.consume(it) }
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        string.lineSequenceCustomFind().forEach { blackhole.consume(it) }
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        string.lineSequenceCustomIterator().forEach { blackhole.consume(it) }
    }
}

@State(Scope.Benchmark)
open class LinesBenchmark : StringLineSequenceBenchmark() {
    @Benchmark
    fun baseline(blackhole: Blackhole) {
        blackhole.consume(string.lines())
    }

    @Benchmark
    fun customFind(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomFind().toList())
    }

    @Benchmark
    fun customIterator(blackhole: Blackhole) {
        blackhole.consume(string.lineSequenceCustomIterator().toList())
    }
}