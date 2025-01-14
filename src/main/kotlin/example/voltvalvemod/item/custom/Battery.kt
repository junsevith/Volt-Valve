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
            val charge = getCharge(stack) // Pobiera poziom naładowania
            player.sendSystemMessage(Component.literal("Battery Charge: $charge")) // Wyświetla wiadomość na czacie
        }

        return InteractionResultHolder.success(stack) // Zwraca wynik użycia
    }



    companion object {
        private const val CHARGE_KEY = "Charge"
        // Funkcja do pobierania poziomu naładowania z NBT
        fun getCharge(stack: ItemStack): Float {
            val tag = stack.tag
            return tag?.getFloat(CHARGE_KEY) ?: 0f // Jeśli brak wartości, zwraca 0
        }

        // Funkcja do ustawiania poziomu naładowania w NBT
        fun setCharge(stack: ItemStack, charge: Float) {
            val tag = stack.orCreateTag
            val newCharge = charge + getCharge(stack)
            tag.putFloat(CHARGE_KEY, newCharge.coerceIn(0f, 5000f))
        }
    }
}