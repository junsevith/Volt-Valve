package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.TestRecieverEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestReciever :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    EntityBlock {

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return TestRecieverEntity(pPos, pState)
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
            if (blockEntity is TestRecieverEntity) {
                blockEntity.disconnect()
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

}