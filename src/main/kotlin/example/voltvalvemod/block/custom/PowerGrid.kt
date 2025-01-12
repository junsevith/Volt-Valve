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
//    var suppliedPower: Long = 0
    var requestedPower: Long = 0

    fun updateGenerator(generator: Generator, silent: Boolean = false) {
        val providedPower = generator.providePower()
        val oldPower = generators[generator] ?: 0
        generators[generator] = providedPower
        generatedPower += providedPower - oldPower
        updateNetwork(silent)
    }

    fun removeGenerator(generator: Generator, silent: Boolean = false) {
        val oldPower = generators[generator] ?: 0
        generators.remove(generator)
        generatedPower -= oldPower
        updateNetwork(silent)
    }

    fun updateReciever(reciever: Reciever, silent: Boolean = false) {
        val requestedPower = reciever.getPowerRequest()
        val oldPower = recievers[reciever] ?: 0
        recievers[reciever] = requestedPower
        this.requestedPower += requestedPower - oldPower
        updateNetwork(silent)
    }

    fun removeReciever(reciever: Reciever, silent: Boolean = false) {
        val oldPower = recievers[reciever] ?: 0
        recievers.remove(reciever)
        requestedPower -= oldPower
        updateNetwork(silent)
    }

    fun addTransmitter(transmitter: Transmitter, silent: Boolean = false) {
        transmitters.add(transmitter)
        updateNetwork(silent)
    }

    fun removeTransmitter(transmitter: Transmitter) {
        transmitters.remove(transmitter)

        generators.forEach {
            it.key.powerGrid = null
        }

        transmitters.forEach {
            it.powerGrid = null
        }

        this.generatedPower = 0

        updateNetwork(false)

        recievers.forEach {
            it.key.powerGrid = null
        }




        transmitters.forEach {
//            if (it.powerGrid == null) {
                it.rebuildNetwork()
//            }
        }

//        updateNetwork()
    }

    fun updateNetwork(silent: Boolean) {

        if (recievers.isNotEmpty()) {
            var uniform = generatedPower / recievers.size
            var usedpower = 0L
            var done = 0

            val sortedRecievers = recievers.toList()
                .sortedBy { (_, value) -> value }

            sortedRecievers.forEach { (reciever, requestedPower) ->
                val sentPower = requestedPower.coerceAtMost(uniform)
                reciever.providePower(sentPower)
                usedpower += sentPower
                done++
                if (done < recievers.size) {
                    uniform = (generatedPower - usedpower) / (recievers.size - done)
                }
            }
        }

        if (!silent)
            VoltValveMod.LOGGER.info("Network Update - "+ getStatus())
    }

    fun mergePowerGrids(powerGrid: PowerGrid, silent: Boolean = false) {

        powerGrid.generators.forEach {
            it.key.powerGrid = this
        }

        powerGrid.transmitters.forEach {
            it.powerGrid = this
        }

        powerGrid.recievers.forEach {
            it.key.powerGrid = this
        }

        updateNetwork(silent)
    }

    fun getStatus(): String {
        return "Transmitters: ${transmitters.size}, Recievers: ${recievers.size}, Generators: ${generators.size}, Generated: $generatedPower, Requested: $requestedPower"
    }
}