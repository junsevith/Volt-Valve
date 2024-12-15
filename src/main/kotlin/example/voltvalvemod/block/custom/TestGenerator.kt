package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class TestGenerator :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    Generator {

    override fun providePower(): Long {
        return 10
    }

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }


    override fun isOn(): Boolean {
        return true
    }

    override fun setPlacedBy(
        pLevel: Level,
        pPos: BlockPos,
        pState: BlockState,
        pPlacer: LivingEntity?,
        pStack: ItemStack
    ) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack)
        if (this.powerGrid == null) {
            this.powerGrid = PowerGrid()
            VoltValveMod.LOGGER.info("New powergrid at $pPos")
        } else {
            VoltValveMod.LOGGER.info("Powergrid already exists at $pPos")
        }

    }

}