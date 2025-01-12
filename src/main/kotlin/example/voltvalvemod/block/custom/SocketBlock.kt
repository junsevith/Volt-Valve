package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.ModBlockEntities
import example.voltvalvemod.block.entity.SocketBlockEntity
import example.voltvalvemod.block.entity.SolarPanelBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.BlockHitResult


class SocketBlock(pProperties: Properties) : DirectionalBlock(pProperties), EntityBlock {
    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, pContext.nearestLookingDirection.opposite) as BlockState
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        pBuilder.add(*arrayOf<Property<*>>(FACING))
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return SocketBlockEntity(pPos, pState)
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
            if (entity is SocketBlockEntity) {
                pPlayer.sendSystemMessage(Component.literal(entity.powerGrid?.getStatus() ?: "no power grid" ))
            } else {
                throw IllegalStateException("Our Container provider is missing!")
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide())
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
            if (blockEntity is SocketBlockEntity) {
                blockEntity.disconnect()

                val neighbours = mutableListOf<BlockEntity?>()
                neighbours.add(pLevel.getBlockEntity(pPos.north()))
                neighbours.add(pLevel.getBlockEntity(pPos.south()))
                neighbours.add(pLevel.getBlockEntity(pPos.west()))
                neighbours.add(pLevel.getBlockEntity(pPos.east()))
                neighbours.add(pLevel.getBlockEntity(pPos.above()))
                neighbours.remove(pLevel.getBlockEntity(pPos.relative(blockEntity.dir)))

                neighbours.filterIsInstance<SolarPanelBlockEntity>()
                    .map { p -> p.grid }
                    .toSet()
                    .forEach(PanelGrid::removeSocket)
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    override fun <T : BlockEntity?> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (pLevel.isClientSide)
            return null

        return createTickerHelper(pBlockEntityType, ModBlockEntities.SOCKET_BE.get())
        { pLevel1, _, _, pBlockEntity -> pBlockEntity.tick(pLevel1) }
    }

    private fun <E : BlockEntity?, A : BlockEntity?> createTickerHelper(
        pServerType: BlockEntityType<A>,
        pClientType: BlockEntityType<E>,
        pTicker: BlockEntityTicker<in E>
    ): BlockEntityTicker<A>? {
        return if (pClientType === pServerType) pTicker as BlockEntityTicker<A> else null
    }
}