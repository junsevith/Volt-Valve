package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import example.voltvalvemod.item.ModItems
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
                //Tutaj dodjemy rzeczy do zakładki w creative
                output.accept(ModBlocks.EXAMPLE_BLOCK.get())
                output.accept(ModBlocks.SOLAR_PANEL.get())
                output.accept(ModBlocks.SOCKET.get())
                output.accept(ModItems.EXAMPLE_ITEM.get())
                output.accept(ModBlocks.CABLE.get())
                output.accept(ModBlocks.TEST_GENERATOR.get())
                output.accept(ModBlocks.TEST_RECIEVER.get())
                output.accept(ModBlocks.EXAMPLE_ENTITY.get())
                output.accept(ModBlocks.ELECTRIC_FURNACE.get())
                output.accept(ModBlocks.BATTERY_CHARGER.get())
                output.accept(ModBlocks.BATTERY_GENERATOR.get())
                output.accept(ModItems.BATTERY.get())
            }
            .build()
    }
}