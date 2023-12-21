fun transpose(contentMatrix: List<List<Char>>): List<List<Char>> {
    return (0 until contentMatrix.first().size).map { i -> (0 until contentMatrix.size).map { j -> contentMatrix[j][i] } }
}


class Matrix(description: String) {
    private val content = description.split("\n").map { it.toMutableList() }
    private var smudgeFixed = false
    private var matches = 0

    private fun transformMatrix(transposed: Boolean, reversed: Boolean): List<List<Char>> {
        return if (transposed) {
            if (reversed) transpose(content).reversed() else transpose(content)
        } else {
            if (reversed) content.reversed() else content
        }
    }

    fun scanAll(): Int {
        matches = 0
        val transposedContent = transpose(content)
        val byRows = scanRows(content, 0) + scanRows(content.reversed(), content.size)
        val byColumns = scanRows(transposedContent, 0) + scanRows(transposedContent.reversed(), transposedContent.size)
        return byRows * 100 + byColumns
    }

    fun scanAllPart2(): Int {
        val firstScan = scanAll()
        fixSmudge(transposed = false, reversed = false)
        fixSmudge(transposed = true, reversed = false)
        fixSmudge(transposed = false, reversed = true)
        fixSmudge(transposed = true, reversed = true)
        val newScan = scanAll()
        return if (matches > 1) newScan - firstScan else newScan
    }


    private fun transformCoordinates(
        reversed: Boolean, transposed: Boolean, coordinates: Pair<Int, Int>, offset: Int
    ): Pair<Int, Int> {
        val newCoordinates = if (transposed) {
            if (reversed) coordinates.second to offset - coordinates.first else coordinates.second to coordinates.first
        } else {
            if (reversed) offset - coordinates.first to coordinates.second else coordinates.first to coordinates.second
        }
        return newCoordinates

    }

    private fun fixSmudge(transposed: Boolean, reversed: Boolean) {
        if (smudgeFixed) return
        val contentMatrix = transformMatrix(transposed, reversed)
        for (i in 1 until contentMatrix.size / 2 + 1) {
            val upperSlice = contentMatrix.slice(0 until i)
            val lowerSlice = contentMatrix.slice(i until 2 * i).reversed()
            val rowDiffCount = upperSlice.zip(lowerSlice).count { it.first != it.second }
            if (rowDiffCount == 0) {
                continue
            }
            val firstDiff = upperSlice.zip(lowerSlice).first { it.first != it.second }
            val valuesDiffCount = firstDiff.first.zip(firstDiff.second).count { it.first != it.second }
            if (rowDiffCount == 1 && valuesDiffCount == 1) {
                val coordinates = upperSlice.zip(lowerSlice)
                    .indexOfFirst { it.first != it.second } to firstDiff.first.zip(firstDiff.second)
                    .indexOfFirst { it.first != it.second }
                val (x, y) = transformCoordinates(reversed, transposed, coordinates, contentMatrix.size - 1)
                content[x][y] = if (content[x][y] == '.') '#' else '.'
                smudgeFixed = true
            }
        }
    }

    private fun scanRows(contentMatrix: List<List<Char>>, offset: Int): Int {
        val matrixHeight = contentMatrix.size
        var sum = 0
        for (i in 1 until matrixHeight / 2 + 1) {
            val upperSlice = contentMatrix.slice(0 until i)
            val lowerSlice = contentMatrix.slice(i until 2 * i).reversed()
            val rowDiffCount = upperSlice.zip(lowerSlice).count { it.first != it.second }
            if (rowDiffCount == 0) {
                sum += if (offset != 0) (offset - i) else i
                matches++
            }
        }
        return sum
    }

}


fun main() {
    fun part1(input: List<String>): Int {
        val matrices = input.joinToString("\n").split("\n\n").map { Matrix(it) }
        return matrices.sumOf { it.scanAll() }
    }

    fun part2(input: List<String>): Int {
        val matrices = input.joinToString("\n").split("\n\n").map { Matrix(it) }
        return matrices.sumOf { it.scanAllPart2() }

    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}