fun rotate(content: List<String>): MutableList<String> {
    val stringLength = content[0].length
    val newContent = mutableListOf<String>()
    for (j in stringLength - 1 downTo 0) {
        newContent.add(newContent.size, content.map { it[j] }.joinToString(""))
    }
    return newContent
}

fun rollRocks(state: List<String>): List<String> {
    val newContent = mutableListOf<String>()
    for (column in state) {
        var newColumn = column
        do {
            newColumn = newColumn.replace(".O", "O.")
        } while (newColumn.contains(".O"))
        newContent.add(newColumn)
    }
    return newContent
}

fun doCycle(state: List<String>): List<String> {
    var newContent = state
    repeat(4) {
        newContent = rotate(rollRocks(newContent))
    }
    return newContent
}

fun calcPressure(column: String): Int {
    return column.mapIndexed { i, c -> if (c == 'O') column.length - i else 0 }.sum()
}


fun main() {

    fun part1(input: List<String>): Int {
        val content = transpose(input.map { it.toList() }).map { it.joinToString("") }
        return rollRocks(content).map { calcPressure(it) }.sum()
    }

    fun part2(input: List<String>): Int {
        val content = transpose(input.map { it.toList() }).map { it.joinToString("") }
        val knownStates = mutableListOf<List<String>>()
        var newContent: List<String> = content
        val maxIterations = 1000000000
        for (i in 0 until maxIterations) {
            newContent = doCycle(newContent)
            if (newContent in knownStates) {
                break
            }
            knownStates.add(knownStates.size, newContent)
        }
        val stepsLeft = maxIterations - knownStates.size - 1
        val resultingLoop = knownStates.dropWhile { it != newContent }
        val resultingIndex = stepsLeft % resultingLoop.size
        val resultingState = resultingLoop[resultingIndex]
        return resultingState.map { calcPressure(it) }.sum()

    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}