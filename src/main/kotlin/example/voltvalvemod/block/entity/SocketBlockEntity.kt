package example.voltvalvemod.block.entity

import example.voltvalvemod.block.custom.PanelGrid
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Generator
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class SocketBlockEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.SOCKET_BE.get(), pPos, pBlockState), Generator {

    val dir = this.blockState.getValue(BlockStateProperties.FACING)

    override fun onLoad() {
        super.onLoad()

        val neighbours = mutableListOf<BlockEntity?>()
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.north()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.south()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.west()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.east()))
        neighbours.add(this.level!!.getBlockEntity(this.blockPos.above()))
        neighbours.remove(this.level!!.getBlockEntity(this.blockPos.relative(dir)))

        neighbours.filterIsInstance<SolarPanelBlockEntity>()
            .map { p -> p.grid }
            .toSet()
            .forEach(PanelGrid::addSockets)
    }

    override var powerGrid: PowerGrid? = null
        set(value) {
            field?.removeGenerator(this)
            field = value
            field?.updateGenerator(this)
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

        val sum = grids.sumOf(PanelGrid::powerPerSocket)

        return sum.toLong()
    }

    fun tick(pLevel: Level) {
        if (pLevel.gameTime % 10L == 0L) {
            powerGrid?.updateGenerator(this, true)
        }
    }

    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeGenerator(this)
    }
}