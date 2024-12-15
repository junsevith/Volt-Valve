package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.interfaces.Transmitter
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

class Cable :
    FenceBlock(Properties.copy(Blocks.OAK_FENCE).sound(SoundType.COPPER)),
    Transmitter {

    override var powerGrid: PowerGrid? = null

    override fun rebuildNetwork() {

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

//    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
//        val res = super.getStateForPlacement(pContext)
//
//        VoltValveMod.LOGGER.info("Cable placed at ${pContext.clickedPos}")
//        return res
//
//    }

}