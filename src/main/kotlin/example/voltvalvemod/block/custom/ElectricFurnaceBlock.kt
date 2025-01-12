package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.ElectricFurnaceEntity
import example.voltvalvemod.block.entity.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.network.NetworkHooks

class ElectricFurnaceBlock(pProperties: Properties) : BaseEntityBlock(pProperties) {

    init {
        registerDefaultState(this.stateDefinition.any().setValue(SIGNAL, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(SIGNAL)
        builder.add(FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return this.defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
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
            //to be fixed
            if (blockEntity is ElectricFurnaceEntity) {
                blockEntity.drops()
                blockEntity.disconnect()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
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
            if (entity is ElectricFurnaceEntity) {
                NetworkHooks.openScreen((pPlayer as ServerPlayer), entity, pPos)
            } else {
                throw IllegalStateException("Our Container provider is missing!")
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide())
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return ElectricFurnaceEntity(pPos, pState)
    }

    override fun <T : BlockEntity?> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (pLevel.isClientSide()) {
            return null
        }

        return createTickerHelper(
            pBlockEntityType, ModBlockEntities.ELECTRIC_FURNACE_BE.get()
        ) { pLevel1, pPos, pState1, pBlockEntity ->
            pBlockEntity.tick(
                pLevel1,
                pPos,
                pState1
            )
        }
    }


    /*override fun neighborChanged(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        var signalStrength = world.getBestNeighborSignal(pos)
        signalStrength = if(signalStrength < 4) 0 else signalStrength - 3
        if (signalStrength != state.getValue(SIGNAL)) {
            world.setBlock(pos, state.setValue(SIGNAL, signalStrength), 3)
        }
    }*/
    fun powerUpdate(level: Level, pos: BlockPos, state: BlockState,power: Int){
        var signalStrength = power/10
        if (signalStrength != state.getValue(SIGNAL)) {
            level.setBlock(pos, state.setValue(SIGNAL, signalStrength), 3)
        }
    }

    companion object {
        val SIGNAL: IntegerProperty = IntegerProperty.create("signal", 0, 12)
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
    }
}