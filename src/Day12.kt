fun main() {
    fun checkValid(mapRow: String, damagedComp: List<Long>): Boolean {
        return mapRow.trim('.').split(".").filter { it.isNotEmpty() }.map { it.length.toLong() }.toList() == damagedComp
    }

    fun getAllCombinations(mapRow: String): List<String> {
        var stringsToCheck = mutableListOf(mapRow)
        for (i in 1..mapRow.count { it == '?' }) {
            val stringsToAdd = mutableListOf<String>()
            for (s in stringsToCheck) {
                stringsToAdd.add(s.replaceFirst("?", "."))
                stringsToAdd.add(s.replaceFirst("?", "#"))
            }
            stringsToCheck = stringsToAdd
        }
        return stringsToCheck.filter { !it.contains("?") }.map { it.replace("""[.]+""".toRegex(), ".") }
    }

    fun part1(input: List<String>): Long {
        var result = 0L
        for (row in input) {
            val (mapRow, damagedComp) = row.split(" ")
            val damagedCompLong = damagedComp.split(",").map { it.toLong() }
            result += getAllCombinations(mapRow).count {
                checkValid(it, damagedCompLong)
            }.toLong()
        }
        return result
    }

    val input = readInput("Day12")
    part1(input).println()
}
