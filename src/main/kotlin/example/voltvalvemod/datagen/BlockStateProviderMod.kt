package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

class BlockStateProviderMod(output: PackOutput, exFileHelper: ExistingFileHelper):
    BlockStateProvider(output, VoltValveMod.ID, exFileHelper) {

    override fun registerStatesAndModels() {
        //Tu się dodaje state do bloków
        blockWithItem(ModBlocks.EXAMPLE_BLOCK)
        cableBlock()
        blockWithItem(ModBlocks.TEST_GENERATOR)
        blockWithItem(ModBlocks.TEST_RECIEVER)
        blockWithItem(ModBlocks.EXAMPLE_ENTITY)
        socketBlock()
        blockWithItem(ModBlocks.SOLAR_PANEL)
    }

    private fun<B: Block> blockWithItem(blockRegistryObject: RegistryObject<B>) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()))
    }

    fun cableBlock() {
        val baseName = key(ModBlocks.CABLE.get()).toString()
        fourWayBlock(
            ModBlocks.CABLE.get(),
            models().fencePost(baseName + "_post", mcLoc("block/quartz_block_side")),
            models().fenceSide(baseName + "_side", blockTexture(Blocks.COPPER_BLOCK)),
        )
    }

    fun socketBlock() {
        val baseName = key(ModBlocks.SOCKET.get()).toString()
        directionalBlock(
            ModBlocks.SOCKET.get(),
            models().cubeTop(baseName, mcLoc("voltvalvemod:block/"+baseName+"_side"), mcLoc("voltvalvemod:block/"+baseName+"_front"))
        )
    }

    private fun key(block: Block): ResourceLocation? {
        return ForgeRegistries.BLOCKS.getKey(block)
    }
}