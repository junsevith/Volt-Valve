package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.item.custom.Battery
import example.voltvalvemod.item.custom.Drill
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModItems {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, VoltValveMod.ID)

    //Tutaj dodajemy nowe przedmioty
    val EXAMPLE_ITEM = REGISTRY.register("example_item") {
        net.minecraft.world.item.SnowballItem(Item.Properties())
    }
    val BATTERY = REGISTRY.register("battery") {
        Battery(Item.Properties().stacksTo(1))
    }
    val DRILL = REGISTRY.register("drill") {
        Drill(1F, -3.0F, BlockTags.MINEABLE_WITH_PICKAXE, Item.Properties().stacksTo(1))
    }
}