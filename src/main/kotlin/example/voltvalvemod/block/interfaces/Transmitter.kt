package example.voltvalvemod.block.interfaces

import example.voltvalvemod.block.PowerGridState

interface Transmitter: PowerNetworkPart {
    fun updateState()
    fun getPowerGrid(): PowerGridState
}