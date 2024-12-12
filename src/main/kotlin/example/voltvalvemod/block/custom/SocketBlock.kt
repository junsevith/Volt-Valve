package example.voltvalvemod.block.custom

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property

class SocketBlock(pProperties: Properties) : Block(pProperties){
    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(DirectionalBlock.FACING, pContext.nearestLookingDirection.opposite) as BlockState
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        pBuilder.add(*arrayOf<Property<*>>(DirectionalBlock.FACING))
    }
}