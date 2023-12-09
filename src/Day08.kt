fun leastCommonMultiple(numbers: List<Long>): Long {
    return numbers.reduce { acc, x ->
        val largestNum = maxOf(acc, x)
        var lcm = largestNum
        val maxLcm = acc * x
        while (lcm <= maxLcm) {
            if (lcm % x == 0L) {
                break
            }
            lcm += largestNum
        }
        lcm
    }
}

data class Node(val label: String, val left: String, val right: String)


class Tree {
    private val nodes = mutableMapOf<String, Node>()
    fun add(node: Node) {
        nodes[node.label] = node
    }

    private fun treeStep(nodeLabel: String, direction: Char): String {
        val currentNode = nodes[nodeLabel]
        val nextNodeLabel = if (direction == 'L') currentNode?.left else currentNode?.right
        return nextNodeLabel ?: nodeLabel
    }

    private fun findDist(nodeLabel: String, directions: String, pattern: String = "Z"): Pair<String, Long> {
        var newNodeLabel = nodeLabel
        var dPointer = 0
        var steps = 0L
        do {
            newNodeLabel = treeStep(newNodeLabel, directions[dPointer])
            if (dPointer == directions.length - 1) {
                dPointer = 0
            } else {
                dPointer += 1
            }
            steps += 1
        } while (!newNodeLabel.endsWith(pattern))
        return newNodeLabel to steps
    }

    fun treeWalk(nodeLabel: String, directions: String): Long {
        val (_, steps) = findDist(nodeLabel, directions, "ZZZ")
        return steps
    }


    fun simultaneousTreeWalk(directions: String): Long {
        val distToZ =
            nodes.keys.filter { it.endsWith('A') || it.endsWith('Z') }.associateWith { findDist(it, directions) }
        val closestZNodes = nodes.keys.filter { it.endsWith('A') }.map { distToZ[it]!! }
        var closestNextZNodes = closestZNodes
        var localMax: Long
        do {
            localMax = closestNextZNodes.maxOf { it.second }
            closestNextZNodes = closestNextZNodes.map {
                distToZ[it.first]!!
            }
            if (closestNextZNodes == closestZNodes) {
                return leastCommonMultiple(closestZNodes.map { it.second }.toList())
            }
            closestNextZNodes = closestNextZNodes.map {
                if (it.second < localMax) {
                    it.first to it.second + it.second
                } else {
                    it
                }
            }
        } while (closestZNodes.map { it.second }.toSet().size != 1)
        return localMax
    }

}


fun main() {

    fun readInput(input: List<String>): Pair<Tree, String> {
        val turns = input[0].trim()
        val tree = Tree()
        input.drop(2).forEach {
            val (label, left, right) = it.replace("""[,()=\s]+""".toRegex(), " ").split(" ")
            tree.add(Node(label, left, right))
        }
        return tree to turns
    }

    fun part1(input: List<String>): Long {
        val (tree, turns) = readInput(input)
        return tree.treeWalk("AAA", turns)
    }

    fun part2(input: List<String>): Long {
        val (tree, turns) = readInput(input)
        return tree.simultaneousTreeWalk(turns)
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}