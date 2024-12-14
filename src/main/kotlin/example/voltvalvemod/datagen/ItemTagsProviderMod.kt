package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ItemTagsProviderMod(
    p_275343_: PackOutput,
    p_275729_: CompletableFuture<HolderLookup.Provider>,
    p_275322_: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?

) : ItemTagsProvider(p_275343_, p_275729_, p_275322_, VoltValveMod.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {
//        TODO("No items that need tags")
    }
}