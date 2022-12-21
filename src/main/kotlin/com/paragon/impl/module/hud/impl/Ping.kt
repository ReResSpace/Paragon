@file:Suppress("SuspiciousVarProperty")

package com.paragon.impl.module.hud.impl

import com.paragon.util.render.font.FontUtil
import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.module.client.Colours
import com.paragon.util.mc
import net.minecraft.util.text.TextFormatting

/**
 * @author Surge
 */
object Ping : HUDModule("Ping", "Displays your ping in ms") {

    override fun render() {
        FontUtil.drawStringWithShadow("Ping: " + TextFormatting.WHITE + getPing() + "ms", x, y, Colours.mainColour.value, alignment.value)
    }

    override var width = FontUtil.getStringWidth("Ping: " + getPing() + "ms")
        get() = FontUtil.getStringWidth("Ping: " + getPing() + "ms")

    override var height = FontUtil.getHeight()
        get() = FontUtil.getHeight()

    fun getPing(): Int = if (mc.connection != null && mc.connection!!.getPlayerInfo(mc.player.uniqueID) != null) {
        mc.connection!!.getPlayerInfo(mc.player.uniqueID).responseTime
    }
    else {
        -1
    }


}