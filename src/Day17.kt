import kotlin.math.max
import kotlin.math.min

interface Moves {
    val left: Int
    val right: Int
    val up: Int
    val down: Int
    fun nextMoves(direction: String): Moves?
}

data class SimpleMoves(override val left: Int, override val right: Int, override val up: Int, override val down: Int) :
    Moves {
    override fun nextMoves(direction: String): Moves? {
        return when {
            direction == "down" && up > 0 -> null
            direction == "up" && down > 0 -> null
            direction == "right" && left > 0 -> null
            direction == "left" && right > 0 -> null
            direction == "left" && left < 3 -> SimpleMoves(left + 1, 0, 0, 0)
            direction == "right" && right < 3 -> SimpleMoves(0, right + 1, 0, 0)
            direction == "up" && up < 3 -> SimpleMoves(0, 0, up + 1, 0)
            direction == "down" && down < 3 -> SimpleMoves(0, 0, 0, down + 1)
            else -> null
        }
    }
}

data class UltraMoves(override val left: Int, override val right: Int, override val up: Int, override val down: Int) :
    Moves {
    override fun nextMoves(direction: String): Moves? {
        return when {
            direction == "down" && up > 0 -> null
            direction == "up" && down > 0 -> null
            direction == "right" && left > 0 -> null
            direction == "left" && right > 0 -> null
            direction == "left" && left < 10 -> UltraMoves(max(left + 1, 4), 0, 0, 0)
            direction == "right" && right < 10 -> UltraMoves(0, max(right + 1, 4), 0, 0)
            direction == "up" && up < 10 -> UltraMoves(0, 0, max(up + 1, 4), 0)
            direction == "down" && down < 10 -> UltraMoves(0, 0, 0, max(down + 1, 4))
            else -> null
        }
    }
}

data class FacilityNode(val x: Int, val y: Int, val moves: Moves) {
    fun getAllNeighbours(): List<FacilityNode> {
        return listOfNotNull(
            getNeighbour("right"),
            getNeighbour("down"),
            getNeighbour("left"),
            getNeighbour("up"),
        )
    }

    private fun getNeighbour(direction: String): FacilityNode? {
        val nextMoves = moves.nextMoves(direction) ?: return null
        return when (direction) {
            "right" -> FacilityNode(x + (nextMoves.right - moves.right), y, nextMoves)
            "left" -> FacilityNode(x - (nextMoves.left - moves.left), y, nextMoves)
            "up" -> FacilityNode(x, y - (nextMoves.up - moves.up), nextMoves)
            "down" -> FacilityNode(x, y + (nextMoves.down - moves.down), nextMoves)
            else -> null
        }
    }

}

class Facility(input: List<String>) {
    private val matrix = input.map { row -> row.toList().map { value -> value.digitToInt() } }
    private val nodes = mutableMapOf<FacilityNode, Long>()
    private val visitedNodes = mutableMapOf<FacilityNode, Long>()

    fun findMin(startingNode: FacilityNode): Long {
        nodes[startingNode] = 0L
        do {
            val currentNode = nodes.minBy { it.value }
            step(currentNode)
        } while (nodes.isNotEmpty())
        return visitedNodes.filter { it.key.y == matrix.size - 1 && it.key.x == matrix.first().size - 1 }
            .minOf { it.value }
    }

    private fun isNodeValid(node: FacilityNode): Boolean {
        val inMatrixCondition = node.x in matrix.first().indices && node.y in matrix.indices
        val notVisitedCondition = node !in visitedNodes
        return inMatrixCondition && notVisitedCondition
    }

    private fun getDistance(firstNode: FacilityNode, secondNode: FacilityNode): Int {
        val xRanges = (firstNode.x - 1 downTo secondNode.x) + (firstNode.x + 1..secondNode.x)
        val yRanges = (firstNode.y - 1 downTo secondNode.y) + (firstNode.y + 1..secondNode.y)
        return xRanges.sumOf { matrix[firstNode.y][it] } + yRanges.sumOf { matrix[it][firstNode.x] }
    }

    private fun step(currentNode: Map.Entry<FacilityNode, Long>) {
        nodes.remove(currentNode.key)
        currentNode.key.getAllNeighbours().filter { isNodeValid(it) }.forEach { node ->
            val oldValue = nodes.getOrDefault(node, Long.MAX_VALUE)
            val newValue = currentNode.value + getDistance(currentNode.key, node)
            nodes[node] = min(oldValue, newValue)
        }
        visitedNodes[currentNode.key] = currentNode.value
    }
}


fun main() {
    fun part1(input: List<String>): Long {
        val facility = Facility(input)
        val startingNode = FacilityNode(0, 0, SimpleMoves(0, 0, 0, 0))
        return facility.findMin(startingNode)
    }

    fun part2(input: List<String>): Long {
        val facility = Facility(input)
        val startingNode = FacilityNode(0, 0, UltraMoves(0, 0, 0, 0))
        return facility.findMin(startingNode)
    }

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
