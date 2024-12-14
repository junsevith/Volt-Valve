package example.voltvalvemod.datagen

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.item.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.RegistryObject

class ItemModelProviderMod(output: PackOutput?, existingFileHelper: ExistingFileHelper?) :
    ItemModelProvider(output, VoltValveMod.ID, existingFileHelper) {

    override fun registerModels() {
        simpleItem(ModItems.EXAMPLE_ITEM)
    }

    private fun<T: Item> simpleItem(item: RegistryObject<T>): ItemModelBuilder {
        return withExistingParent(item.id.path, ResourceLocation("item/generated"))
            .texture("layer0", ResourceLocation(VoltValveMod.ID, "item/${item.id.path}"))
    }
}