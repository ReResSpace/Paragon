package com.paragon.impl.module.hud.impl

import com.paragon.bus.listener.Listener
import com.paragon.impl.event.network.PacketEvent
import com.paragon.impl.module.client.Colours
import com.paragon.impl.module.hud.HUDModule
import com.paragon.util.calculations.Timer
import com.paragon.util.mc
import com.paragon.util.render.font.FontUtil
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.util.text.TextFormatting

object CPS : HUDModule("CPS", "Display Crystals per Second in text") {
    private var attackedCrystals = 0.0
    private var actualACrystals = 0.0
    val timer = Timer()

    override fun render() {
        if (timer.hasMSPassed(1000.0)) {
            attackedCrystals = actualACrystals
            actualACrystals = 0.0
            timer.reset()
        }
        FontUtil.drawStringWithShadow(getText(), x, y, Colours.mainColour.value, alignment.value)
    }

    override var width = FontUtil.getStringWidth(getText())
        get() = FontUtil.getStringWidth(getText())

    override var height = FontUtil.getHeight()
        get() = FontUtil.getHeight()

    private fun getText() = "CPS " + TextFormatting.WHITE + attackedCrystals

    @Listener
    fun onPacket(event: PacketEvent.PostSend) {
        if (event.packet is CPacketUseEntity && event.packet.action == CPacketUseEntity.Action.ATTACK && event.packet.getEntityFromWorld(
                mc.world) is EntityEnderCrystal) {
            actualACrystals++
        }
    }
}