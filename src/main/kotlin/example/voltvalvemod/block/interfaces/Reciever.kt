package example.voltvalvemod.block.interfaces

interface Reciever: PowerNetworkPart {
    fun requestPower(amount: Long): Long
}