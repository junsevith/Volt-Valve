package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.entity.TestGeneratorEntity
import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TestGenerator :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    EntityBlock {

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return TestGeneratorEntity(pPos, pState)
    }

}