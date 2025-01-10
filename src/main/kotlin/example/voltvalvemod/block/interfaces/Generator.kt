package example.voltvalvemod.block.interfaces

interface Generator: PowerNetworkPart {
    // For network to know how much power this generator can provide
    fun providePower(): Long
}