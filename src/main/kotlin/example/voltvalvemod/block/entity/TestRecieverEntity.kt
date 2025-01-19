package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.ElectricFurnaceBlock
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.custom.TestReciever
import example.voltvalvemod.block.interfaces.Reciever
import net.minecraft.core.BlockPos
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestRecieverEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.TEST_RECIEVER_BE.get(), pPos, pBlockState),
    Reciever {

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
//            if (field != value) {
//                field?.removeReciever(this)
//            }
            field = value
            field?.updateReciever(this)
        }

    private var providedPower: Long = 0

    private var efficiencyPercentage: Double = 0.0

    override fun getPowerRequest(): Long {
        return 15
    }

    override fun providePower(amount: Long) {
        providedPower = amount
        efficiencyPercentage = (100 * providedPower).toDouble() / getPowerRequest()
        val state = level?.getBlockState(worldPosition)
        if (state != null) {
            level?.setBlock(worldPosition, state.setValue(TestReciever.BRIGHTNESS, providedPower.toInt()), Block.UPDATE_ALL)
        }



    }

    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeReciever(this)
    }

    fun brightness(): Int {
        return providedPower.toInt()
    }
}