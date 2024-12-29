package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SocketBlockEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.SOCKET_BE.get(), pPos, pBlockState), Generator {

    override var powerGrid: PowerGrid? = null
        set(value) {
            field?.removeGenerator(this)
            field = value
            field?.updateGenerator(this)
        }


    override fun providePower(): Long {
//        TODO("not implemented yet")
        return 10
    }

    override fun isOn(): Boolean {
        return true
    }


}