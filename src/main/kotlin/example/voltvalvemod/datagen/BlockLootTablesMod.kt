package example.voltvalvemod.datagen

import example.voltvalvemod.block.ModBlocks
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class BlockLootTablesMod :
    BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags()) {

    override fun generate() {
        this.dropSelf(ModBlocks.EXAMPLE_BLOCK.get())
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return ModBlocks.REGISTRY.entries.map { it.get() }.toMutableList()
    }
}