package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    pOutput: PackOutput,
    pLookupProvider: CompletableFuture<HolderLookup.Provider>,
    pParentProvider: CompletableFuture<TagLookup<Item>>,
    pBlockTags: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?
) : ItemTagsProvider(pOutput, pLookupProvider, pParentProvider, pBlockTags, VoltValveMod.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {
        TODO("No items that need tags")
    }
}