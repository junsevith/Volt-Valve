package example.voltvalvemod.datagen

import example.voltvalvemod.block.ModBlocks
import example.voltvalvemod.item.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class ModRecipeProvider(pOutput: PackOutput) :
    RecipeProvider(pOutput),
    IConditionBuilder{

    override fun buildRecipes(pWriter: Consumer<FinishedRecipe>) {
        exampleBlockRecipe(pWriter)
        exampleItemRecipe(pWriter)

    }

    private fun exampleBlockRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EXAMPLE_BLOCK.get())
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', Items.GOLD_INGOT)
            .define('Y', Items.DIAMOND)
            .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
            .save(pWriter)
    }

    private fun exampleItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXAMPLE_ITEM.get())
            .requires(Items.GOLD_INGOT)
            .requires(Items.DIAMOND)
            .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
            .save(pWriter)
    }
}