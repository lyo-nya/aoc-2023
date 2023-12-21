fun hash(s: String): Int {
    var h = 0
    for (c in s) {
        h += c.code
        h *= 17
        h %= 256
    }
    return h
}


fun main() {
    fun part1(input: String): Int {
        return input.split(",").sumOf { hash(it) }
    }

    fun part2(input: String): Int {
        val boxes = mutableMapOf<Int, MutableMap<String, Int>>()
        val instructions = input.split(",")
        for (instruction in instructions) {
            if (instruction.contains("-")) {
                val id = hash(instruction.dropLast(1))
                boxes[id]?.remove(instruction.dropLast(1))
            } else {
                val id = hash(instruction.takeWhile { it != '=' })
                boxes[id] = boxes.getOrDefault(id, emptyMap()).toMutableMap()
                boxes[id]?.put(instruction.split("=").first(), instruction.split("=").last().toInt())
            }
        }
        return boxes.map { box ->
            var i = 0
            box.value.map { lens ->
                i++
                lens.value * i * (box.key + 1)
            }.sum()
        }.sum()
    }

    val input = readInput("Day15").joinToString("")
    part1(input).println()
    part2(input).println()
}
