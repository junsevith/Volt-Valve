package example.voltvalvemod.item.custom

import net.minecraft.world.item.ItemStack

interface Rechargeable {
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