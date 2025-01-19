package example.voltvalvemod

import example.voltvalvemod.block.ModBlocks
import example.voltvalvemod.block.entity.ModBlockEntities
import example.voltvalvemod.datagen.DataGenerators
import example.voltvalvemod.item.ModCreativeModeTabs
import example.voltvalvemod.item.ModItems
import example.voltvalvemod.item.ModRecipesSerializers
import example.voltvalvemod.screen.ExampleEntityScreen
import example.voltvalvemod.screen.ElectricFurnaceEntityScreen
import example.voltvalvemod.screen.ModMenuTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(VoltValveMod.ID)
object VoltValveMod {
    const val ID = "voltvalvemod"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        // You run the data generators with gradle command runData
        MOD_BUS.addListener(DataGenerators::gatherData)

        // Register the KDeferredRegister to the mod-specific event bus
        ModBlocks.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)
        ModCreativeModeTabs.REGISTRY.register(MOD_BUS)
        ModBlockEntities.REGISTRY.register(MOD_BUS)
        ModMenuTypes.REGISTRY.register(MOD_BUS)
        ModBlocks.BLOCKS.register(MOD_BUS)
        ModRecipesSerializers.REGISTRY.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        println(obj)

        MinecraftForge.EVENT_BUS.register(this)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
        MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), ::ExampleEntityScreen)
        MenuScreens.register(ModMenuTypes.ELECTRIC_FURNACE_MENU.get(), ::ElectricFurnaceEntityScreen)
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }
}