package example.voltvalvemod.item.custom

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class Battery(properties: Properties) : Item(properties) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        if (!world.isClientSide) {
            val charge = Rechargeable.getCharge(stack) // Pobiera poziom naładowania
            player.sendSystemMessage(Component.literal("Battery Charge: $charge")) // Wyświetla wiadomość na czacie
        }

        return InteractionResultHolder.success(stack) // Zwraca wynik użycia
    }
}