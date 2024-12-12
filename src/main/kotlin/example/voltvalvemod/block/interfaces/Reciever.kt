package example.voltvalvemod.block.interfaces

interface Reciever {
    fun setVoltage(): Double

    fun getResistance(): Double

    fun isOn(): Boolean
}