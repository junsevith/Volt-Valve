package example.voltvalvemod.item.custom

import net.minecraft.world.item.ItemStack

interface Rechargeable {
    companion object {
        const val MAX_CHARGE = 5000f
        private const val CHARGE_KEY = "Charge"
        private const val HAS_BATTERY = "HasBattery"

        // Funkcja do pobierania poziomu naładowania z NBT
        fun getCharge(stack: ItemStack): Float {
            val tag = stack.tag
            return tag?.getFloat(CHARGE_KEY) ?: 0f // Jeśli brak wartości, zwraca 0
        }

        // Funkcja do ustawiania poziomu naładowania w NBT
        fun setCharge(stack: ItemStack, charge: Float) {
            val tag = stack.orCreateTag
            val newCharge = charge + getCharge(stack)
            tag.putFloat(CHARGE_KEY, newCharge.coerceIn(0f, MAX_CHARGE))
        }

        fun putBattery(stack: ItemStack) {
            val tag = stack.orCreateTag
            tag.putBoolean(HAS_BATTERY, true)
        }

        fun removeBattery(stack: ItemStack) {
            val tag = stack.orCreateTag
            tag.putBoolean(HAS_BATTERY, false)
        }

        fun hasBattery(stack: ItemStack): Boolean{
            val tag = stack.tag
            return tag?.getBoolean(HAS_BATTERY) ?: false
        }
    }
}