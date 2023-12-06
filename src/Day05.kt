class Seed(val id: Long) {
    var soil: Long = id
    var fertilizer: Long = id
    var water: Long = id
    var light: Long = id
    var temperature: Long = id
    var humidity: Long = id
    var location: Long = id

    fun setValue(to: Long, type: String) {
        when (type) {
            "soil" -> soil = to
            "fertilizer" -> fertilizer = to
            "water" -> water = to
            "light" -> light = to
            "temperature" -> temperature = to
            "humidity" -> humidity = to
            "location" -> location = to
        }
    }

    fun getParent(type: String): Long {
        return when (type) {
            "fertilizer" -> soil
            "water" -> fertilizer
            "light" -> water
            "temperature" -> light
            "humidity" -> temperature
            "location" -> humidity
            else -> id
        }
    }


    fun applyMapping(mapping: Mapping): Seed {
        val parent = this.getParent(mapping.type)
        this.setValue(parent, mapping.type)
        mapping.maps.forEach { map ->
            if (parent in map[1]..(map[1] + map[2])) {
                this.setValue(parent - map[1] + map[0], mapping.type)
            }
        }
        return this
    }

}

class Mapping(data: String) {
    val type: String
    val maps: List<List<Long>>

    init {
        val rows = data.split("\n")
        type = rows.first().replace(" ", "-").split("-")[2]
        maps = rows.drop(1).map { it.split(" ").map { it.toLong() } }
    }
}


fun main() {

    fun getSeedsPart1(input: String): Sequence<Seed> {
        val inputs = input.replace("seeds: ", "").trim().split(" ").map { it.toLong() }
        return sequence {
            inputs.forEach {
                yield(Seed(it))
            }
        }
    }

    fun getSeedsPart2(input: String): Sequence<Seed> {
        val inputs = input.replace("seeds: ", "").trim().split(" ").map { it.toLong() }
        return sequence {
            for (i in 0 until inputs.size step 2) {
                for (seed in inputs[i]..inputs[i] + inputs[i + 1] - 1) {
                    yield(Seed(seed))
                }
            }
        }
    }

    fun resolve(maps: Sequence<Mapping>, seeds: Sequence<Seed>): Long {
        var newSeeds = seeds
        maps.forEach { map ->
            newSeeds = newSeeds.map { it.applyMapping(map) }
        }
        return newSeeds.minOf {
            it.location
        }

    }


    fun getMaps(input: List<String>): Sequence<Mapping> {
        return sequence {
            input.forEach {
                yield(Mapping(it))
            }
        }
    }

    fun part1(input: List<String>): Long {
        val seeds = getSeedsPart1(input[0])
        val maps = getMaps(input.drop(1))
        return resolve(maps, seeds)

    }

    fun part2(input: List<String>): Long {
        val seeds = getSeedsPart2(input[0])
        val maps = getMaps(input.drop(1))
        return resolve(maps, seeds)
    }

    val input = readInput("Day05").joinToString("\n").split("\n\n")
    part1(input).println()
    part2(input).println()
}
