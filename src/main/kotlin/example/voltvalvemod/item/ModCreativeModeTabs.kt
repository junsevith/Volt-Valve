package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraftforge.registries.DeferredRegister

object ModCreativeModeTabs {
    val REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoltValveMod.ID)

    val VOLT_VALVE_TAB = REGISTRY.register("volt-valve") {
        CreativeModeTab.builder().icon(ModBlocks.EXAMPLE_BLOCK.get().asItem()::getDefaultInstance)
            .title(Component.literal("Volt & Valve"))
            .displayItems { itemDisplayParameters, output ->
                output.accept(ModBlocks.EXAMPLE_BLOCK.get())
                output.accept(ModBlocks.SOLAR_PANEL.get())
            }
            .build()
    }
}