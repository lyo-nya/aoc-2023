import kotlin.math.abs

data class Hole(val x: Long, val y: Long) {
    fun next(direction: String, steps: Long): Hole {
        return when (direction) {
            "R" -> Hole(x + steps, y)
            "L" -> Hole(x - steps, y)
            "U" -> Hole(x, y - steps)
            "D" -> Hole(x, y + steps)
            else -> this
        }
    }
}

fun dig(moves: List<Pair<String, Long>>): Long {
    var area: Long = 0
    var currentHole = Hole(0, 0)
    moves.forEach {
        val nextHole = currentHole.next(it.first, it.second)
        area += calcArea(currentHole, nextHole)
        currentHole = nextHole
    }
    return area / 2 + 1
}


fun calcArea(firstHole: Hole, secondHole: Hole): Long {
    val areaPart = firstHole.x * secondHole.y - firstHole.y * secondHole.x
    val perimeterPart = abs(firstHole.x - secondHole.x) + abs(firstHole.y - secondHole.y)
    return (areaPart + perimeterPart)
}

fun readMoveInstructionPart2(instruction: String): Pair<String, Long> {
    val hex = instruction.split(" ").last().trim('(', ')', '#')
    val direction = when (hex.last()) {
        '0' -> "R"
        '1' -> "D"
        '2' -> "L"
        '3' -> "U"
        else -> null
    }
    return direction!! to hex.dropLast(1).toLong(radix = 16)

}


fun main() {
    fun part1(input: List<String>): Long {
        val moves = input.map { move -> move.split(" ").dropLast(1).let { it[0] to it[1].toLong() } }
        return dig(moves)
    }

    fun part2(input: List<String>): Long {
        val moves = input.map { move -> readMoveInstructionPart2(move) }
        return dig(moves)
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}