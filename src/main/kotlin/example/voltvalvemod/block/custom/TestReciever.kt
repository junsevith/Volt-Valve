package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.TestRecieverEntity
import example.voltvalvemod.block.interfaces.Reciever
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestReciever :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    EntityBlock {

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return TestRecieverEntity(pPos, pState)
    }

}