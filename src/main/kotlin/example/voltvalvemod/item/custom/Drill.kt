package example.voltvalvemod.item.custom

import example.voltvalvemod.item.ModItems
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class Drill(pAttackDamageModifier: Float, pAttackSpeedModifier: Float, pTier: Tier, pBlocks: TagKey<Block>, pProperties: Properties):
    DiggerItem(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties), Rechargeable {

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {

        val stack = pPlayer.getItemInHand(pUsedHand)

        if (pPlayer is ServerPlayer) {
            val charge = Rechargeable.getCharge(stack)
            if (charge != 0F) {
                pPlayer.inventory.setItem(pPlayer.inventory.selected, ItemStack(ModItems.DRILL.get()))
                val batteryStack = ItemStack(ModItems.BATTERY.get())
                Rechargeable.setCharge(batteryStack, charge)

                if (pPlayer.inventory.freeSlot < 0) {
                    pPlayer.drop(batteryStack, false)
                }
                else {
                    pPlayer.inventory.add(batteryStack)
                }
            }
        }

        return InteractionResultHolder.success(stack)
    }
}