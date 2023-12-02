fun main() {
    fun readRounds(roundsString: String): MutableMap<String, Int> {
        val requirements = mutableMapOf(
            "red" to 0, "green" to 0, "blue" to 0
        )
        roundsString.split(";").forEach { round ->
            round.split(",").forEach { cubes ->
                val (value, key) = cubes.trim().split(" ")
                val intValue = value.toInt()
                if (requirements.get(key)!! < intValue) {
                    requirements[key] = intValue
                }
            }
        }
        return requirements
    }


    fun checkValid(row: String, constraint: Map<String, Int>): Int {
        val (game, rounds) = row.split(":")
        val roundsData = readRounds(rounds)
        val gameId = game.filter { it.isDigit() }.toInt()
        roundsData.forEach { (key, value) ->
            if (constraint[key]!! < value) {
                return 0
            }
        }
        return gameId
    }

    fun calcPower(row: String): Int {
        val (_, rounds) = row.split(":")
        val roundsData = readRounds(rounds)
        var prod = 1
        roundsData.values.forEach {
            prod *= it
        }
        return prod
    }


    fun part1(input: List<String>): Int {
        val constraint = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        return input.sumOf {
            checkValid(it, constraint)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { calcPower(it) }
    }

    val input = readInput("Day02")

    part1(input).println()
    part2(input).println()
}
