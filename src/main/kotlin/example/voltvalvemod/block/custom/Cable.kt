package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.entity.CableEntity
import example.voltvalvemod.block.entity.ExampleEntity
import example.voltvalvemod.block.interfaces.PowerNetworkPart
import example.voltvalvemod.block.interfaces.Transmitter
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class Cable :
    FenceBlock(Properties.copy(Blocks.OAK_FENCE).sound(SoundType.COPPER)),
    EntityBlock {

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CableEntity(pPos, pState)
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {
        if (pState.block !== pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            if (blockEntity is CableEntity) {
                blockEntity.disconnect()
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

//    override fun onNeighborChange(state: BlockState?, level: LevelReader?, pos: BlockPos?, neighbor: BlockPos?) {
//        super.onNeighborChange(state, level, pos, neighbor)
//        VoltValveMod.LOGGER.info("Neighbor changed at $pos")
//        val blockEntity = level?.getBlockEntity(pos!!)
//        if (blockEntity is CableEntity) {
//            blockEntity.rebuildNetwork()
//        }
//    }

    override fun neighborChanged(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNeighborBlock: Block,
        pNeighborPos: BlockPos,
        pMovedByPiston: Boolean
    ) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston)
        VoltValveMod.LOGGER.info("2Neighbor changed at $pPos")
        val blockEntity = pLevel.getBlockEntity(pPos)
        val neighborBlockEntity = pLevel.getBlockEntity(pNeighborPos)
        if (blockEntity is CableEntity && neighborBlockEntity is PowerNetworkPart) {
            blockEntity.rebuildNetwork()
        }
    }

}