package com.paragon.impl.ui.configuration.camper.impl

import com.paragon.impl.module.client.ClickGUI
import com.paragon.impl.module.client.Colours
import com.paragon.impl.setting.Bind
import com.paragon.impl.setting.Setting
import com.paragon.impl.ui.configuration.shared.RawElement
import com.paragon.impl.ui.configuration.camper.CamperCheatGUI
import com.paragon.impl.ui.configuration.camper.impl.setting.*
import com.paragon.impl.ui.util.Click
import com.paragon.util.render.ColourUtil.fade
import com.paragon.util.render.ColourUtil.glColour
import com.paragon.util.render.RenderUtil
import me.surge.animation.Animation
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import net.minecraft.util.math.MathHelper
import org.lwjgl.opengl.GL11.*
import java.awt.Color

open class SettingElement<T>(
    val parent: ModuleElement,
    val setting: Setting<T>,
    x: Float,
    y: Float,
    width: Float,
    height: Float
) : RawElement(x, y, width, height) {

    val hover = ColourAnimation(Color(0, 0, 0, 100), Color(0, 0, 0, 150), { 100f }, false, Easing.LINEAR)
    val expanded = Animation({ ClickGUI.animationSpeed.value }, false, { ClickGUI.easing.value })

    val elements = arrayListOf<SettingElement<*>>()

    init {
        setting.subsettings.forEach {
            when (it.value) {
                is Boolean -> {
                    elements.add(BooleanElement(parent, it as Setting<Boolean>, x + 1, y, width - 1, height))
                }

                is Enum<*> -> {
                    elements.add(EnumElement(parent, it as Setting<Enum<*>>, x + 1, y, width - 1, height))
                }

                is Number -> {
                    elements.add(SliderElement(parent, it as Setting<Number>, x + 1, y, width - 1, height))
                }

                is Bind -> {
                    elements.add(BindElement(parent, it as Setting<Bind>, x + 1, y, width - 1, height))
                }

                is Color -> {
                    elements.add(ColourElement(parent, it as Setting<Color>, x, y, width - 1, height))
                }

                is String -> {
                    elements.add(StringElement(parent, it as Setting<String>, x, y, width - 1, height))
                }
            }
        }
    }

    override fun draw(mouseX: Float, mouseY: Float, mouseDelta: Int) {
        if (isHovered(mouseX, mouseY)) {
            CamperCheatGUI.description = setting.description
        }

        hover.state = isHovered(mouseX, mouseY)
    }

    fun drawSettings(mouseX: Float, mouseY: Float, mouseDelta: Int) {
        if (expanded.getAnimationFactor() > 0) {
            var offset = y + height + 1
            val factor = expanded.getAnimationFactor()

            if (factor != 1.0 && factor != 0.0) {
                RenderUtil.pushScissor(
                    x,
                    MathHelper.clamp(y + 4f, parent.parent.y + parent.parent.height, 100000f),
                    width,
                    MathHelper.clamp(
                        getAbsoluteHeight() - 4,
                        0f,
                        (parent.parent.y + parent.parent.height + parent.parent.moduleHeight).toFloat() - (y + 4f)
                    )
                )
            }

            elements.forEach {
                if (it.setting.isVisible()) {
                    it.x = x + 1
                    it.y = offset

                    it.draw(mouseX, mouseY, mouseDelta)

                    offset += it.getAbsoluteHeight() + 1
                }
            }

            RenderUtil.drawRect(
                x,
                y + height,
                1f,
                offset - y - height,
                Colours.mainColour.value.fade(Colours.mainColour.value.darker(), hover.getAnimationFactor())
            )

            if (factor != 1.0 && factor != 0.0) {
                RenderUtil.popScissor()
            }
        }

        if (elements.filter { it.setting.isVisible() }.size > 2) {
            RenderUtil.rotate((90 * expanded.getAnimationFactor()).toFloat(), x + parent.width - 6f, y + 6.5f, 0f) {
                // RenderUtil.drawTriangle(x + parent.width - 6, y + 6.5f, 4f, 6f, Color.DARK_GRAY)

                fun drawTriangle(x: Float, y: Float, width: Float, height: Float, lineWidth: Float, colour: Color) {
                    glDisable(GL_DEPTH_TEST)
                    glDisable(GL_TEXTURE_2D)
                    glEnable(GL_BLEND)
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
                    glDepthMask(true)

                    glEnable(GL_LINE_SMOOTH)
                    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)

                    glEnable(GL_POLYGON_SMOOTH)
                    glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)

                    glLineWidth(lineWidth)

                    colour.glColour()

                    glTranslatef(-(width / 2f), -(height / 2f), 0f)

                    glBegin(GL_LINE_STRIP)

                    glVertex2f(x, y)
                    glVertex2f(x + width, y + height / 2)
                    glVertex2f(x, y + height)

                    glEnd()

                    glTranslatef(width / 2f, height / 2f, 0f)

                    glDisable(GL_LINE_SMOOTH)
                    glDisable(GL_POLYGON_SMOOTH)
                    glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE)
                    glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE)

                    glEnable(GL_TEXTURE_2D)
                    glEnable(GL_DEPTH_TEST)
                    glColor4f(1f, 1f, 1f, 1f)
                }

                drawTriangle(x + parent.width - 6, y + 6.5f, 3f, 5f, 1f, Color.WHITE)
            }
        }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float, click: Click) {
        if (expanded.getAnimationFactor() > 0) {
            elements.forEach {
                if (it.setting.isVisible()) {
                    it.mouseClicked(mouseX, mouseY, click)
                }
            }
        }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float, click: Click) {
        if (expanded.getAnimationFactor() == 1.0) {
            elements.forEach {
                if (it.setting.isVisible()) {
                    it.mouseReleased(mouseX, mouseY, click)
                }
            }
        }
    }

    override fun keyTyped(character: Char, keyCode: Int) {
        if (expanded.getAnimationFactor() == 1.0) {
            elements.forEach {
                if (it.setting.isVisible()) {
                    it.keyTyped(character, keyCode)
                }
            }
        }
    }

    fun getRenderableWidth(): Float {
        return if (elements.filter { it.setting.isVisible() }.size > 2) width - 12f else width
    }

    open fun getAbsoluteHeight(): Float {
        return height + ((elements.filter { it.setting.isVisible() }
            .sumOf { it.getAbsoluteHeight().toDouble() + 1 } + 1) * expanded.getAnimationFactor()).toFloat()
    }

}