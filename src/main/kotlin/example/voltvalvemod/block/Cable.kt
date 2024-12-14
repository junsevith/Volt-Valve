package example.voltvalvemod.block

import example.voltvalvemod.block.interfaces.Transmitter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.SoundType

class Cable :
    FenceBlock(Properties.copy(Blocks.OAK_FENCE).sound(SoundType.COPPER)),
    Transmitter {

    override var powerGrid: PowerGrid?

    init {
        powerGrid = PowerGrid()
    }

    override fun updateState() {

    }

    override fun rebuildNetwork() {

    }

    override fun isOn(): Boolean {
        return true
    }

}