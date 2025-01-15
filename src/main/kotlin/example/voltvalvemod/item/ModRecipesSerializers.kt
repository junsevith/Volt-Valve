package example.voltvalvemod.item

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.item.crafting.RechargeRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModRecipesSerializers {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VoltValveMod.ID)

    val RECHARGE = REGISTRY.register("crafting_special_recharge") {
        SimpleCraftingRecipeSerializer { pId: ResourceLocation, pCategory: CraftingBookCategory ->
            RechargeRecipe(pId, pCategory)
        }
    }
}