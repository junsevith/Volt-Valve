package example.voltvalvemod.block.entity

import example.voltvalvemod.screen.ExampleEntityMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

class ElectricFurnace(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.ELECTRIC_FURNACE_BE.get(), pPos, pBlockState), MenuProvider {
    private val itemHandler = ItemStackHandler(2)

    private var lazyItemHandler: LazyOptional<IItemHandler> = LazyOptional.empty()

    protected val data: ContainerData
    private var progress = 0
    private var maxProgress = 78

    init {
        this.data = object : ContainerData {
            override fun get(pIndex: Int): Int {
                return when (pIndex) {
                    0 -> this@ElectricFurnace.progress
                    1 -> this@ElectricFurnace.maxProgress
                    else -> 0
                }
            }

            override fun set(pIndex: Int, pValue: Int) {
                when (pIndex) {
                    0 -> this@ElectricFurnace.progress = pValue
                    1 -> this@ElectricFurnace.maxProgress = pValue
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }
    }

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap === ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast()
        }

        return super.getCapability(cap, side)
    }

    override fun onLoad() {
        super.onLoad()
        lazyItemHandler = LazyOptional.of { itemHandler }
    }

    override fun invalidateCaps() {
        super.invalidateCaps()
        lazyItemHandler.invalidate()
    }

    fun drops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0..<itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }
        Containers.dropContents(this.level, this.worldPosition, inventory)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.voltvalvemod.electric_furnace")
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu? {
        return ExampleEntityMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.put("inventory", itemHandler.serializeNBT())
        pTag.putInt("electric_furnace.progress", progress)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        itemHandler.deserializeNBT(pTag.getCompound("inventory"))
        progress = pTag.getInt("electric_furnace.progress")
    }

    fun tick(pLevel: Level, pPos: BlockPos, pState: BlockState) {
        if (hasRecipe()) {
            increaseCraftingProgress()
            setChanged(pLevel, pPos, pState)

            if (hasProgressFinished()) {
                craftItem()
                resetProgress()
            }
        } else {
            resetProgress()
        }
    }

    private fun resetProgress() {
        progress = 0
    }

    private fun craftItem() {
        val result = ItemStack(Items.DIAMOND, 1)
        itemHandler.extractItem(INPUT_SLOT, 1, false)

        itemHandler.setStackInSlot(
            OUTPUT_SLOT, ItemStack(
                result.item,
                itemHandler.getStackInSlot(OUTPUT_SLOT).count + result.count
            )
        )
    }

    private fun hasRecipe(): Boolean {
        val hasCraftingItem = itemHandler.getStackInSlot(INPUT_SLOT).item === Items.COAL
        val result = ItemStack(Items.DIAMOND)

        return hasCraftingItem && canInsertAmountIntoOutputSlot(result.count) && canInsertItemIntoOutputSlot(result.item)
    }

    private fun canInsertItemIntoOutputSlot(item: Item): Boolean {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty || itemHandler.getStackInSlot(OUTPUT_SLOT).`is`(item)
    }

    private fun canInsertAmountIntoOutputSlot(count: Int): Boolean {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).count + count <= itemHandler.getStackInSlot(OUTPUT_SLOT).maxStackSize
    }

    private fun hasProgressFinished(): Boolean {
        return progress >= maxProgress
    }

    private fun increaseCraftingProgress() {
        progress++
    }

    companion object {
        private const val INPUT_SLOT = 0
        private const val OUTPUT_SLOT = 1
    }
}