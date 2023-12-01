fun main() {
    fun extractInt(string: String): Int {
        return string.toCharArray().filter { it.isDigit() }.let {
            it.slice(listOf(0, it.size - 1)).joinToString("").toInt()
        }
    }

    fun replaceDigitNames(string: String): String {
        var newString: String = string
        val digitsMap = mapOf(
            "zero" to "0",
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
        )
        // it.key + it.value + it.key should keep all the necessary letters in place, while not changing the resulting order of digits
        digitsMap.forEach {
            newString = newString.replace(it.key, it.key + it.value + it.key)
        }
        return newString
    }


    fun part1(input: List<String>): Int {
        return input.sumOf { extractInt(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { extractInt(replaceDigitNames(it)) }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
