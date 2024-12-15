package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Reciever
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestRecieverEntity(pPos: BlockPos, pBlockState: BlockState):
    BlockEntity(ModBlockEntities.TEST_RECIEVER_BE.get(), pPos, pBlockState),
    Reciever {

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
            field?.removeReciever(this)
            field = value
            field?.updateReciever(this)
        }

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