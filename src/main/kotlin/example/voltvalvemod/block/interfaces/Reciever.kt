package example.voltvalvemod.block.interfaces

interface Reciever: PowerNetworkPart {
    fun getPowerRequest(): Long
    fun providePower(amount: Long)
}