package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.BatteryChargerBlock
import example.voltvalvemod.block.custom.BatteryGeneratorBlock
import example.voltvalvemod.block.custom.PowerGrid
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
import net.minecraft.world.level.block.entity.TickingBlockEntity
import net.minecraft.world.level.block.state.BlockState

class BatteryChargerEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.BATTERY_CHARGER_BE.get(), pPos, pBlockState), Reciever {

    private val requestedPower: Long = 100
    private var currentPower = 0
    var isEmpty : Boolean = true
    private var battery : ItemStack? = null
    private var sendingPowerStrength : Float = 0f

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
//            if (field != value) {
//                field?.removeReciever(this)
//            }
            field = value
            field?.updateReciever(this)
        }

    override fun getPowerRequest(): Long {
        if(isEmpty){
            return 0
        }
        return requestedPower
    }

    override fun providePower(amount: Long) {
        this.currentPower = amount.toInt()
    }

    override fun isOn(): Boolean {
        return true
    }

    override fun disconnect() {
        this.powerGrid?.removeReciever(this)
    }


    fun tick(pLevel: Level, pPos: BlockPos, pState: BlockState) {
        (pLevel.getBlockState(worldPosition).block as BatteryChargerBlock).updateIsEmpty(isEmpty, pState, pLevel, pPos)
        if (level != null && pLevel.getBlockState(worldPosition).block is BatteryChargerBlock) {
            sendingPowerStrength = 2f * currentPower.toFloat() / 200f
        }
        if(!isEmpty && battery != null && sendingPowerStrength > 0f) {
            if(Battery.getCharge(battery!!) <= 5000){
                Battery.setCharge(battery!!, sendingPowerStrength)
                val powerVolume = Battery.getCharge(battery!!).toInt() / 625
                (pLevel.getBlockState(worldPosition).block as BatteryChargerBlock).powerVolumeUpdate(powerVolume, pState, pLevel, pPos)
            }
        }
        powerGrid?.updateReciever(this, silent = true)
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