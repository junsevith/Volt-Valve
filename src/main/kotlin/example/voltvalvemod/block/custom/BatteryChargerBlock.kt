package example.voltvalvemod.block.custom

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.BatteryGeneratorBlock.Companion
import example.voltvalvemod.block.entity.BatteryChargerEntity
import example.voltvalvemod.block.entity.ModBlockEntities
import example.voltvalvemod.item.custom.Battery
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult

class BatteryChargerBlock(pProperties: Properties) : BaseEntityBlock(pProperties){
    init {
        registerDefaultState(this.stateDefinition.any().setValue(IS_EMPTY, true))
        registerDefaultState(this.stateDefinition.any().setValue(POWER_VOLUME, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(IS_EMPTY)
        builder.add(POWER_VOLUME)
    }
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {
        if (pState.block !== pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            //to be fixed
            if (blockEntity is BatteryChargerEntity) {
                blockEntity.drops()
                blockEntity.disconnect()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }


    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return BatteryChargerEntity(pPos, pState)
    }

    override fun <T : BlockEntity?> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (pLevel.isClientSide()) {
            return null
        }
        return createTickerHelper(
            pBlockEntityType, ModBlockEntities.BATTERY_CHARGER_BE.get()
        ) { pLevel1, pPos, pState1, pBlockEntity ->
            pBlockEntity.tick(
                pLevel1,
                pPos,
                pState1
            )
        }
    }


    override fun use(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHit: BlockHitResult
    ): InteractionResult {
        val heldItem = pPlayer.getItemInHand(pHand)
        val blockEntity = pLevel.getBlockEntity(pPos)
        if (blockEntity is BatteryChargerEntity) {
            if (heldItem.item is Battery && blockEntity.isEmpty) {
                blockEntity.insertBattery(heldItem)
                val newState = pState.setValue(IS_EMPTY, false)
                pLevel.setBlock(pPos, newState, 3)
                removeItemFromPlayer(pPlayer, pHand)
                VoltValveMod.LOGGER.info("Inserted" + heldItem + "battery at block " + pPos)

                return InteractionResult.SUCCESS
            } else if (heldItem.isEmpty && !blockEntity.isEmpty) {
                val battery = blockEntity.extractBattery()
                updateIsEmpty(true, pState, pLevel, pPos)
                powerVolumeUpdate(0, pState, pLevel, pPos)
                VoltValveMod.LOGGER.info("Extracted battery at block " + pPos)
                pPlayer.setItemInHand(pHand, battery)
                return InteractionResult.SUCCESS

            }
        }
        return InteractionResult.PASS
    }

    fun removeItemFromPlayer(player: Player, hand: InteractionHand) {
        player.setItemInHand(hand, ItemStack.EMPTY)

    }

    fun updateIsEmpty(
        isEmpty: Boolean,
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos){
        val newState = pState.setValue(IS_EMPTY, isEmpty)
        pLevel.setBlock(pPos, newState, 3)
    }

    fun powerVolumeUpdate(
        powerVolume: Int,
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos){
        val newState = pState.setValue(POWER_VOLUME, powerVolume)
        pLevel.setBlock(pPos, newState, 3)
    }

    companion object {
        var IS_EMPTY: BooleanProperty = BooleanProperty.create("is_empty",)
        var POWER_VOLUME: IntegerProperty = IntegerProperty.create("power_volume",0, 8)
    }
}