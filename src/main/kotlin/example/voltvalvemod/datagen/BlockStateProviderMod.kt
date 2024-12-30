package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile
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
        simpleBlockWithItem(ModBlocks.ELECTRIC_FURNACE.get(), UncheckedModelFile(modLoc("block/electric_furnace")))
        socketBlock()
        solarPanelBlock()
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
        val key = key(ModBlocks.SOCKET.get())
        val model = models().orientable(key.toString(),
            modLoc("block/socket_side"),
            modLoc("block/socket_front"),
            modLoc("block/socket_side"))

        myDirectionalBlock(ModBlocks.SOCKET.get(), model)
        itemModels().getBuilder(key!!.path).parent(model)
    }

    private fun myDirectionalBlock(block: Block, model: ModelFile) {
        getVariantBuilder(block)
            .forAllStates { state: BlockState ->
                val dir = state.getValue(BlockStateProperties.FACING)
                ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(if (dir == Direction.DOWN) 90 else if (dir.axis.isHorizontal) 0 else 270)
                    .rotationY(if (dir.axis.isVertical) 0 else ((dir.toYRot().toInt())) + 180 % 360)
                    .build()
            }
    }

    fun solarPanelBlock() {
        val key = key(ModBlocks.SOLAR_PANEL.get())
        val model = models().withExistingParent(key.toString(), "block/template_daylight_detector")
            .texture("side", blockTexture(Blocks.IRON_BLOCK))
            .texture("top", modLoc("block/solar_panel"))

        simpleBlock(ModBlocks.SOLAR_PANEL.get(), model)
        itemModels().getBuilder(key!!.path).parent(model)
    }

    private fun key(block: Block): ResourceLocation? {
        return ForgeRegistries.BLOCKS.getKey(block)
    }
}