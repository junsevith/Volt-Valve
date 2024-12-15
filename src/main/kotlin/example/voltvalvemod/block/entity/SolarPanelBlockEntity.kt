package example.voltvalvemod.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class SolarPanelBlockEntity(pType: BlockEntityType<*>, pPos: BlockPos, pBlockState: BlockState) : BlockEntity(pType, pPos, pBlockState) {
    fun tick(pLevel: Level, pPos: BlockPos) {
        var brightness = pLevel.getBrightness(LightLayer.SKY, pPos) - pLevel.skyDarken
        var sunAngle = pLevel.getSunAngle(1.0f)

        val alfa = if (sunAngle < 3.1415927f) 0.0f else 6.2831855f
        sunAngle += (alfa - sunAngle) * 0.2f
        brightness = Math.round(brightness.toFloat() * Mth.cos(sunAngle))
    }
}