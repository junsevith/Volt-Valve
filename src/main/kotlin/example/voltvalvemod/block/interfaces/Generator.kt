package example.voltvalvemod.block.interfaces

interface Generator {
    fun getVoltage(): Double
    fun getInnerResistance(): Double
}