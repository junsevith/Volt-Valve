package example.voltvalvemod.block.interfaces

import example.voltvalvemod.block.PowerGridState

interface Transmitter {
    fun updateState()
    fun getPowerGrid(): PowerGridState
}