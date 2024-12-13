package example.voltvalvemod.datagen

import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider

class ModLootTableProvider {
    fun create(output: PackOutput): LootTableProvider {
        return LootTableProvider(output, emptySet(), listOf())
    }
}