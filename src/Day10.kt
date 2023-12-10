fun Char.opposite(): Char {
    return when (this) {
        'l' -> 'r'
        'r' -> 'l'
        'u' -> 'd'
        'd' -> 'u'
        else -> this
    }
}

fun Char.toDirection(): String {
    return when (this) {
        '|' -> "ud"
        '-' -> "lr"
        'L' -> "ur"
        'J' -> "ul"
        '7' -> "dl"
        'F' -> "dr"
        'S' -> "S"
        else -> "."
    }
}


class Grid(input: List<String>) {
    private val description: List<List<String>> = input.map { row -> row.toCharArray().map { it.toDirection() } }
    private val start: Pair<Int, Int> =
        input.first { it.contains('S') }.indexOf('S') to input.indexOfFirst { it.contains("S") }
    private val descriptionDim = description.first().size to description.size

    fun findLoop(): List<Pair<Int, Int>> {
        val loops = mutableListOf<List<Pair<Int, Int>>>()
        for (d in listOf('r', 'l', 'u', 'd')) {
            loops.add(traverseOneDirection(d))
        }
        return loops.maxBy { it.size }
    }

    private fun traverseOneDirection(firstTurn: Char): MutableList<Pair<Int, Int>> {
        var direction: Char = firstTurn
        var newStart: Pair<Int, Int>
        var newCoordinate: Pair<Int, Int> = start
        val loop = mutableListOf<Pair<Int, Int>>()
        var head: String
        do {
            newStart = newCoordinate
            newCoordinate = turn(direction, newStart)
            loop.add(newCoordinate)
            head = description[newCoordinate.second][newCoordinate.first]
            direction = head.first { it != direction.opposite() }
        } while (newStart != newCoordinate && !head.endsWith("S") && !head.endsWith("."))
        return loop
    }


    private fun turn(direction: Char, currentCoordinate: Pair<Int, Int>): Pair<Int, Int> {
        val newCoordinate = when (direction) {
            'l' -> currentCoordinate.first - 1 to currentCoordinate.second
            'r' -> currentCoordinate.first + 1 to currentCoordinate.second
            'u' -> currentCoordinate.first to currentCoordinate.second - 1
            'd' -> currentCoordinate.first to currentCoordinate.second + 1
            else -> currentCoordinate
        }
        if (newCoordinate.first !in 0..descriptionDim.first || newCoordinate.second !in 0..descriptionDim.second) {
            return currentCoordinate
        }
        return newCoordinate

    }

    private fun isDotWithin(dot: Pair<Int, Int>, boundaries: List<Pair<Int, Int>>): Boolean {
        return when {
            boundaries.count {
                it.second == dot.second && it.first > dot.first && description[it.second][it.first] !in listOf(
                    "ur",
                    "ul",
                    "lr",
                    "S"
                )
            } % 2 == 1 -> true

            else -> false
        }

    }

    fun dotsEnclosed(): List<Pair<Int, Int>> {
        val largestLoop = findLoop()
        return description.flatMapIndexed { i, row ->
            row.mapIndexed { j, value -> j to value }
                .filter { it.second == "." || !largestLoop.contains(it.first to i) }.map { it.first to i }
        }.filter { dot -> isDotWithin(dot, largestLoop) }
    }

}


fun main() {

    fun part1(input: List<String>): Int {
        return Grid(input).findLoop().size / 2

    }

    fun part2(input: List<String>): Int {
        return Grid(input).dotsEnclosed().size
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
