package example.voltvalvemod.util

import example.voltvalvemod.VoltValveMod
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class ModTags {

    class Blocks {
        companion object {
            val POWER_NETWORK_PART = tag("powernetworkpart")

            private fun tag(name: String): TagKey<Block> {
                return BlockTags.create(ResourceLocation(VoltValveMod.ID, name))
            }
        }
    }

    class Items {
        companion object {
//            val POWER_NETWORK_PART = tag("powernetworkpart")

            private fun tag(name: String): TagKey<Item> {
                return ItemTags.create(ResourceLocation(VoltValveMod.ID, name))
            }
        }
    }
}