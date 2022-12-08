package com.paragon.impl.ui.configuration

import com.paragon.impl.module.client.ClickGUI
import com.paragon.impl.module.client.ClickGUI.darkenBackground
import com.paragon.impl.ui.hub.HubWindow
import com.paragon.impl.ui.util.Click
import com.paragon.impl.ui.windows.Window
import com.paragon.util.render.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import java.awt.Color

/**
 * @author Surge
 * @since 27/07/2022
 */
class ConfigurationGUI : GuiScreen() {

    var closeOnEscape = true

    private var currentGUI: GuiImplementation? = ClickGUI.getGUI()
    private val hub = HubWindow(5f, 5f, 90f, 16f)
    val windowsList: MutableList<Window> = mutableListOf()
    val removeBuffer: MutableList<Window> = mutableListOf()

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        if ((currentGUI?.javaClass ?: return) != ClickGUI.getGUI().javaClass) {
            currentGUI = ClickGUI.getGUI()
            currentGUI?.initGui()
        }

        var mouseDelta = Mouse.getDWheel()

        if (removeBuffer.isNotEmpty()) {
            windowsList.removeIf(removeBuffer::contains)
        }

        windowsList.forEach {
            if (it.scroll(mouseX, mouseY, mouseDelta)) {
                mouseDelta = 0
            }
        }

        if (darkenBackground.value) {
            RenderUtil.drawRect(0f, 0f, width.toFloat(), height.toFloat(), Color(0, 0, 0, 150))
        }

        currentGUI?.width = width.toFloat()
        currentGUI?.height = height.toFloat()
        currentGUI?.drawScreen(mouseX, mouseY, mouseDelta)

        windowsList.forEach { it.draw(mouseX, mouseY, mouseDelta) }

        hub.x = 5f
        hub.y = ScaledResolution(Minecraft.getMinecraft()).scaledHeight - hub.getTotalHeight() - 5f
        hub.draw(mouseX.toFloat(), mouseY.toFloat(), mouseDelta)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        windowsList.reverse()

        windowsList.forEach {
            if (it.mouseClicked(mouseX, mouseY, Click.getClick(mouseButton))) {
                return
            }
        }

        windowsList.reverse()

        currentGUI?.mouseClicked(mouseX, mouseY, mouseButton)

        hub.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), Click.getClick(mouseButton))
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        windowsList.forEach { it.mouseReleased(mouseX, mouseY, Click.getClick(state)) }

        currentGUI?.mouseReleased(mouseX, mouseY, state)

        hub.mouseReleased(mouseX.toFloat(), mouseY.toFloat(), Click.getClick(state))
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (closeOnEscape) {
            super.keyTyped(typedChar, keyCode)
        }

        currentGUI?.keyTyped(typedChar, keyCode)

        windowsList.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun onGuiClosed() {
        super.onGuiClosed()

        currentGUI?.onGuiClosed()
    }

    override fun doesGuiPauseGame() = ClickGUI.pause.value

}