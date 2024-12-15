package example.voltvalvemod.block.custom

import example.voltvalvemod.block.interfaces.Reciever
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class TestReciever :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    Reciever {

    override var powerGrid: PowerGrid? = null

    private var providedPower: Long = 0

    private var efficiencyPercentage: Double = 0.0

    override fun getPowerRequest(): Long {
        return 10
    }

    override fun providePower(amount: Long) {
        providedPower = amount
        efficiencyPercentage = (100 * providedPower).toDouble() / getPowerRequest()
    }

    override fun isOn(): Boolean {
        return true
    }

}