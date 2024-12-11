package example.voltvalvemod.block

interface Reciever {
    fun setVoltage(): Double

    fun getResistance(): Double

    fun isOn(): Boolean
}