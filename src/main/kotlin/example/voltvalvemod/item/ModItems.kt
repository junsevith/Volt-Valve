package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.item.custom.Battery
import example.voltvalvemod.item.custom.Drill
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers
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
        Drill(0f, 1F, -3.0F, BlockTags.MINEABLE_WITH_PICKAXE, Item.Properties().stacksTo(1))
    }
    val CHARGED_DRILL = REGISTRY.register("charged_drill") {
        Drill(Tiers.NETHERITE.speed*1.5F, 1F, -3.0F, BlockTags.MINEABLE_WITH_PICKAXE, Item.Properties().stacksTo(1))
    }

    val ACID = REGISTRY.register("acid") {
        Item(Item.Properties().stacksTo(16))
    }

    val SILICON_CHIP = REGISTRY.register("silicon_chip") {
        Item(Item.Properties().stacksTo(16))
    }

    val CIRCUIT = REGISTRY.register("circuit") {
        Item(Item.Properties().stacksTo(16))
    }
}