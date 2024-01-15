enum class Pulse { LOW, HIGH, NONE }

interface Module {
    fun transformSignal(sourceNode: String, signal: Pulse): Pulse
}

data class FlipFlop(val id: String, var on: Boolean = false) : Module {
    override fun transformSignal(sourceNode: String, signal: Pulse): Pulse {
        if (signal == Pulse.LOW) {
            on = !on
            return if (on) Pulse.HIGH else Pulse.LOW
        }
        return Pulse.NONE
    }

}

data class Conjunction(val id: String, val previousPulses: MutableMap<String, Pulse>) : Module {
    override fun transformSignal(sourceNode: String, signal: Pulse): Pulse {
        previousPulses[sourceNode] = signal
        return if (previousPulses.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
    }
}

data class Broadcast(val id: String) : Module {
    override fun transformSignal(sourceNode: String, signal: Pulse): Pulse {
        return signal
    }
}

data class TestingModule(val id: String) : Module {
    override fun transformSignal(sourceNode: String, signal: Pulse): Pulse {
        return Pulse.NONE
    }
}

data class Flow(val source: String, val sink: String, val signal: Pulse)


class ModulesConfiguration(description: List<String>) {
    private val wires = getWires(description)
    private val modules = parseWires(wires)
    private val wiresCleaned = cleanWires(wires)
    private val callStack = mutableListOf<Flow>()
    private var lowSignals = 0L
    private var highSignals = 0L
    private val multipliers = mutableMapOf("nd" to 0L, "pc" to 0L, "vd" to 0L, "tx" to 0L)
    private var presses = 0L


    fun pressUntilDone(): Long {
        do {
            pressButton(1)
        } while (multipliers.any { it.value == 0L })
        return leastCommonMultiple(multipliers.values.toList())
    }

    fun pressButton(n: Int): Long {
        presses += n
        repeat(n) {
            callStack.add(0, Flow("button", "broadcaster", Pulse.LOW))
            while (callStack.isNotEmpty()) {
                call()
            }
        }
        return lowSignals * highSignals
    }

    private fun call() {
        val command = callStack.removeAt(0)
        if (command.source in multipliers.keys && command.signal == Pulse.HIGH && command.sink == "hf") multipliers[command.source] =
            presses
        val sinks = wiresCleaned.filter { it.first == command.sink }.map { it.second }
        when (command.signal) {
            Pulse.LOW -> lowSignals += 1
            Pulse.HIGH -> highSignals += 1
            Pulse.NONE -> {}
        }
        val processedSignal = modules[command.sink]!!.transformSignal(command.source, command.signal)
        if (processedSignal != Pulse.NONE) callStack.addAll(callStack.size,
            sinks.map { Flow(command.sink, it, processedSignal) })
    }

    private fun cleanWires(wires: List<Pair<String, String>>): List<Pair<String, String>> {
        return wires.map { it.first.replace("[&%]".toRegex(), "") to it.second }
    }

    private fun getWires(description: List<String>): List<Pair<String, String>> {
        return description.flatMap { line ->
            val (source, sinks) = line.split(" -> ")
            sinks.split(", ").map { source to it }
        }
    }


    private fun parseWires(wires: List<Pair<String, String>>): Map<String, Module> {
        val flipFlops = wires.filter { it.first.startsWith("%") }.map {
            val name = it.first.drop(1)
            name to FlipFlop(name)
        }
        val conjunctions = wires.filter { it.first.startsWith("&") }.map { conjunction ->
            val name = conjunction.first.drop(1)
            conjunction.first.drop(1) to Conjunction(name,
                wires.filter { it.second == name }.map { if (it.first != "broadcaster") it.first.drop(1) else it.first }
                    .associateWith { Pulse.LOW }.toMutableMap()
            )
        }
        val testingModules = wires.filter { wire -> wire.second !in wires.map { it.first.drop(1) } }
            .map { it.second to TestingModule(it.second) }.distinctBy { it.first }
        return (flipFlops + conjunctions + testingModules + listOf("broadcaster" to Broadcast("broadcaster"))).toMap()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val modulesConfiguration = ModulesConfiguration(input)
        return modulesConfiguration.pressButton(1000)
    }

    fun part2(input: List<String>): Long {
        val modulesConfiguration = ModulesConfiguration(input)
        return modulesConfiguration.pressUntilDone()
    }

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}