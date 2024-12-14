package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModItems {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, VoltValveMod.ID)

    val EXAMPLE_ITEM = REGISTRY.register("example_item") {
        net.minecraft.world.item.SnowballItem(Item.Properties().durability(150))
    }

}