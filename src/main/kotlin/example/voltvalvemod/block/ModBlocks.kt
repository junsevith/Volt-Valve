package example.voltvalvemod.block

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.custom.SocketBlock
import example.voltvalvemod.block.custom.SolarPanelBlock
import example.voltvalvemod.block.custom.Cable
import example.voltvalvemod.block.custom.ExampleEntityBlock
import example.voltvalvemod.block.custom.TestGenerator
import example.voltvalvemod.block.custom.TestReciever
import example.voltvalvemod.item.ModItems
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier


object ModBlocks {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, VoltValveMod.ID)

    // the returned ObjectHolderDelegate can be used as a property delegate
    // this is automatically registered by the deferred registry at the correct times

    //Tutaj dodajemy nowe bloki
    val EXAMPLE_BLOCK = registerBlock("example_block") {
        Block(BlockBehaviour.Properties.of().lightLevel { 15 }.strength(3.0f))
    }
    val SOLAR_PANEL = registerBlock("solar_panel") {
        SolarPanelBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion())
    }
    val SOCKET = registerBlock("socket") {
        SocketBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).noOcclusion())
    }

    val CABLE = registerBlock("cable") { Cable() }
    val TEST_GENERATOR = registerBlock("test_generator") { TestGenerator() }
    val TEST_RECIEVER = registerBlock("test_reciever") { TestReciever() }


    val EXAMPLE_ENTITY: RegistryObject<Block> = registerBlock("example_entity") {
        ExampleEntityBlock(
            BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()
        )
    }


    private fun <T : Block> registerBlock(name: String, block: Supplier<T>): RegistryObject<T> {
        val toReturn = REGISTRY.register(name, block)
        registerBlockItem(name, toReturn)
        return toReturn
    }

    private fun <T : Block> registerBlockItem(name: String, block: RegistryObject<T>): RegistryObject<BlockItem>? {
        return ModItems.REGISTRY.register(name) { BlockItem(block.get(), Item.Properties()) }
    }
}