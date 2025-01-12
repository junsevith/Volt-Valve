package example.voltvalvemod.block.entity

import example.voltvalvemod.VoltValveMod
import example.voltvalvemod.block.ModBlocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlockEntities {
    val REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VoltValveMod.ID)

    val GEM_POLISHING_BE: RegistryObject<BlockEntityType<ExampleEntity>> =
        REGISTRY.register("gem_polishing_be") {
            BlockEntityType.Builder.of(
                ::ExampleEntity, ModBlocks.EXAMPLE_ENTITY.get()
            ).build(null)
        }


    val ELECTRIC_FURNACE_BE: RegistryObject<BlockEntityType<ElectricFurnaceEntity>> =
        REGISTRY.register("electric_furnace_be") {
            BlockEntityType.Builder.of(
                ::ElectricFurnaceEntity, ModBlocks.ELECTRIC_FURNACE.get()
            ).build(null)
        }


    val CABLE_BE: RegistryObject<BlockEntityType<CableEntity>> =
        REGISTRY.register("cable_be") {
            BlockEntityType.Builder.of(
                ::CableEntity, ModBlocks.CABLE.get()
            ).build(null)
        }

    val SOLAR_PANEL_BE: RegistryObject<BlockEntityType<SolarPanelBlockEntity>> =
        REGISTRY.register("solar_panel_be") {
            BlockEntityType.Builder.of(
                ::SolarPanelBlockEntity, ModBlocks.SOLAR_PANEL.get()
            ).build(null)
        }

    val SOCKET_BE: RegistryObject<BlockEntityType<SocketBlockEntity>> =
        REGISTRY.register("socket_be") {
            BlockEntityType.Builder.of(
                ::SocketBlockEntity, ModBlocks.SOCKET.get()
            ).build(null)
        }

    val TEST_GENERATOR_BE: RegistryObject<BlockEntityType<TestGeneratorEntity>> =
        REGISTRY.register("test_generator_be") {
            BlockEntityType.Builder.of(
                ::TestGeneratorEntity, ModBlocks.TEST_GENERATOR.get()
            ).build(null)
        }

    val TEST_RECIEVER_BE: RegistryObject<BlockEntityType<TestRecieverEntity>> =
        REGISTRY.register("test_reciever_be") {
            BlockEntityType.Builder.of(
                ::TestRecieverEntity, ModBlocks.TEST_RECIEVER.get()
            ).build(null)
        }
}

