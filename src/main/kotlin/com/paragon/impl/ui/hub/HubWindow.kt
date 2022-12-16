package com.paragon.impl.ui.hub

import com.paragon.Paragon
import com.paragon.impl.module.client.Colours
import com.paragon.impl.module.hud.impl.HUDEditor
import com.paragon.impl.ui.configuration.shared.Panel
import com.paragon.impl.ui.util.Click
import com.paragon.impl.ui.windows.impl.BaritoneWindow
import com.paragon.impl.ui.windows.impl.ChangelogWindow
import com.paragon.impl.ui.windows.impl.ConfigWindow
import com.paragon.util.Wrapper
import com.paragon.util.render.RenderUtil
import com.paragon.util.render.font.FontUtil
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import java.awt.Color

/**
 * @author Surge
 * @since 08/12/2022
 */
class HubWindow(x: Float, y: Float, width: Float, height: Float) : Panel(x, y, width, height), Wrapper {

    private val hover = ColourAnimation(Color(30, 30, 30), Color(35, 35, 35), { 200f }, false, { Easing.LINEAR })

    private val tasks = listOf(
        Task("GUI", {
            if (minecraft.currentScreen != Paragon.INSTANCE.configurationGUI) {
                minecraft.displayGuiScreen(Paragon.INSTANCE.configurationGUI)
            }
        }, x, y, width - 8f, 12f),

        Task("Changelog", {
            if (Paragon.INSTANCE.configurationGUI.windowsList.any { it is ChangelogWindow }) {
                Paragon.INSTANCE.configurationGUI.windowsList.filterIsInstance<ChangelogWindow>().forEach { Paragon.INSTANCE.configurationGUI.removeBuffer.add(it) }
            } else {
                Paragon.INSTANCE.configurationGUI.windowsList.add(ChangelogWindow(200f, 200f, 300f, 250f, 16f))
            }
        }, x, y, width - 8f, 12f),

        Task("Configs", {
            if (Paragon.INSTANCE.configurationGUI.windowsList.any { it is ConfigWindow }) {
                Paragon.INSTANCE.configurationGUI.windowsList.filterIsInstance<ChangelogWindow>().forEach { Paragon.INSTANCE.configurationGUI.removeBuffer.add(it) }
            } else {
                Paragon.INSTANCE.configurationGUI.windowsList.add(ConfigWindow(200f, 200f, 200f, 150f, 16f))
            }
        }, x, y, width - 8f, 12f),

        Task("Baritone", {
            if (Paragon.INSTANCE.configurationGUI.windowsList.any { it is BaritoneWindow }) {
                Paragon.INSTANCE.configurationGUI.windowsList.filterIsInstance<ChangelogWindow>().forEach { Paragon.INSTANCE.configurationGUI.removeBuffer.add(it) }
            } else {
                Paragon.INSTANCE.configurationGUI.windowsList.add(BaritoneWindow(200f, 200f, 200f, 150f, 16f))
            }
        }, x, y, width - 8f, 12f),

        Task("HUD Editor", {
            HUDEditor.toggle()
        }, x, y, width - 8f, 12f)
    )

    override fun draw(mouseX: Float, mouseY: Float, mouseDelta: Int) {
        super.draw(mouseX, mouseY, mouseDelta)
        hover.state = isHovered(mouseX, mouseY)

        RenderUtil.drawRect(x, y, width, getTotalHeight(), hover.getColour())
        RenderUtil.drawRect(x, y + height - 2, width, 2f, Colours.mainColour.value)

        FontUtil.drawStringWithShadow("Hub", x + 3f, y + 3f, Color.WHITE)

        var yOffset = y + height + 4f

        tasks.forEach {
            it.x = x + 4f
            it.y = yOffset

            it.draw(mouseX, mouseY)

            yOffset += it.height + 2f
        }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float, click: Click) {
        super.mouseClicked(mouseX, mouseY, click)

        tasks.forEach {
            it.mouseClicked(mouseX, mouseY)
        }
    }

    fun getTotalHeight(): Float {
        return height + 6f + tasks.sumOf { it.height.toDouble() + 2f }.toFloat()
    }

    private class Task(val text: String, val onClick: () -> Unit, var x: Float, var y: Float, var width: Float, var height: Float) {

        private val hover = ColourAnimation(Color(40, 40, 40), Color(50, 50, 50), { 100f }, false, { Easing.LINEAR })
        fun draw(mouseX: Float, mouseY: Float) {
            hover.state = mouseX in x..x + width && mouseY in y..y + height

            RenderUtil.drawRect(x, y, width, height, hover.getColour())

            FontUtil.drawStringWithShadow(text, x + 3, y + 2, Color.WHITE)
        }

        fun mouseClicked(mouseX: Float, mouseY: Float): Boolean {
            if (hover.state) {
                onClick()

                return true
            }

            return false
        }

    }

}