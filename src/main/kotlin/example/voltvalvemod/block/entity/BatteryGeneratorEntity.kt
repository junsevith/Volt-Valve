package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.BatteryChargerBlock
import example.voltvalvemod.block.custom.BatteryGeneratorBlock
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Generator
import example.voltvalvemod.block.interfaces.Reciever
import example.voltvalvemod.item.custom.Battery
import net.minecraft.client.renderer.texture.Tickable
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class BatteryGeneratorEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.BATTERY_GENERATOR_BE.get(), pPos, pBlockState), Generator {

    private var providingPower: Long = 0
    var isEmpty : Boolean = true
    private var battery : ItemStack? = null


    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
//            if (field != value) {
//                field?.removeReciever(this)
//            }
            field = value
            field?.updateGenerator(this)
        }

    override fun providePower(): Long {
        return providingPower
    }

    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeGenerator(this)
    }


    fun tick(pLevel: Level, pPos: BlockPos, pState: BlockState) {
        (pLevel.getBlockState(worldPosition).block as BatteryGeneratorBlock).updateIsEmpty(isEmpty, pState, pLevel, pPos)
        if(!isEmpty && battery != null) {
            if(Battery.getCharge(battery!!) > 0){
                providingPower = 60
                Battery.setCharge(battery!!, -0.6f)
                val powerVolume = Battery.getCharge(battery!!).toInt() / 625
                (pLevel.getBlockState(worldPosition).block as BatteryGeneratorBlock).powerVolumeUpdate(powerVolume, pState, pLevel, pPos)
            }else{
                providingPower = 0
            }
        }else{
            providingPower = 0
        }
        powerGrid?.updateGenerator(this, silent = true)
    }

    fun drops(){
        if(battery != null){
            val inventory = SimpleContainer(1)
            inventory.setItem(0, battery)
            Containers.dropContents(this.level, this.worldPosition, inventory)
        }
    }

    fun insertBattery(battery: ItemStack){
        this.battery = battery
        isEmpty = false
    }
    fun extractBattery(): ItemStack?{
        isEmpty = true
        val toReturn = battery
        this.battery = null
        return toReturn
    }




}