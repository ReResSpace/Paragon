package com.paragon.impl.module.hud.impl

import com.paragon.impl.module.client.Colours
import com.paragon.impl.module.hud.HUDModule
import com.paragon.util.render.font.FontUtil
import net.minecraft.util.text.TextFormatting

/**
 * @author Surge
 * @since 01/12/2022
 */
object ServerBrand : HUDModule("ServerBrand", "Shows the brand of the server you are playing on") {

    override fun render() {
        FontUtil.drawStringWithShadow(getText(), x, y, Colours.mainColour.value, alignment.value)
    }

    override var width = FontUtil.getStringWidth(getText())
        get() = FontUtil.getStringWidth(getText())

    override var height = FontUtil.getHeight()
        get() = FontUtil.getHeight()

    private fun getText() = "Server Brand " + TextFormatting.WHITE + if (minecraft.player != null) if (minecraft.isSingleplayer) "Singleplayer" else minecraft.player.serverBrand else ""

}