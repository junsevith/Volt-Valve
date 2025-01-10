package example.voltvalvemod.block.interfaces

import example.voltvalvemod.block.custom.PowerGrid

interface PowerNetworkPart {

    // Power grid this block is connected to
    var powerGrid: PowerGrid?

    // Is block active
    fun isOn(): Boolean

    // Must be called when block is destroyed, should disconnect same block from network
    fun disconnect()
}