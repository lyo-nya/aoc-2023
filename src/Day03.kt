data class Input(val row: Int, val colStart: Int, val colEnd: Int, val value: Int)
data class StarCoords(val row: Int, val col: Int)

fun main() {

    fun getNumbers(row: String, index: Int): List<Input> {
        val expr = Regex("""[\d]*""")
        val res = expr.findAll(row)
        return res.filter { it.value != "" }.map {
            Input(index, it.range.first, it.range.last, it.value.toInt())
        }.toList()
    }

    fun getStars(row: String, index: Int): List<StarCoords> {
        val expr = Regex("""[^\d\.]""")
        val res = expr.findAll(row)
        return res.filter { it.value != "" }.map {
            StarCoords(index, it.range.start)
        }.toList()
    }

    fun cartesianDistSquared(x1: Int, x2: Int, y1: Int, y2: Int): Int {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)
    }


    fun starSurround(star: StarCoords, inputs: List<Input>): List<Input> {
        return inputs.filter {
            cartesianDistSquared(it.row, star.row, it.colStart, star.col) <= 2
                    || cartesianDistSquared(it.row, star.row, it.colEnd, star.col) <= 2
        }

    }

    fun starSurroundWithRatio(star: StarCoords, inputs: List<Input>): Int {
        val surroundingInputs = starSurround(star, inputs)
        if (surroundingInputs.size == 2) {
            return surroundingInputs.let { it.first().value * it.last().value }
        } else {
            return 0
        }

    }

    fun part1(input: List<String>): Int {
        val numbers = input.flatMapIndexed { i, row -> getNumbers(row, i) }
        val stars = input.flatMapIndexed { i, row -> getStars(row, i) }
        return stars.flatMap { starSurround(it, numbers) }.toSet().sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.flatMapIndexed { i, row -> getNumbers(row, i) }
        val stars = input.flatMapIndexed { i, row -> getStars(row, i) }
        return stars.sumOf { starSurroundWithRatio(it, numbers) }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
