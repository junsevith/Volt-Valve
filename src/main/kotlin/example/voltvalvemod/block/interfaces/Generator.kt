package example.voltvalvemod.block.interfaces

interface Generator: PowerNetworkPart {
    fun providePower(): Long
}