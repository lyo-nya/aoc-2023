fun main() {

    fun diff(input: MutableList<Long>): MutableList<Long> {
        val result = mutableListOf<Long>()
        (0..input.size - 2).forEach {
            result.add(input[it + 1] - input[it])
        }
        return result
    }

    fun predict(input: MutableList<Long>): Long {
        val scheme = mutableListOf<MutableList<Long>>()
        scheme.add(input)
        while (!scheme.last().all { it == 0L }) {
            scheme.add(diff(scheme.last()))
        }
        scheme.last().add(0)
        (scheme.size - 2 downTo 0).forEach {
            scheme[it].add(scheme[it + 1].last() + scheme[it].last())
        }
        return scheme.first().last()
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { reportRow ->
            predict(reportRow.split(" ").map { it.toLong() }.toMutableList())
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { reportRow ->
            predict(reportRow.split(" ").map { it.toLong() }.reversed().toMutableList())
        }
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
