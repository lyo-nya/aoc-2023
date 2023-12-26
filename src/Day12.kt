fun main() {
    fun checkValid(mapRow: String, damagedComp: List<Long>): Boolean {
        return mapRow.trim('.').split(".").filter { it.isNotEmpty() }.map { it.length.toLong() }.toList() == damagedComp
    }

    fun checkValidSubpattern(mapRow: String, damagedComp: List<Long>): Boolean {
        val subpattern = mapRow.trim('.').split(".").filter { it.isNotEmpty() }.map { it.length.toLong() }.toList()
        if (subpattern.size > damagedComp.size) {
            return false
        }
        return subpattern.dropLast(1) == damagedComp.slice(0 until subpattern.size - 1)
    }

    fun getAllCombinations(mapRow: String, damagedComp: List<Long>): Map<String, Long> {
        val stringsToCheck = mutableMapOf(mapRow to 1L)
        while (stringsToCheck.keys.any { it.contains("?") }) {
            val originalString = stringsToCheck.keys.first { it.contains("?") }
            val originalCount = stringsToCheck.remove(originalString)
            for (newChar in listOf(".", "#")) {
                val newString = originalString.replaceFirst("?", newChar).replace("[.]+".toRegex(), ".")
                if (checkValidSubpattern(newString.split('?').first(), damagedComp)) {
                    stringsToCheck[newString] = stringsToCheck.getOrDefault(newString, 0L) + originalCount!!
                }
            }
        }
        return stringsToCheck
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { row ->
            val (mapRow, damagedComp) = row.split(" ")
            val damagedCompLong = damagedComp.split(",").map { it.toLong() }
            getAllCombinations(mapRow, damagedCompLong).filter { combination ->
                checkValid(combination.key, damagedCompLong)
            }.map { it.value }.sum()
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { row ->
            val (mapRow, damagedComp) = row.split(" ")
            val damagedCompLong = ("$damagedComp,").repeat(5).dropLast(1).split(",").map { it.toLong() }
            getAllCombinations("$mapRow?".repeat(5).dropLast(1), damagedCompLong).filter { combination ->
                checkValid(combination.key, damagedCompLong)
            }.map { it.value }.sum()
        }
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
