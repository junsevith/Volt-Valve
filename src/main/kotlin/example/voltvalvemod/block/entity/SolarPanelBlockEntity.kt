package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.SolarPanelBlock
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SolarPanelBlockEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.SOLAR_PANEL_BE.get(), pPos, pBlockState) {

    private var powerFraction = 0.0
    private var flag = true

    fun tick(pLevel: Level, pPos: BlockPos) {
        if (pLevel.gameTime % 20 == 0L) {
            val brightness = pLevel.getBrightness(LightLayer.SKY, pPos) - pLevel.skyDarken
            var sunAngle = pLevel.getSunAngle(1.0f)

            val alfa = if (sunAngle < Mth.PI) 0.0f else Mth.PI * 2F
            sunAngle += (alfa - sunAngle) * 0.2f
            powerFraction = Mth.clamp(brightness.toDouble() * Mth.cos(sunAngle) / 15, 0.0, 1.0)
        }
    }

    fun providePowerSum(): Double {
        val p = getPower()
        resetFlag()
        return p
    }

    private fun getPower(): Double {
        flag = false

        val neighbors = mutableListOf<BlockEntity?>()
        neighbors.add(this.level!!.getBlockEntity(worldPosition.north()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.south()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.east()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.west()))

        var sum = 0.0
        neighbors.filterIsInstance<SolarPanelBlockEntity>()
            .forEach { entity ->
                if (entity.flag)
                    sum += entity.getPower()
            }

        return powerFraction * 20 + sum
    }

    private fun resetFlag() {
        flag = true

        val neighbors = mutableListOf<BlockEntity?>()
        neighbors.add(this.level!!.getBlockEntity(worldPosition.north()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.south()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.east()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.west()))

        val panels = neighbors.filterIsInstance<SolarPanelBlockEntity>()
            .filter { entity -> !entity.flag }

        panels.forEach(SolarPanelBlockEntity::resetFlag)
    }
}