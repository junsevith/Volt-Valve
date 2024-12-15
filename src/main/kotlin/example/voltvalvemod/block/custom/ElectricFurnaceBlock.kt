package example.voltvalvemod.block.custom

import example.voltvalvemod.block.entity.ElectricFurnace
import example.voltvalvemod.block.entity.ExampleEntity
import example.voltvalvemod.block.entity.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.network.NetworkHooks

class ElectricFurnaceBlock(pProperties: Properties) : BaseEntityBlock(pProperties) {
//    override fun getShape(
//        pState: BlockState,
//        pLevel: BlockGetter,
//        pPos: BlockPos,
//        pContext: CollisionContext
//    ): VoxelShape {
//        return SHAPE
//    }

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
            if (blockEntity is ExampleEntity) {
                blockEntity.drops()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    override fun use(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHit: BlockHitResult
    ): InteractionResult {
        if (!pLevel.isClientSide()) {
            val entity = pLevel.getBlockEntity(pPos)
            //to be fixed
            if (entity is ExampleEntity) {
                NetworkHooks.openScreen((pPlayer as ServerPlayer), entity, pPos)
            } else {
                throw IllegalStateException("Our Container provider is missing!")
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide())
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        //to be fixed
        return ExampleEntity(pPos, pState)
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
            pBlockEntityType, ModBlockEntities.GEM_POLISHING_BE.get()
        ) { pLevel1, pPos, pState1, pBlockEntity ->
            pBlockEntity.tick(
                pLevel1,
                pPos,
                pState1
            )
        }
    }

//    companion object {
//        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
//    }
}