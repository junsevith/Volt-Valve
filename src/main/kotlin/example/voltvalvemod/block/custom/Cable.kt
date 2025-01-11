package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.entity.CableEntity
import example.voltvalvemod.block.interfaces.Generator
import example.voltvalvemod.block.interfaces.Reciever
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

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
        var blockEntity: BlockEntity? = null
        if (pState.block !== pNewState.block) {
            blockEntity = pLevel.getBlockEntity(pPos)

        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)

        if (blockEntity is CableEntity) {
            blockEntity.disconnect()
        }
    }

//    override fun setPlacedBy(
//        pLevel: Level,
//        pPos: BlockPos,
//        pState: BlockState,
//        pPlacer: LivingEntity?,
//        pStack: ItemStack
//    ) {
//        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack)
//        if (!pLevel.isClientSide) {
//            val blockEntity = pLevel.getBlockEntity(pPos)
//            if (blockEntity is CableEntity) {
//                blockEntity.rebuildNetwork()
//            }
//        }
//    }


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
        val blockEntity = pLevel.getBlockEntity(pPos)
        val neighborBlockEntity = pLevel.getBlockEntity(pNeighborPos)
        if (blockEntity is CableEntity && (neighborBlockEntity is Generator || neighborBlockEntity is Reciever)) {
            VoltValveMod.LOGGER.info("2Neighbor changed at $pPos")
            blockEntity.rebuildNetwork()
        }
    }

    override fun use(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHit: BlockHitResult
    ): InteractionResult {
        if (!pLevel.isClientSide()) {
            val entity = pLevel.getBlockEntity(pPos)
            if (entity is CableEntity) {
                pPlayer.sendSystemMessage(Component.literal(entity.powerGrid?.getStatus() ?: "no power grid" ))
            } else {
                throw IllegalStateException("Our Container provider is missing!")
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide())
    }

}