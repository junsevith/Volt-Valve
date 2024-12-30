package example.voltvalvemod.screen

import example.voltvalvemod.VoltValveMod
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModMenuTypes {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, VoltValveMod.ID)

    val GEM_POLISHING_MENU: RegistryObject<MenuType<ExampleEntityMenu>> =
        registerMenuType("gem_polishing_menu", ::ExampleEntityMenu)

    val ELECTRIC_FURNACE_MENU: RegistryObject<MenuType<ElectricFurnaceEntityMenu>> =
        registerMenuType("electric_furnace_menu", ::ElectricFurnaceEntityMenu)

    private fun <T : AbstractContainerMenu?> registerMenuType(
        name: String,
        factory: IContainerFactory<T>
    ): RegistryObject<MenuType<T>> {
        return REGISTRY.register(name) { IForgeMenuType.create(factory) }
    }

}