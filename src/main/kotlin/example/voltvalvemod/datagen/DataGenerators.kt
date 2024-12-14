package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import net.minecraftforge.data.event.GatherDataEvent

//@Mod.EventBusSubscriber(modid = VoltValveMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class DataGenerators {
    companion object {
//        @SubscribeEvent
        fun gatherData(event: GatherDataEvent) {
            VoltValveMod.LOGGER.info("Pierogi")

            val generator = event.generator
            val packOutput = generator.packOutput
            val existingFileHelper = event.existingFileHelper
            val lookupProvider = event.lookupProvider

            generator.addProvider(event.includeServer(), RecipeProviderMod(packOutput))
            generator.addProvider(event.includeServer(), LootTableProviderMod.create(packOutput))

            generator.addProvider(event.includeClient(), BlockStateProviderMod(packOutput, existingFileHelper))
            generator.addProvider(event.includeClient(), ItemModelProviderMod(packOutput, existingFileHelper))

            val blockTagsProvider = generator.addProvider(event.includeServer(), BlockTagsProviderMod(packOutput, lookupProvider, existingFileHelper))
            generator.addProvider(event.includeServer(), ItemTagsProviderMod(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper))
        }
    }
}