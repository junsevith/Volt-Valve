package example.voltvalvemod.block.custom

import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.world.level.block.Block

class SolarPanelBlock(pProperties: Properties) : Block(pProperties), Generator {
    override fun getVoltage(): Double {
        TODO("Not yet implemented")
    }

    override fun getInnerResistance(): Double {
        TODO("Not yet implemented")
    }
}