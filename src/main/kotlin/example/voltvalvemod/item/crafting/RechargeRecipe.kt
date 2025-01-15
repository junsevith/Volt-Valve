package example.voltvalvemod.item.crafting

import com.google.common.collect.Lists
import example.voltvalvemod.item.ModRecipesSerializers
import example.voltvalvemod.item.custom.Battery
import example.voltvalvemod.item.custom.Rechargeable
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level

class RechargeRecipe(pId: ResourceLocation, pCategory: CraftingBookCategory) : CustomRecipe(pId, pCategory) {

    override fun matches(pContainer: CraftingContainer, pLevel: Level): Boolean {
        val batteries: MutableList<ItemStack> = Lists.newArrayList()
        val rechargable: MutableList<ItemStack> = Lists.newArrayList()

        for (i in 0..<pContainer.containerSize) {
            val itemstack: ItemStack = pContainer.getItem(i)
            if (!itemstack.isEmpty) {
                if (itemstack.item !is Rechargeable) {
                    if (itemstack.item !is Battery) {
                        return false
                    }
                    else {
                        batteries.add(itemstack)
                    }
                }
                else {
                    rechargable.add(itemstack)
                }
            }
        }

        return batteries.count() == 1 && rechargable.count() == 1
    }

    override fun assemble(pContainer: CraftingContainer, pRegistryAccess: RegistryAccess): ItemStack {
        val batteries: MutableList<ItemStack> = Lists.newArrayList()
        val rechargable: MutableList<ItemStack> = Lists.newArrayList()

        for (i in 0..<pContainer.containerSize) {
            val itemstack: ItemStack = pContainer.getItem(i)
            if (!itemstack.isEmpty) {
                if (itemstack.item !is Rechargeable) {
                    if (itemstack.item !is Battery) {
                        return ItemStack.EMPTY
                    }
                    else {
                        batteries.add(itemstack)
                    }
                }
                else {
                    rechargable.add(itemstack)
                }
            }
        }

        if (batteries.count() == 1 && rechargable.count() == 1) {
            val rechargeditem = rechargable[0]
            val battery = batteries[0]

            val batteryCharge = Rechargeable.getCharge(battery)

            val item = rechargeditem.item
            if (item is Rechargeable) {
                val newStack: ItemStack
                if (batteryCharge > 0) {
                    newStack = item.chargedItemStack(batteryCharge)

                    newStack.damageValue = rechargeditem.damageValue
                    EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(rechargeditem), newStack)
                }
                else {
                    newStack = rechargeditem.copy()
                    Rechargeable.putBattery(newStack)
                }

                return newStack
            }
        }

        return ItemStack.EMPTY
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return pWidth * pHeight >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipesSerializers.RECHARGE.get()
    }
}