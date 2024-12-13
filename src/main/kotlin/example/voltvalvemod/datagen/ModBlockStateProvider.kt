package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.RegistryObject

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper):
    BlockStateProvider(output, VoltValveMod.ID, exFileHelper) {

    override fun registerStatesAndModels() {
        blockWithItem(ModBlocks.EXAMPLE_BLOCK)
    }

    private fun blockWithItem(blockRegistryObject: RegistryObject<Block>) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()))
    }
}