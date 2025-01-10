package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestGeneratorEntity(pPos: BlockPos, pBlockState: BlockState):
    BlockEntity(ModBlockEntities.TEST_GENERATOR_BE.get(), pPos, pBlockState),
    Generator {

    override fun providePower(): Long {
        return 10
    }

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
            field?.removeGenerator(this)
            field = value
            field?.updateGenerator(this)
        }


    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeGenerator(this)
    }
}