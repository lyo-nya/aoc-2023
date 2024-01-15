import kotlin.math.max
import kotlin.math.min


fun processParts(partsDescription: String): List<Map<Char, Long>> {
    return partsDescription.split("\n").map { description ->
        description.trim('{', '}').split(",")
            .associate { value -> value.first() to value.filter { it.isDigit() }.toLong() }
    }
}

fun getInstruction(description: String): String {
    return description.split(':', ',')
        .first { !it.contains("<") && !it.contains(">") && it !in listOf("R", "A") && it.isNotEmpty() }
}

fun negateCondition(condition: String): String {
    return when {
        condition.contains(">") -> condition.replace(">", "<=")
        condition.contains("<") -> condition.replace("<", ">=")
        else -> condition
    }
}

fun inflateConditions(steps: String): List<Pair<String, String>> {
    val parsedSteps = steps.split(",").map { step ->
        val sanitizedStep = if (step.any { it.isDigit() }) step else " :$step"
        val (condition, result) = sanitizedStep.split(":")
        condition to result
    }

    return parsedSteps.mapIndexed { idx, step ->
        parsedSteps.take(idx).joinToString(":") {
            negateCondition(it.first)
        } + ":${step.first}:" to step.second
    }
}

fun processInstructions(instructionsDescription: String): List<String> {
    val instructionsMap = instructionsDescription.split("\n").associate {
        val (id, steps) = it.split('{', '}')
        id to inflateConditions(steps)
    }
    val instructions = instructionsMap["in"]!!.toMutableList()
    var currentInstruction = instructions.firstOrNull { it.second !in listOf("R", "A") }
    while (currentInstruction != null) {
        instructions.remove(currentInstruction)
        val newInstruction = getInstruction(currentInstruction.second)
        instructions.addAll(instructions.size,
            instructionsMap[newInstruction]!!.map { "${currentInstruction?.first}:${it.first}" to it.second })
        currentInstruction = instructions.firstOrNull { it.second !in listOf("R", "A") }
    }
    return instructions.map {
        it.first.replace(" ", "").replace(""":+""".toRegex(), ":").trim(':') to it.second
    }.filter { it.second == "A" }.map { it.first }
}

fun findRange(conditions: String): MutableMap<Char, Pair<Long, Long>> {
    val ranges = mutableMapOf<Char, Pair<Long, Long>>(
        'x' to (1L to 4000L), 'm' to (1L to 4000L), 's' to (1L to 4000L), 'a' to (1L to 4000L)
    )
    conditions.split(":").forEach {
        val identifier = it.first()
        val newBound = it.split("<=", ">=", "<", ">").last().toLong()
        val oldBounds = ranges[identifier]!!
        when {
            it.contains("<=") -> ranges[identifier] = oldBounds.first to min(newBound, oldBounds.second)
            it.contains("<") -> ranges[identifier] = oldBounds.first to min(newBound - 1, oldBounds.second)
            it.contains(">=") -> ranges[identifier] = max(newBound, oldBounds.first) to oldBounds.second
            else -> ranges[identifier] = max(newBound + 1, oldBounds.first) to oldBounds.second
        }
    }
    return ranges
}


fun main() {
    fun part1(input: String): Long {
        val (instructions, parts) = input.split("\n\n")
        val ranges = processInstructions(instructions).map { findRange(it) }
        val processedParts = processParts(parts)
        return processedParts.filter { part ->
            ranges.any { range -> range.all { it.value.first <= part[it.key]!! && it.value.second >= part[it.key]!! } }
        }.sumOf { it.values.sum() }

    }

    fun part2(input: String): Long {
        val (instructions, _) = input.split("\n\n")
        return processInstructions(instructions).sumOf { instruction ->
            findRange(instruction).values.map { it.second - it.first + 1 }.reduce { acc, i -> acc * i }
        }
    }


    val input = readInput("Day19").joinToString("\n")
    part1(input).println()
    part2(input).println()

}
