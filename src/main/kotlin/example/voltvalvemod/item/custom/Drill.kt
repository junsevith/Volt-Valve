package example.voltvalvemod.item.custom

import example.voltvalvemod.item.ModItems
import example.voltvalvemod.item.tiers.DrillTier
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class Drill(pSpeed: Float, pAttackDamageModifier: Float, pAttackSpeedModifier: Float, pBlocks: TagKey<Block>, pProperties: Properties):
    DiggerItem(pAttackDamageModifier, pAttackSpeedModifier, DrillTier(pSpeed), pBlocks, pProperties), Rechargeable {

    override fun chargedItemStack(charge: Float): ItemStack {
        val stack = ItemStack(ModItems.CHARGED_DRILL.get())
        Rechargeable.setCharge(stack, charge)
        Rechargeable.putBattery(stack)
        return stack
    }

    override fun mineBlock(
        pStack: ItemStack,
        pLevel: Level,
        pState: BlockState,
        pPos: BlockPos,
        pEntityLiving: LivingEntity
    ): Boolean {
        Rechargeable.setCharge(pStack, -Rechargeable.MAX_CHARGE/(Tiers.DIAMOND.uses/4f))
        if (pEntityLiving is Player && Rechargeable.getCharge(pStack) <= 0f) {
            val newStack = ItemStack(ModItems.DRILL.get())
            Rechargeable.putBattery(newStack)
            newStack.damageValue = pStack.damageValue
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(pStack), newStack)

            pEntityLiving.inventory.setItem(pEntityLiving.inventory.selected, newStack)
        }
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

                val newStack = ItemStack(ModItems.DRILL.get())
                newStack.damageValue = stack.damageValue
                EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), newStack)

                pPlayer.inventory.setItem(pPlayer.inventory.selected, newStack)
            }
        }

        return InteractionResultHolder.success(stack)
    }
}