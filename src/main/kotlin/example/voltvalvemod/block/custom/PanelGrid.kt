package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.SolarPanelBlockEntity

class PanelGrid(panel: SolarPanelBlockEntity) {
    val panels: MutableSet<SolarPanelBlockEntity> = mutableSetOf()

    init {
        addPanel(panel)
    }

    fun addPanel(panel: SolarPanelBlockEntity) {
        panels.add(panel)
    }

    fun removePanel(panel: SolarPanelBlockEntity) {
        panels.remove(panel)
    }

    fun providePowerSum(): Double {
        return panels.sumOf(SolarPanelBlockEntity::getPower)
    }

    fun addPanelsTo(grid: PanelGrid) {
        if (grid != this) {
            panels.forEach { panel ->
                grid.addPanel(panel)
                panel.grid = grid
            }
            panels.clear()
        }
    }
}