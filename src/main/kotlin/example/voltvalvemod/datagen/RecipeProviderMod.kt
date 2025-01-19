package example.voltvalvemod.datagen

import example.voltvalvemod.block.ModBlocks
import example.voltvalvemod.item.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class RecipeProviderMod(pOutput: PackOutput) :
    RecipeProvider(pOutput),
    IConditionBuilder {

    override fun buildRecipes(pWriter: Consumer<FinishedRecipe>) {
        //Tutaj ustawiamy przepisy dla blooków i przedmiotów
        exampleBlockRecipe(pWriter)
        exampleItemRecipe(pWriter)
        acidItemRecipe(pWriter)
        siliconChipItemRecipe(pWriter)
        circuitItemRecipe(pWriter)
        cableItemRecipe(pWriter)
        solarPanelItemRecipe(pWriter)
        socketItemRecipe(pWriter)
        furnaceItemRecipe(pWriter)
        drillItemRecipe(pWriter)
        batteryItemRecipe(pWriter)
        batteryChargerItemRecipe(pWriter)
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


    private fun acidItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ACID.get(), 3)
            .requires(Items.WATER_BUCKET)
            .requires(Items.BLAZE_POWDER, 3)
            .requires(Items.GLASS_BOTTLE, 3)
            .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
            .save(pWriter)


    }

    private fun siliconChipItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(Items.QUARTZ),
            RecipeCategory.MISC,
            ModItems.SILICON_CHIP.get(),
            0.7f,
            200
        )
            .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
            .save(pWriter)
    }

    private fun circuitItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CIRCUIT.get())
            .pattern("SAS")
            .pattern("CCC")
            .pattern("XXX")
            .define('S', ModItems.SILICON_CHIP.get())
            .define('C', Items.COPPER_INGOT)
            .define('X', Items.REDSTONE)
            .define('A', ModItems.ACID.get())
            .unlockedBy(getHasName(ModItems.SILICON_CHIP.get()), has(ModItems.SILICON_CHIP.get()))
            .save(pWriter)
    }

    private fun cableItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.CABLE.get())
            .requires(Items.COPPER_INGOT, 3)
            .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
            .save(pWriter)
    }

    private fun solarPanelItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOLAR_PANEL.get())
            .pattern("GGG")
            .pattern("SSS")
            .pattern("CXC")
            .define('G', Items.GLASS_PANE)
            .define('S', ModItems.SILICON_CHIP.get())
            .define('C', Items.IRON_INGOT)
            .define('X', ModBlocks.CABLE.get())
            .unlockedBy(getHasName(ModItems.CIRCUIT.get()), has(ModItems.CIRCUIT.get()))
            .save(pWriter)
    }

    private fun socketItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOCKET.get())
            .pattern("ICI")
            .pattern("CXC")
            .pattern("ICI")
            .define('I', Items.IRON_INGOT)
            .define('X', ModItems.CIRCUIT.get())
            .define('C', ModBlocks.CABLE.get())
            .unlockedBy(getHasName(ModItems.CIRCUIT.get()), has(ModItems.CIRCUIT.get()))
            .save(pWriter)
    }

    private fun furnaceItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ELECTRIC_FURNACE.get())
            .pattern("III")
            .pattern("CXC")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .define('X', ModItems.CIRCUIT.get())
            .define('C', Items.COPPER_INGOT)
            .unlockedBy(getHasName(ModItems.CIRCUIT.get()), has(ModItems.CIRCUIT.get()))
            .save(pWriter)
    }

    private fun drillItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRILL.get())
            .pattern(" D ")
            .pattern("DND")
            .pattern("IXI")
            .define('D', Items.DIAMOND)
            .define('N', Items.NETHERITE_INGOT)
            .define('X', ModItems.CIRCUIT.get())
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(ModItems.CIRCUIT.get()), has(ModItems.CIRCUIT.get()))
            .save(pWriter)
    }

    private fun batteryItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY.get())
            .pattern("IAC")
            .pattern("IAC")
            .pattern("IAC")
            .define('I', Items.IRON_INGOT)
            .define('A', ModItems.ACID.get())
            .define('C', Items.COPPER_INGOT)
            .unlockedBy(getHasName(ModItems.CIRCUIT.get()), has(ModItems.CIRCUIT.get()))
            .save(pWriter)
    }

    private fun batteryChargerItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BATTERY_CHARGER.get())
            .pattern("I I")
            .pattern("C C")
            .pattern("IXI")
            .define('I', Items.IRON_INGOT)
            .define('X', ModItems.CIRCUIT.get())
            .define('C', Items.COPPER_INGOT)
            .unlockedBy(getHasName(ModItems.BATTERY.get()), has(ModItems.BATTERY.get()))
            .save(pWriter)

//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.BATTERY_CHARGER.get())
//            .requires(ModBlocks.BATTERY_GENERATOR.get())
//            .unlockedBy(getHasName(ModItems.BATTERY.get()), has(ModItems.BATTERY.get()))
//            .save(pWriter)

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.BATTERY_GENERATOR.get())
            .requires(ModBlocks.BATTERY_CHARGER.get())
            .unlockedBy(getHasName(ModItems.BATTERY.get()), has(ModItems.BATTERY.get()))
            .save(pWriter)
    }

    private fun lampItemRecipe(pWriter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TEST_RECIEVER.get())
            .pattern(" G ")
            .pattern("GBG")
            .pattern(" I ")
            .define('G', Items.GLASS_PANE)
            .define('B', Items.BAMBOO)
            .define('I', Items.IRON_INGOT)
            .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
            .save(pWriter)
    }
}