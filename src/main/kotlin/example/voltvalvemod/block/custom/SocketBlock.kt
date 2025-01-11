package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.SocketBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property

class SocketBlock(pProperties: Properties) : DirectionalBlock(pProperties), EntityBlock{
    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, pContext.nearestLookingDirection.opposite) as BlockState
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        pBuilder.add(*arrayOf<Property<*>>(FACING))
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return SocketBlockEntity(pPos, pState)
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
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }
}