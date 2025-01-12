package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.SocketBlockEntity
import example.voltvalvemod.block.entity.SolarPanelBlockEntity

class PanelGrid(panel: SolarPanelBlockEntity) {
    val panels: MutableSet<SolarPanelBlockEntity> = mutableSetOf()
    var socketsNumber = 0

    init {
        addPanel(panel)
    }

    fun addSockets(sockets: Int = 1) {
        socketsNumber += sockets
    }

    fun removeSocket() {
        socketsNumber--
    }

    fun addPanel(panel: SolarPanelBlockEntity) {
        panels.add(panel)
    }

    fun removePanel(panel: SolarPanelBlockEntity) {
        panels.remove(panel)
    }

    fun powerPerSocket(): Double {
        return panels.sumOf(SolarPanelBlockEntity::getPower)/socketsNumber
    }

    fun addPanelsTo(grid: PanelGrid) {
        if (grid != this) {
            panels.forEach { panel ->
                grid.addPanel(panel)
                panel.grid = grid
            }
            grid.addSockets(socketsNumber)
            panels.clear()
            socketsNumber = 0
        }
    }
}