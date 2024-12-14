package example.voltvalvemod.block.interfaces

interface Transmitter: PowerNetworkPart {
    fun updateState()
    fun rebuildNetwork()
}