import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    fun calcDistance(maxAllowedTime: Long, recordDistance: Long): Long {
        val discriminant = maxAllowedTime * maxAllowedTime - 4 * (recordDistance + 1)
        val x1 = ceil((-maxAllowedTime - sqrt(discriminant.toDouble())) / -2).toLong()
        val x2 = floor((-maxAllowedTime + sqrt(discriminant.toDouble())) / -2).toLong()
        return abs(x2 - x1 + 1)

    }

    fun part1(input: List<String>): Long {
        val (times, distances) = input.map {
            it.replace("""[\s]+""".toRegex(), " ").split(": ").last().trim().split(" ").map { numberString ->
                numberString.toLong()
            }
        }
        var result = 1L
        (times zip distances).forEach {
            result *= calcDistance(it.first, it.second)
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input.map {
            it.replace("""[\s]+""".toRegex(), "").split(":").last().toLong()
        }
        return calcDistance(time, distance)
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
