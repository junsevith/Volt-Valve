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
import net.minecraft.world.level.Level

class RechargeRecipe(pId: ResourceLocation, pCategory: CraftingBookCategory) : CustomRecipe(pId, pCategory) {

    override fun matches(pContainer: CraftingContainer, pLevel: Level): Boolean {
        val list: MutableList<ItemStack> = Lists.newArrayList()

        for (i in 0..<pContainer.containerSize) {
            val itemstack: ItemStack = pContainer.getItem(i)
            if (!itemstack.isEmpty) {
                if (itemstack.item !is Rechargeable)
                    return false
                list.add(itemstack)
                if (list.size > 1) {
                    val itemstack1 = list[0]
                    if (itemstack1.item !is Battery && itemstack.item !is Battery || itemstack1.count != 1 || itemstack.count != 1) {
                        return false
                    }
                }
            }
        }

        return list.size == 2
    }

    override fun assemble(pContainer: CraftingContainer, pRegistryAccess: RegistryAccess): ItemStack {
        val list: MutableList<ItemStack> = Lists.newArrayList()

        for (i in 0..<pContainer.containerSize) {
            val itemstack: ItemStack = pContainer.getItem(i)
            if (!itemstack.isEmpty) {
                if (itemstack.item !is Rechargeable)
                    return ItemStack.EMPTY
                list.add(itemstack)
                if (list.size > 1) {
                    val itemstack1 = list[0]
                    if (itemstack1.item !is Battery && itemstack.item !is Battery || itemstack1.count != 1 || itemstack.count != 1) {
                        return ItemStack.EMPTY
                    }
                }
            }
        }

        if (list.size == 2) {
            val itemstack3 = list[0]
            val itemstack4 = list[1]
            if (itemstack3.item is Battery || itemstack4.item is Battery && itemstack3.count == 1 && itemstack4.count == 1) {
                val rechargeditem: ItemStack
                val battery: ItemStack
                if (itemstack4.item is Battery) {
                    rechargeditem =  itemstack3.copy()
                    battery = itemstack4
                }
                else {
                    rechargeditem =  itemstack4.copy()
                    battery = itemstack3
                }

                val itemCharge = Rechargeable.getCharge(rechargeditem)
                val batteryCharge = Rechargeable.getCharge(battery)
                Rechargeable.setCharge(rechargeditem, itemCharge + batteryCharge)

                return rechargeditem
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