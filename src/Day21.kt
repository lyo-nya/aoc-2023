class Garden(input: List<String>) {
    data class Move(val x: Int, val y: Int, val movesLeft: Int)

    private val matrix = input.map { it.toList() }
    private val movesStack = mutableListOf<Move>()
    private val finalNodes = mutableSetOf<Move>()
    private val performedMoves = mutableSetOf<Move>()

    fun traverse(moves: Int): Int {
        val start = matrix.indexOfFirst { it.contains('S') }
            .let { row -> Move(row, matrix[row].indexOfFirst { it == 'S' }, moves) }
        movesStack.add(start)
        while (movesStack.isNotEmpty()) {
            step(movesStack.removeAt(0))
        }
        return finalNodes.size
    }

    private fun step(move: Move) {
        if (move.x in matrix.indices && move.y in matrix.first().indices && matrix[move.x][move.y] != '#') {
            if (move.movesLeft != 0) {
                val nextMoves = listOf(
                    Move(move.x + 1, move.y, move.movesLeft - 1),
                    Move(move.x - 1, move.y, move.movesLeft - 1),
                    Move(move.x, move.y + 1, move.movesLeft - 1),
                    Move(move.x, move.y - 1, move.movesLeft - 1)
                )
                movesStack.addAll(movesStack.size, nextMoves.filter { it !in performedMoves })
                performedMoves.addAll(nextMoves)
            } else {
                finalNodes.add(move)
            }
        }
    }

    fun traverseInfinite(moves: Int): Int {
        val start = matrix.indexOfFirst { it.contains('S') }
            .let { row -> Move(row, matrix[row].indexOfFirst { it == 'S' }, moves) }
        movesStack.add(start)
        while (movesStack.isNotEmpty()) {
            stepInfinite(movesStack.first())
            movesStack.size.println()
        }
        return finalNodes.size
    }

    private fun stepInfinite(move: Move) {
        movesStack.removeAll { it == move }
        if (matrix[(matrix.size + (move.x % matrix.size)) % matrix.size][(100 * matrix.first().size + (move.y % matrix.first().size)) % matrix.first().size] != '#') {
            if (move.movesLeft != 0) {
                val nextMoves = listOf(
                    Move(move.x + 1, move.y, move.movesLeft - 1),
                    Move(move.x - 1, move.y, move.movesLeft - 1),
                    Move(move.x, move.y + 1, move.movesLeft - 1),
                    Move(move.x, move.y - 1, move.movesLeft - 1)
                )
                movesStack.addAll(0, nextMoves.filter { it !in performedMoves })
                performedMoves.addAll(nextMoves)
            } else {
                finalNodes.add(move)
            }

        }

    }
}


fun main() {
    fun part1(input: List<String>): Int {
        return Garden(input).traverse(64)
    }

    val input = readInput("Day21")
    part1(input).println()
}