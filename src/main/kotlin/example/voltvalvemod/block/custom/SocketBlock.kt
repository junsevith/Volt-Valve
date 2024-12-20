package example.voltvalvemod.block.custom

import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property

class SocketBlock(pProperties: Properties) : DirectionalBlock(pProperties){
    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, pContext.nearestLookingDirection.opposite) as BlockState
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        pBuilder.add(*arrayOf<Property<*>>(FACING))
    }
}