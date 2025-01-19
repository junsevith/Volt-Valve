package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.TestGeneratorEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestGenerator :
    Block(Properties.copy(Blocks.IRON_BLOCK).lightLevel { state ->
        7
    }),
    EntityBlock {

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return TestGeneratorEntity(pPos, pState)
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
            if (blockEntity is TestGeneratorEntity) {
                blockEntity.disconnect()
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

}