package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.SocketBlockEntity
import example.voltvalvemod.block.entity.SolarPanelBlockEntity

class PanelGrid(panel: SolarPanelBlockEntity) {
    val panels: MutableSet<SolarPanelBlockEntity> = mutableSetOf()
    val sockets: MutableSet<SocketBlockEntity> = mutableSetOf()

    init {
        addPanel(panel)
    }

    fun addSocket(socket: SocketBlockEntity) {
        sockets.add(socket)
    }

    fun removeSocket(socket: SocketBlockEntity) {
        sockets.remove(socket)
    }

    fun addPanel(panel: SolarPanelBlockEntity) {
        panels.add(panel)
    }

    fun removePanel(panel: SolarPanelBlockEntity) {
        panels.remove(panel)
    }

    fun powerPerSocket(): Double {
        return panels.sumOf(SolarPanelBlockEntity::getPower)/sockets.count()
    }

    fun addPanelsTo(grid: PanelGrid) {
        if (grid != this) {
            panels.forEach { panel ->
                grid.addPanel(panel)
                panel.grid = grid
            }
            sockets.forEach { s -> grid.addSocket(s) }
            panels.clear()
            sockets.clear()
        }
    }
}