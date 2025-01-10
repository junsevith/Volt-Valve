package example.voltvalvemod.screen

import com.mojang.blaze3d.systems.RenderSystem
import example.voltvalvemod.VoltValveMod
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class ElectricFurnaceEntityScreen(pMenu: ElectricFurnaceEntityMenu, pPlayerInventory: Inventory, pTitle: Component) :
    AbstractContainerScreen<ElectricFurnaceEntityMenu?>(pMenu, pPlayerInventory, pTitle) {
    override fun init() {
        super.init()
        this.inventoryLabelY = 10000
        this.titleLabelY = 10000
    }

    override fun renderBg(guiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)

        renderProgressArrow(guiGraphics, x, y)
    }

    private fun renderProgressArrow(guiGraphics: GuiGraphics, x: Int, y: Int) {
        if (menu?.isCrafting == true) {
            guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu!!.scaledProgress)
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(guiGraphics)
        super.render(guiGraphics, mouseX, mouseY, delta)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    companion object {
        private val TEXTURE = ResourceLocation(VoltValveMod.ID, "textures/gui/electric_furnace_gui.png")
    }
}