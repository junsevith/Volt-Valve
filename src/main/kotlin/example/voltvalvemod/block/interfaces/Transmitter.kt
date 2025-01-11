package example.voltvalvemod.block.interfaces

interface Transmitter : PowerNetworkPart {
    // Signal transmitter to rescan its surroundings and rebuild network
    fun rebuildNetwork()
}