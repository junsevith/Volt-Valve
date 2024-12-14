package example.voltvalvemod.datagen

import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

class LootTableProviderMod {
    companion object {
        fun create(output: PackOutput): LootTableProvider {
            return LootTableProvider(
                output, emptySet(), listOf(
                    LootTableProvider.SubProviderEntry( ::BlockLootTablesMod, LootContextParamSets.BLOCK)
                )
            )
        }
    }
}