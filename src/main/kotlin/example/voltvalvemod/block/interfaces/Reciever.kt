package example.voltvalvemod.block.interfaces

interface Reciever: PowerNetworkPart {
    // For network to know how much power this reciever needs
    fun getPowerRequest(): Long

    // Used by network to provide power to reciever
    fun providePower(amount: Long)
}