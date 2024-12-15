package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.PowerNetworkPart
import example.voltvalvemod.block.interfaces.Transmitter
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class CableEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.CABLE_BE.get(), pPos, pBlockState),  Transmitter {

    override var powerGrid: PowerGrid? = null
        set(value) {
            field = value

            field?.addTransmitter(this)
        }

    override fun onLoad() {
        super.onLoad()
        rebuildNetwork()
    }

    override fun rebuildNetwork() {
        val neighbors = mutableListOf<BlockEntity?>()
        neighbors.add(this.level!!.getBlockEntity(worldPosition.above()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.below()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.north()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.south()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.east()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.west()))

        val powerNetworkParts = neighbors.filterIsInstance<PowerNetworkPart>()

//        val powerGrids = powerNetworkParts.mapNotNull { it.powerGrid }

        val (somePowerGrid, noPowerGrid) = powerNetworkParts.partition { it.powerGrid != null }

        val powerGrids = somePowerGrid.mapNotNull { it.powerGrid }
        if (powerGrids.isNotEmpty()){
            this.powerGrid = powerGrids.first()
            VoltValveMod.LOGGER.info("Connected to powergrid at at $worldPosition")
        } else {
            this.powerGrid = PowerGrid()
            VoltValveMod.LOGGER.info("New powergrid at $worldPosition")
        }

        noPowerGrid.forEach {
            it.powerGrid = this.powerGrid
        }

    }

    fun disconnect(){
        this.powerGrid!!.removeTransmitter(this)
        VoltValveMod.LOGGER.info("Disconnected from powergrid at $worldPosition")
    }

    override fun isOn(): Boolean {
        return true
    }
    

//    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu? {
//        return ExampleEntityMenu(pContainerId, pPlayerInventory, this, this.data)
//        return null
//    }
//
//    override fun getDisplayName(): Component {
//        return Component.translatable("block.voltvalvemod.cable")
//    }
}