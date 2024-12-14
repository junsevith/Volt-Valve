package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
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
        //Tu się dodaje tagi do
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.EXAMPLE_BLOCK.get())
    }
}
