import kotlin.math.max

class Field(input: List<String>) {
    private val matrix = input.map { it.toMutableList() }.toMutableList()
    private val active = MutableList(matrix.size) { MutableList(matrix.first().size) { 0 } }
    private val starts = mutableListOf<Pair<Pair<Int, Int>, String>>()
    private val startsVisited = mutableSetOf<Pair<Pair<Int, Int>, String>>()


    fun part1(): Int {
        starts.add(0 to -1 to "right")
        traverse()
        return active.sumOf { it.sum() }

    }

    private fun cleanStart() {
        startsVisited.clear()
        for (i in 0 until active.size) {
            for (j in 0 until active.first().size) {
                active[i][j] = 0
            }
        }

    }

    fun part2(): Int {
        var maxEnergy = 0
        for (i in 0 until matrix.size) {
            cleanStart()
            starts.add(i to -1 to "right")
            traverse()
            maxEnergy = max(maxEnergy, active.map { it.sum() }.sum())
        }
        for (i in 0 until matrix.size) {
            cleanStart()
            starts.add(i to matrix.first().size to "left")
            traverse()
            maxEnergy = max(maxEnergy, active.map { it.sum() }.sum())
        }
        for (i in 0 until matrix.first().size) {
            cleanStart()
            starts.add(-1 to i to "down")
            traverse()
            maxEnergy = max(maxEnergy, active.map { it.sum() }.sum())
        }
        for (i in 0 until matrix.first().size) {
            cleanStart()
            starts.add(matrix.size to i to "up")
            traverse()
            maxEnergy = max(maxEnergy, active.map { it.sum() }.sum())
        }
        return maxEnergy
    }

    private fun traverse() {
        do {
            for (i in 0 until starts.size) {
                val start = starts.removeAt(0)
                startsVisited.add(start)
                step(start.first, start.second)
            }
        } while (starts.size > 0)
    }

    private fun step(position: Pair<Int, Int>, direction: String) {
        val nextPosition = findNextPosition(position, direction)
        if (nextPosition.first in 0 until matrix.size && nextPosition.second in 0 until matrix.first().size) {
            active[nextPosition.first][nextPosition.second] = 1
            val nextPositionValue = matrix[nextPosition.first][nextPosition.second]
            val nextDirections = getNextDirections(nextPositionValue, direction)
            for (d in nextDirections) {
                if (!startsVisited.contains(nextPosition.first to nextPosition.second to d)) {
                    starts.add(starts.size, nextPosition.first to nextPosition.second to d)
                }
            }
        }
    }

    private fun getNextDirections(value: Char, direction: String): List<String> {
        return when {
            value == '-' && direction in listOf("left", "right") -> listOf(direction)
            value == '-' && direction in listOf("up", "down") -> listOf("left", "right")
            value == '|' && direction in listOf("left", "right") -> listOf("up", "down")
            value == '|' && direction in listOf("up", "down") -> listOf(direction)
            value == '/' && direction == "right" -> listOf("up")
            value == '/' && direction == "left" -> listOf("down")
            value == '/' && direction == "down" -> listOf("left")
            value == '/' && direction == "up" -> listOf("right")
            value == '\\' && direction == "right" -> listOf("down")
            value == '\\' && direction == "up" -> listOf("left")
            value == '\\' && direction == "left" -> listOf("up")
            value == '\\' && direction == "down" -> listOf("right")
            else -> listOf(direction)
        }
    }

    private fun findNextPosition(position: Pair<Int, Int>, direction: String): Pair<Int, Int> {
        return when (direction) {
            "right" -> position.first to position.second + 1
            "down" -> position.first + 1 to position.second
            "left" -> position.first to position.second - 1
            "up" -> position.first - 1 to position.second
            else -> position
        }

    }
}

fun main() {
    val input = readInput("Day16")
    Field(input).part1().println()
    Field(input).part2().println()
}