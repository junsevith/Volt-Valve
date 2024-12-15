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

    val CABLE_BE: RegistryObject<BlockEntityType<CableEntity>> =
        REGISTRY.register("cable_be") {
            BlockEntityType.Builder.of(
                ::CableEntity, ModBlocks.CABLE.get()
            ).build(null)
        }
}