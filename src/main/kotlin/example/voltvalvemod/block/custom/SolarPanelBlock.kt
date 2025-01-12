package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.entity.SocketBlockEntity
import example.voltvalvemod.block.entity.SolarPanelBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class SolarPanelBlock(pProperties: Properties) : BaseEntityBlock(pProperties) {
    private val SHAPE = box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0)

    override fun getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return SolarPanelBlockEntity(pPos, pState)
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pMovedByPiston: Boolean
    ) {
        if (pState.block !== pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            if (blockEntity is SolarPanelBlockEntity) {
                blockEntity.grid.removePanel(blockEntity)
                blockEntity.grid.panels.forEach { panel ->
                    panel.grid = PanelGrid(panel)
                }

                val neighbors = mutableListOf<BlockEntity?>()
                neighbors.add(pLevel.getBlockEntity(pPos.north()))
                neighbors.add(pLevel.getBlockEntity(pPos.south()))
                neighbors.add(pLevel.getBlockEntity(pPos.east()))
                neighbors.add(pLevel.getBlockEntity(pPos.west()))

                blockEntity.grid.panels.forEach { p ->
                    val n = mutableListOf<BlockEntity?>()
                    n.add(pLevel.getBlockEntity(p.blockPos.north()))
                    n.add(pLevel.getBlockEntity(p.blockPos.south()))
                    n.add(pLevel.getBlockEntity(p.blockPos.west()))
                    n.add(pLevel.getBlockEntity(p.blockPos.east()))
                    n.add(pLevel.getBlockEntity(p.blockPos.below()))

                    n.filterIsInstance<SocketBlockEntity>().forEach { s -> p.grid.addSocket(s) }
                }

                blockEntity.grid.panels.minus(neighbors.filterIsInstance<SolarPanelBlockEntity>().toSet())
                    .forEach(SolarPanelBlockEntity::buildGrid)
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

//    TODO("Delete use function")
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
            if (entity is SolarPanelBlockEntity) {
                VoltValveMod.LOGGER.info(entity.grid.powerPerSocket())
                VoltValveMod.LOGGER.info(entity.grid.sockets.count())
            } else {
                throw IllegalStateException("Our Container provider is missing!")
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide())
    }
}