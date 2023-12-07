class CamelCard(val bid: Long) {
    private var combination: String = ""


    fun setCombination(hand: String) {
        val counts = hand.map { char -> char to hand.count { it == char } }.toMap()
        val nPairs = counts.count { it.value == 2 }
        combination = when {
            counts.maxOf { it.value } == 5 -> "6"
            counts.maxOf { it.value } == 4 -> "5"
            counts.containsValue(3) && counts.containsValue(2) -> "4"
            counts.maxOf { it.value } == 3 -> "3"
            nPairs == 2 -> "2"
            nPairs == 1 -> "1"
            else -> "0"
        }

    }

    fun setHand(hand: String, replacements: Map<String, String>) {
        combination += hand
        replacements.forEach {
            combination = combination.replace(it.key, it.value)
        }

    }

    operator fun compareTo(otherCard: CamelCard): Int {
        return this.combination.compareTo(otherCard.combination)
    }
}


class CamelDeck {
    private val cards: MutableList<CamelCard> = mutableListOf()
    fun add(card: CamelCard) {
        val idx = cards.indexOfFirst { it > card }
        cards.add(if (idx > -1) idx else cards.size, card)
    }

    fun getValue(): Long {
        var total = 0L
        cards.forEachIndexed { idx, card ->
            total += (idx + 1) * card.bid
        }
        return total
    }

}

fun main() {

    fun part1(input: List<String>): Long {
        val deck = CamelDeck()
        val labelReplacements = mapOf(
            "A" to "Z", "K" to "Y", "Q" to "X", "J" to "W", "T" to "V"
        )
        input.forEach {
            val (hand, bid) = it.split(" ")
            val card = CamelCard(bid.toLong())
            card.setCombination(hand)
            card.setHand(hand, labelReplacements)
            deck.add(card)
        }
        return deck.getValue()
    }

    fun part2(input: List<String>): Long {
        val deck = CamelDeck()
        val labelReplacements = mapOf(
            "A" to "Z", "K" to "Y", "Q" to "X", "J" to "0", "T" to "V"
        )
        input.forEach { row ->
            val (hand, bid) = row.split(" ")
            val mostFrequentChar = hand.filter { it != 'J' }.maxByOrNull { c -> hand.count { it == c } } ?: 'J'
            val fakeHand = hand.replace("J", mostFrequentChar.toString())
            val card = CamelCard(bid.toLong())
            card.setCombination(fakeHand)
            card.setHand(hand, labelReplacements)
            deck.add(card)
        }
        return deck.getValue()
    }

    check(part1(readInput("Day07test")) == 6440L)
    check(part1(readInput("Day07")) == 250898830L)
    check(part2(readInput("Day07test")) == 5905L)
    check(part2(readInput("Day07")) == 252127335L)
}
