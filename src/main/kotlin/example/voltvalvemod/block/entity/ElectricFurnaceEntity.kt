package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.ElectricFurnaceBlock
import example.voltvalvemod.block.custom.PowerGrid
import example.voltvalvemod.block.interfaces.Reciever
import example.voltvalvemod.block.interfaces.Transmitter
import example.voltvalvemod.screen.ElectricFurnaceEntityMenu
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

class ElectricFurnaceEntity(pPos: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.ELECTRIC_FURNACE_BE.get(), pPos, pBlockState), Reciever, MenuProvider {
    private val itemHandler = ItemStackHandler(2)
    private var lazyItemHandler: LazyOptional<IItemHandler> = LazyOptional.empty()

    protected val data: ContainerData
    private var progress = 0
    private var maxProgress = 78

    private var currentPower = 0

    override var powerGrid: PowerGrid? = null
        get() {
            return field
        }
        set(value) {
            field?.removeReciever(this)
            field = value
            field?.updateReciever(this)
        }

    override fun getPowerRequest(): Long {
        return 120
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



    init {
        this.data = object : ContainerData {
            override fun get(pIndex: Int): Int {
                return when (pIndex) {
                    0 -> this@ElectricFurnaceEntity.progress
                    1 -> this@ElectricFurnaceEntity.maxProgress
                    else -> 0
                }
            }

            override fun set(pIndex: Int, pValue: Int) {
                when (pIndex) {
                    0 -> this@ElectricFurnaceEntity.progress = pValue
                    1 -> this@ElectricFurnaceEntity.maxProgress = pValue
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
        return ElectricFurnaceEntityMenu(pContainerId, pPlayerInventory, this, this.data)
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
        var signalStrength = 0
        if (level != null && pLevel.getBlockState(worldPosition).block is ElectricFurnaceBlock) {
            (pLevel.getBlockState(worldPosition).block as ElectricFurnaceBlock).powerUpdate(pLevel, pPos, pState, currentPower)
            signalStrength = pLevel.getBlockState(worldPosition).getValue(ElectricFurnaceBlock.SIGNAL)
            val cookTime = getCookTimeBasedOnSignal(signalStrength)
            maxProgress = cookTime
        }
        if (hasRecipe() && signalStrength > 0 ) {
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
        val result = processRecipe(itemHandler.getStackInSlot(INPUT_SLOT).item)
        itemHandler.extractItem(INPUT_SLOT, 1, false)

        itemHandler.setStackInSlot(
            OUTPUT_SLOT, ItemStack(
                result.item,
                itemHandler.getStackInSlot(OUTPUT_SLOT).count + result.count
            )
        )
    }



    private fun hasRecipe(): Boolean {
        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT).item
        if(inputItem !in FurnaceRecipes){
            return false
        }
        val result = processRecipe(inputItem)
        return canInsertAmountIntoOutputSlot(result.count) && canInsertItemIntoOutputSlot(result.item)
    }

    private fun processRecipe(item: Item): ItemStack {
        val result = ItemStack(FurnaceRecipes[item], 1)
        return result
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

    private fun getCookTimeBasedOnSignal(signal: Int): Int {
        return 250 - (signal * 10)
    }

    companion object {
        private const val INPUT_SLOT = 0
        private const val OUTPUT_SLOT = 1
        val FurnaceRecipes = mapOf(
            // Rudy
            Items.RAW_IRON to Items.IRON_INGOT,
            Items.IRON_ORE to Items.IRON_INGOT,
            Items.DEEPSLATE_IRON_ORE to Items.IRON_INGOT,

            Items.RAW_GOLD to Items.GOLD_INGOT,
            Items.GOLD_ORE to Items.GOLD_INGOT,
            Items.DEEPSLATE_GOLD_ORE to Items.GOLD_INGOT,
            Items.NETHER_GOLD_ORE to Items.GOLD_NUGGET,

            Items.RAW_COPPER to Items.COPPER_INGOT,
            Items.COPPER_ORE to Items.COPPER_INGOT,
            Items.DEEPSLATE_COPPER_ORE to Items.COPPER_INGOT,

            Items.ANCIENT_DEBRIS to Items.NETHERITE_SCRAP,
            Items.REDSTONE_ORE to Items.REDSTONE,
            Items.DEEPSLATE_REDSTONE_ORE to Items.REDSTONE_ORE,
            Items.COAL_ORE to Items.COAL,
            Items.DEEPSLATE_COAL_ORE to Items.COAL,
            Items.EMERALD_ORE to Items.EMERALD,
            Items.DEEPSLATE_EMERALD_ORE to Items.EMERALD,
            Items.LAPIS_ORE to Items.LAPIS_LAZULI,
            Items.DEEPSLATE_LAPIS_ORE to Items.LAPIS_LAZULI,
            Items.DIAMOND_ORE to Items.DIAMOND,
            Items.DEEPSLATE_DIAMOND_ORE to Items.DIAMOND,
            Items.NETHER_QUARTZ_ORE to Items.QUARTZ,

            // Narzedzia i zbroje
            Items.IRON_AXE to Items.IRON_NUGGET,
            Items.IRON_HOE to Items.IRON_NUGGET,
            Items.IRON_SHOVEL to Items.IRON_NUGGET,
            Items.IRON_PICKAXE to Items.IRON_NUGGET,
            Items.IRON_SWORD to Items.IRON_NUGGET,
            Items.IRON_HELMET to Items.IRON_NUGGET,
            Items.IRON_CHESTPLATE to Items.IRON_NUGGET,
            Items.IRON_LEGGINGS to Items.IRON_NUGGET,
            Items.IRON_BOOTS to Items.IRON_NUGGET,
            Items.IRON_HORSE_ARMOR to Items.IRON_NUGGET,
            Items.CHAINMAIL_HELMET to Items.IRON_NUGGET,
            Items.CHAINMAIL_CHESTPLATE to Items.IRON_NUGGET,
            Items.CHAINMAIL_LEGGINGS to Items.IRON_NUGGET,
            Items.CHAINMAIL_BOOTS to Items.IRON_NUGGET,

            Items.GOLDEN_AXE to Items.GOLD_NUGGET,
            Items.GOLDEN_HOE to Items.GOLD_NUGGET,
            Items.GOLDEN_SHOVEL to Items.GOLD_NUGGET,
            Items.GOLDEN_PICKAXE to Items.GOLD_NUGGET,
            Items.GOLDEN_SWORD to Items.GOLD_NUGGET,
            Items.GOLDEN_HELMET to Items.GOLD_NUGGET,
            Items.GOLDEN_CHESTPLATE to Items.GOLD_NUGGET,
            Items.GOLDEN_LEGGINGS to Items.GOLD_NUGGET,
            Items.GOLDEN_BOOTS to Items.GOLD_NUGGET,
            Items.GOLDEN_HORSE_ARMOR to Items.GOLD_NUGGET,


            // Bloki
            Items.COBBLESTONE to Items.STONE,
            Items.STONE to Items.SMOOTH_STONE,
            Items.STONE_BRICKS to Items.CRACKED_STONE_BRICKS,
            Items.COBBLED_DEEPSLATE to Items.DEEPSLATE,
            Items.DEEPSLATE_BRICKS to Items.CRACKED_DEEPSLATE_BRICKS,
            Items.DEEPSLATE_TILES to Items.CRACKED_DEEPSLATE_TILES,
            Items.SANDSTONE to Items.SMOOTH_SANDSTONE,
            Items.RED_SANDSTONE to Items.SMOOTH_RED_SANDSTONE,
            Items.NETHER_BRICKS to Items.CRACKED_NETHER_BRICKS,
            Items.BASALT to Items.SMOOTH_BASALT,
            Items.POLISHED_BLACKSTONE_BRICKS to Items.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            Items.QUARTZ_BLOCK to Items.SMOOTH_QUARTZ,

            Items.CLAY to Items.TERRACOTTA,
            Items.WHITE_TERRACOTTA to Items.WHITE_GLAZED_TERRACOTTA,
            Items.ORANGE_TERRACOTTA to Items.ORANGE_GLAZED_TERRACOTTA,
            Items.MAGENTA_TERRACOTTA to Items.MAGENTA_GLAZED_TERRACOTTA,
            Items.LIGHT_BLUE_TERRACOTTA to Items.LIGHT_BLUE_GLAZED_TERRACOTTA,
            Items.YELLOW_TERRACOTTA to Items.YELLOW_GLAZED_TERRACOTTA,
            Items.LIME_TERRACOTTA to Items.LIME_GLAZED_TERRACOTTA,
            Items.PINK_TERRACOTTA to Items.PINK_GLAZED_TERRACOTTA,
            Items.GRAY_TERRACOTTA to Items.GRAY_GLAZED_TERRACOTTA,
            Items.LIGHT_GRAY_TERRACOTTA to Items.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Items.CYAN_TERRACOTTA to Items.CYAN_GLAZED_TERRACOTTA,
            Items.PURPLE_TERRACOTTA to Items.PURPLE_GLAZED_TERRACOTTA,
            Items.BLUE_TERRACOTTA to Items.BLUE_GLAZED_TERRACOTTA,
            Items.BROWN_TERRACOTTA to Items.BROWN_GLAZED_TERRACOTTA,
            Items.GREEN_TERRACOTTA to Items.GREEN_GLAZED_TERRACOTTA,
            Items.RED_TERRACOTTA to Items.RED_GLAZED_TERRACOTTA,
            Items.BLACK_TERRACOTTA to Items.BLACK_GLAZED_TERRACOTTA,

            Items.SAND to Items.GLASS,
            Items.RED_SAND to Items.GLASS,

            Items.CLAY_BALL to Items.BRICK,
            Items.WET_SPONGE to Items.SPONGE,
            Items.NETHERRACK to Items.NETHER_BRICK,

            // Jedzenie
            Items.PORKCHOP to Items.COOKED_PORKCHOP,
            Items.BEEF to Items.COOKED_BEEF,
            Items.CHICKEN to Items.COOKED_CHICKEN,
            Items.COD to Items.COOKED_COD,
            Items.SALMON to Items.COOKED_SALMON,
            Items.POTATO to Items.BAKED_POTATO,
            Items.MUTTON to Items.COOKED_MUTTON,
            Items.RABBIT to Items.COOKED_RABBIT,
            Items.KELP to Items.DRIED_KELP,

            // Drewno → Węgiel drzewny
            Items.OAK_LOG to Items.CHARCOAL,
            Items.STRIPPED_OAK_LOG to Items.CHARCOAL,
            Items.SPRUCE_LOG to Items.CHARCOAL,
            Items.STRIPPED_SPRUCE_LOG to Items.CHARCOAL,
            Items.BIRCH_LOG to Items.CHARCOAL,
            Items.STRIPPED_BIRCH_LOG to Items.CHARCOAL,
            Items.JUNGLE_LOG to Items.CHARCOAL,
            Items.STRIPPED_JUNGLE_LOG to Items.CHARCOAL,
            Items.ACACIA_LOG to Items.CHARCOAL,
            Items.STRIPPED_ACACIA_LOG to Items.CHARCOAL,
            Items.DARK_OAK_LOG to Items.CHARCOAL,
            Items.STRIPPED_DARK_OAK_LOG to Items.CHARCOAL,
            Items.MANGROVE_LOG to Items.CHARCOAL,
            Items.STRIPPED_MANGROVE_LOG to Items.CHARCOAL,
            Items.CHERRY_LOG to Items.CHARCOAL,
            Items.STRIPPED_CHERRY_LOG to Items.CHARCOAL,
            Items.CRIMSON_STEM to Items.CHARCOAL,
            Items.STRIPPED_CRIMSON_STEM to Items.CHARCOAL,
            Items.WARPED_STEM to Items.CHARCOAL,
            Items.STRIPPED_WARPED_STEM to Items.CHARCOAL,

            // Barwniki i inne
            Items.CACTUS to Items.GREEN_DYE,
            Items.SEA_PICKLE to Items.LIME_DYE,
            Items.CHORUS_FRUIT to Items.POPPED_CHORUS_FRUIT,


            )
    }


}