package example.voltvalvemod.block

import example.voltvalvemod.block.interfaces.Generator
import example.voltvalvemod.block.interfaces.Reciever
import example.voltvalvemod.block.interfaces.Transmitter

class PowerGrid {
    var generators: MutableMap<Generator, Long> = mutableMapOf()
    var recievers: MutableMap<Reciever, Long> = mutableMapOf()
    var transmitters: MutableSet<Transmitter> = mutableSetOf()

    var generatedPower: Long = 0
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
    }

    fun removeTransmitter(transmitter: Transmitter) {
        transmitters.remove(transmitter)

        transmitters.forEach {
            it.powerGrid = null
        }

        transmitters.forEach {
            if (it.powerGrid == null) {
                it.rebuildNetwork()
            }
        }
    }

    fun updateNetwork() {

    }
}