package com.paragon.impl.module.hud.impl

import com.paragon.util.render.font.FontUtil.drawStringWithShadow
import com.paragon.util.render.font.FontUtil.getHeight
import com.paragon.util.render.font.FontUtil.getStringWidth
import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.module.client.Colours
import com.paragon.util.mc
import net.minecraft.util.text.TextFormatting
import kotlin.math.roundToInt

object Coordinates : HUDModule("Coordinates", "Displays your coordinates") {

    override fun render() {
        drawStringWithShadow(text, x, y, Colours.mainColour.value, alignment.value)
    }

    override var width: Float
        get() = getStringWidth(text)
        set(width) {
            super.width = width
        }

    override var height: Float
        get() = getHeight()
        set(height) {
            super.height = height
        }

    val text: String
        get() = "X " + TextFormatting.WHITE + mc.player.posX.roundToInt() + TextFormatting.RESET + " Y " + TextFormatting.WHITE + mc.player.posY.roundToInt() + TextFormatting.RESET + " Z " + TextFormatting.WHITE + mc.player.posZ.roundToInt()

}