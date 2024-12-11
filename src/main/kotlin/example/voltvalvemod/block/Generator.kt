package example.voltvalvemod.block

interface Generator {
    fun getVoltage(): Double
    fun getInnerResistance(): Double
}