package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = VoltValveMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class DataGenerators {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val packOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider

        generator.addProvider(event.includeServer(), ModRecipeProvider(packOutput))
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput))

        generator.addProvider(event.includeClient(), ModBlockStateProvider(packOutput, existingFileHelper))
        generator.addProvider(event.includeClient(), ModItemModelProvider(packOutput, existingFileHelper))

        val blockTagsProvider = generator.addProvider(event.includeServer(), ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper))
        generator.addProvider(event.includeServer(), ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper))
    }
}