package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import example.voltvalvemod.util.ModTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class BlockTagsProviderMod(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, VoltValveMod.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {
        //Tu się dodaje tagi do bloków
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.EXAMPLE_BLOCK.get())

        this.tag(BlockTags.FENCES)
            .add(ModBlocks.CABLE.get())

        this.tag(ModTags.Blocks.POWER_NETWORK_PART)
            .add(ModBlocks.CABLE.get())
            .add(ModBlocks.TEST_GENERATOR.get())
            .add(ModBlocks.TEST_RECIEVER.get())
            .add(ModBlocks.SOCKET.get())
            .add(ModBlocks.ELECTRIC_FURNACE.get())
            .add(ModBlocks.BATTERY_CHARGER.get())
            .add(ModBlocks.BATTERY_GENERATOR.get())
    }
}
