package example.voltvalvemod.block.interfaces

import example.voltvalvemod.block.custom.PowerGrid

interface PowerNetworkPart {
    var powerGrid: PowerGrid?
    fun isOn(): Boolean
}