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
        if (level != null && !level!!.isClientSide) {
            rebuildNetwork()
        }
    }

    override fun rebuildNetwork() {
        val neighbors = mutableListOf<BlockEntity?>()
        neighbors.add(this)
        neighbors.add(this.level!!.getBlockEntity(worldPosition.above()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.below()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.north()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.south()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.east()))
        neighbors.add(this.level!!.getBlockEntity(worldPosition.west()))

        val powerNetworkParts = neighbors.filterIsInstance<PowerNetworkPart>()

//        val powerGrids = powerNetworkParts.mapNotNull { it.powerGrid }

        val (somePowerGrid, noPowerGrid) = powerNetworkParts.partition { it.powerGrid != null }
        val powerGrids = somePowerGrid.mapNotNull { it.powerGrid }.toSet()

        if (powerGrids.isNotEmpty()){
            VoltValveMod.LOGGER.info("Rebuild network: Connected to powergrid at at $worldPosition")
//            this.powerGrid = powerGrids.first()
            val powerGridsCount = powerGrids.size

            if (powerGridsCount == 1) {
                this.powerGrid = powerGrids.first()
            } else {
                val newPowerGrid = PowerGrid()
                powerGrids.forEach {
                    newPowerGrid.mergePowerGrids(it)
                }
                this.powerGrid = newPowerGrid
            }


        } else {
            VoltValveMod.LOGGER.info("Rebuild network: New powergrid at $worldPosition")
            this.powerGrid = PowerGrid()
        }

        noPowerGrid.forEach {
            if (it !is Transmitter){
                it.powerGrid = this.powerGrid
            }
        }

    }

    override fun disconnect(){
        VoltValveMod.LOGGER.info("Disconnecting cable from powergrid at $worldPosition")
        this.powerGrid!!.removeTransmitter(this)
        VoltValveMod.LOGGER.info("Disconnected cable from powergrid at $worldPosition")
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