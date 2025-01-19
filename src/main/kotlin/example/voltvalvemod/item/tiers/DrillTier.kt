package example.voltvalvemod.item.tiers

import net.minecraft.world.item.Items
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient

class DrillTier(pSpeed: Float): Tier {
    private val speed = pSpeed

    override fun getUses(): Int {
        return Tiers.NETHERITE.uses
    }

    override fun getSpeed(): Float {
        return speed
    }

    override fun getAttackDamageBonus(): Float {
        return Tiers.IRON.attackDamageBonus
    }

    override fun getLevel(): Int {
        return Tiers.IRON.level
    }

    override fun getEnchantmentValue(): Int {
        return Tiers.IRON.enchantmentValue
    }

    override fun getRepairIngredient(): Ingredient {
        return Ingredient.of(Items.DIAMOND)
    }
}