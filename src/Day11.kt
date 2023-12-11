import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Pair<Long, Long>.distTo(other: Pair<Long, Long>): Long {
    val xDiff = abs(this.first - other.first)
    val yDiff = abs(this.second - other.second)
    val minDiff = min(xDiff, yDiff)
    val maxDiff = max(xDiff, yDiff)
    return minDiff + maxDiff
}


fun main() {

    fun getPoints(input: List<String>, speed: Long): List<Pair<Long, Long>> {
        val points = mutableListOf<Pair<Long, Long>>()
        var row = 0L
        val columnsNotMet = (0..input.first().length).toMutableSet()
        var columns: List<Int>
        input.forEach {
            columns = it.mapIndexed { index, c -> if (c == '#') index else -1 }.filter { it != -1 }
            columnsNotMet.removeAll(columns)
            if (columns.isEmpty()) {
                row += (speed - 1)
            }
            columns.forEach { column ->
                points.add(row to column.toLong())
            }
            row++
        }
        return points.map {
            it.first to it.second + (columnsNotMet.filter { c -> c < it.second }.size) * (speed - 1)
        }
    }

    fun solution(input: List<String>, speed: Long): Long {
        val points = getPoints(input, speed)
        var result = 0L
        points.forEachIndexed { i, point ->
            points.forEachIndexed { j, other ->
                if (i < j) {
                    result += point.distTo(other)
                }
            }
        }
        return result

    }

    fun part1(input: List<String>): Long {
        return solution(input, 2L)
    }

    fun part2(input: List<String>): Long {
        return solution(input, 1000000L)
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
