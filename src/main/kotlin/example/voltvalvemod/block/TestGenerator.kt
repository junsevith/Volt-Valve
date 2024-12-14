package example.voltvalvemod.block

import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class TestGenerator :
    Block(Properties.copy(Blocks.IRON_BLOCK)),
    Generator {

    override fun providePower(): Long {
        return 10
    }

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }


    override fun isOn(): Boolean {
        return true
    }


}