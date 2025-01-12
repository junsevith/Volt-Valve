package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PanelGrid
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SolarPanelBlockEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.SOLAR_PANEL_BE.get(), pPos, pBlockState) {

    var grid: PanelGrid = PanelGrid(this)
        set(value) {
            field = value
            value.addPanel(this)
        }

    override fun onLoad() {
        super.onLoad()
        buildGrid()
    }

    fun buildGrid() {
        val neighbors = mutableSetOf<BlockEntity?>()
        neighbors.add(this.level!!.getBlockEntity(worldPosition.north()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.south()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.east()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.west()))

        val panelGrids = neighbors.filterIsInstance<SolarPanelBlockEntity>()
            .map { solarPanelBE -> solarPanelBE.grid }

        if (panelGrids.isNotEmpty()) {
            grid = panelGrids.first()

            if (panelGrids.size != 1) {
                panelGrids.forEach { grid ->
                    grid.addPanelsTo(this.grid)
                }
            }
        }

        neighbors.add(this.level!!.getBlockEntity(worldPosition.below()))
        this.grid.addSockets(neighbors.count{n -> n is SocketBlockEntity})
    }

    private fun calculateExposure(pLevel: Level, pPos: BlockPos): Double {
        val brightness = pLevel.getBrightness(LightLayer.SKY, pPos) - pLevel.skyDarken
        var sunAngle = pLevel.getSunAngle(1.0f)

        val alfa = if (sunAngle < Mth.PI) 0.0f else Mth.PI * 2F
        sunAngle += (alfa - sunAngle) * 0.2f
        return Mth.clamp(brightness.toDouble() * Mth.cos(sunAngle) / 15, 0.0, 1.0)
    }

    fun getPower(): Double {
        return calculateExposure(this.level!!, worldPosition) * 20
    }
}