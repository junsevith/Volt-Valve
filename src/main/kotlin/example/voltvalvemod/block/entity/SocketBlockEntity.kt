package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PanelGrid
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Generator
import example.voltvalvemod.block.interfaces.Transmitter
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class SocketBlockEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.SOCKET_BE.get(), pPos, pBlockState), Generator {

    private val dir = this.blockState.getValue(BlockStateProperties.FACING)

    override var powerGrid: PowerGrid? = null
        set(value) {
            val entity = this.level!!.getBlockEntity(this.blockPos.relative(dir))
            if (entity is Transmitter && entity.powerGrid == value) {
                field?.removeGenerator(this)
                field = value
                field?.updateGenerator(this)
            }
        }

    override fun providePower(): Long {
        val neighbours = mutableListOf<BlockEntity?>()
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.north()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.south()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.west()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.east()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.above()))
        neighbours.remove(this.level!!.getBlockEntity(this.blockPos.relative(dir)))

        val grids = neighbours.filterIsInstance<SolarPanelBlockEntity>()
            .map { panel -> panel.grid }
            .toSet()

        val sum = grids.sumOf(PanelGrid::providePowerSum)

        return sum.toLong()
    }

    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeGenerator(this)
    }
}