package example.voltvalvemod.block.interfaces

interface Transmitter {
    fun getCurrent(): Double
    fun setCurrent(): Double

    fun getVoltage(): Double
    fun setVoltage(): Double

    fun getResistance(): Double

    fun isFunctional(): Boolean
}