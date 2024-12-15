package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.interfaces.Generator
import example.voltvalvemod.block.interfaces.Reciever
import example.voltvalvemod.block.interfaces.Transmitter

class PowerGrid {
    var generators: MutableMap<Generator, Long> = mutableMapOf()
    var recievers: MutableMap<Reciever, Long> = mutableMapOf()
    var transmitters: MutableSet<Transmitter> = mutableSetOf()

    var generatedPower: Long = 0
    var suppliedPower: Long = 0
    var requestedPower: Long = 0

    fun updateGenerator(generator: Generator) {
        val providedPower = generator.providePower()
        val oldPower = generators[generator] ?: 0
        generators[generator] = providedPower
        generatedPower += providedPower - oldPower
        updateNetwork()
    }

    fun removeGenerator(generator: Generator) {
        val oldPower = generators[generator] ?: 0
        generators.remove(generator)
        generatedPower -= oldPower
        updateNetwork()
    }

    fun updateReciever(reciever: Reciever) {
        val requestedPower = reciever.getPowerRequest()
        val oldPower = recievers[reciever] ?: 0
        recievers[reciever] = requestedPower
        this.requestedPower += requestedPower - oldPower
        updateNetwork()
    }

    fun removeReciever(reciever: Reciever) {
        val oldPower = recievers[reciever] ?: 0
        recievers.remove(reciever)
        requestedPower -= oldPower
        updateNetwork()
    }

    fun addTransmitter(transmitter: Transmitter) {
        transmitters.add(transmitter)
        updateNetwork()
    }

    fun removeTransmitter(transmitter: Transmitter) {
        transmitters.remove(transmitter)

        generators.forEach {
            it.key.powerGrid = null
        }

        transmitters.forEach {
            it.powerGrid = null
        }

        recievers.forEach {
            it.key.powerGrid = null
        }

        transmitters.forEach {
            if (it.powerGrid == null) {
                it.rebuildNetwork()
            }
        }

        updateNetwork()
    }

    fun updateNetwork() {

        if (recievers.isNotEmpty()) {
            var uniform = generatedPower / recievers.size
            var usedpower = 0L
            var done = 0

            val sortedRecievers = recievers.toList()
                .sortedBy { (_key, value) -> value }

            sortedRecievers.forEach { (reciever, requestedPower) ->
                val sentPower = requestedPower.coerceAtMost(uniform)
                reciever.providePower(sentPower)
                usedpower += sentPower
                done++
                uniform = (generatedPower - usedpower) / (recievers.size - done)
            }
        }

        VoltValveMod.LOGGER.info("Transmitters: ${transmitters.size}, Recievers: ${recievers.size}, Generators: ${generators.size}")

    }
}