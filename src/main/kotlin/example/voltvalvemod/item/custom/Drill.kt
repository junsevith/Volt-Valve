package example.voltvalvemod.item.custom

import example.voltvalvemod.item.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class Drill(pAttackDamageModifier: Float, pAttackSpeedModifier: Float, pBlocks: TagKey<Block>, pProperties: Properties):
    DiggerItem(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties), Rechargeable {

    companion object {
        val pTier = object : Tier {
            override fun getUses(): Int {
                return Tiers.DIAMOND.uses
            }

            override fun getSpeed(): Float {
                return Tiers.DIAMOND.speed
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
                return Ingredient.of(Items.IRON_INGOT)
            }
        }
    }

    override fun onBlockStartBreak(itemstack: ItemStack?, pos: BlockPos?, player: Player?): Boolean {
        if (itemstack == null)
            return false
        return Rechargeable.getCharge(itemstack) <= 0
    }

    override fun mineBlock(
        pStack: ItemStack,
        pLevel: Level,
        pState: BlockState,
        pPos: BlockPos,
        pEntityLiving: LivingEntity
    ): Boolean {
        Rechargeable.setCharge(pStack, -Rechargeable.MAX_CHARGE/(pTier.uses/4f))
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving)
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        val stack = pPlayer.getItemInHand(pUsedHand)

        if (pPlayer is ServerPlayer) {
            val charge = Rechargeable.getCharge(stack)
            if (Rechargeable.hasBattery(stack)) {
                val batteryStack = ItemStack(ModItems.BATTERY.get())
                Rechargeable.setCharge(batteryStack, charge)

                if (pPlayer.inventory.freeSlot < 0) {
                    pPlayer.drop(batteryStack, false)
                }
                else {
                    pPlayer.inventory.add(batteryStack)
                }

                Rechargeable.setCharge(stack, -charge)
                Rechargeable.removeBattery(stack)
            }
        }

        return InteractionResultHolder.success(stack)
    }
}