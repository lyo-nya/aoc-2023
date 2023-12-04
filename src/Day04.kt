import java.lang.Math.pow

class Card(row: String) {
    private val values: Set<String>
    private val winners: Set<String>
    var count: Int

    init {
        val (winnersString, valuesString) = row.replace("""[\s]+""".toRegex(), " ").split(": ").last().split(" | ")
        values = valuesString.split(" ").toSet()
        winners = winnersString.split(" ").toSet()
        count = 1
    }

    fun getValue(): Int {
        val nWinners = getSimpleValue().toDouble()
        if (nWinners == 0.0) {
            return 0
        } else {
            return pow(2.0, nWinners - 1).toInt()
        }
    }

    fun getSimpleValue(): Int {
        return values.intersect(winners).size
    }
}


class CardPile {
    private val pile = mutableListOf<Card>()
    private fun add(card: Card) {
        pile.add(card)
    }

    private fun getCardCount(): Int {
        return pile.sumOf {
            it.count
        }
    }

    private fun process() {
        pile.forEachIndexed { index, card ->
            (index + 1..index + card.getSimpleValue()).forEach {
                pile[it].count += 1 * card.count
            }

        }

    }

    private fun inflate(inputs: List<String>) {
        inputs.forEach {
            add(Card(it))
        }

    }

    fun resolve(inputs: List<String>): Int {
        inflate(inputs)
        process()
        return getCardCount()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf {
            Card(it).getValue()
        }
    }

    fun part2(input: List<String>): Int {
        return CardPile().resolve(input)
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
