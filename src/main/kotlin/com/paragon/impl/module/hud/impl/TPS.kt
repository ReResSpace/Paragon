package com.paragon.impl.module.hud.impl

import com.paragon.Paragon
import com.paragon.impl.module.client.Colours
import com.paragon.impl.module.hud.HUDModule
import com.paragon.util.render.font.FontUtil
import com.paragon.util.system.CircularArray
import net.minecraft.util.text.TextFormatting

object TPS : HUDModule("TPS", "Display server's TPS") {
    private val tpsBuffer = CircularArray.create(20, 20f)
    private val newTpsBuffer = CircularArray.create(20, 20f)

    override fun render() {
        tpsBuffer.add(Paragon.INSTANCE.lagCompensator.tickRate)
        newTpsBuffer.add(Paragon.INSTANCE.tpsManager.tickRate)

        FontUtil.drawStringWithShadow(getText(), x, y, Colours.mainColour.value, alignment.value)
    }

    override var width = FontUtil.getStringWidth(getText())
        get() = FontUtil.getStringWidth(getText())

    override var height = FontUtil.getHeight()
        get() = FontUtil.getHeight()

    private fun getText() = "TPS " + TextFormatting.WHITE + "%.2f".format(tpsBuffer.average()) +
            " " + TextFormatting.GRAY + "[" + TextFormatting.WHITE + "%.2f".format(newTpsBuffer.average()) + TextFormatting.GRAY + "]"
}