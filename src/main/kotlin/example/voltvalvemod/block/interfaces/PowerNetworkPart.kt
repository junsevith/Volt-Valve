package example.voltvalvemod.block.interfaces

import example.voltvalvemod.block.PowerGridState

interface PowerNetworkPart {
    fun isOn(): Boolean
    fun connectPowerGrid(state: PowerGridState)
}