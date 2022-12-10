package com.paragon.impl.module.hud.impl.graphs

import com.paragon.bus.listener.Listener
import com.paragon.impl.event.network.PacketEvent
import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.setting.Setting
import com.paragon.util.anyNull
import com.paragon.util.calculations.Timer
import com.paragon.util.render.RenderUtil
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.network.play.client.CPacketUseEntity

/**
 * @author SooStrator1136
 */
object GraphCrystals : HUDModule("CrystalsGraph", "Graph showing the amount of crystals you attack") {

    private val scale = Setting(
        "Size", 1.0, 0.1, 2.0, 0.1
    ) describedBy "Size of the graph"

    private val updateDelay = Setting(
        "Delay", 250.0, 75.0, 1000.0, 25.0
    )

    private val background = Setting("Background", Graph.Background.ALL)

    private var graph = Graph("Crystals") { background.value }

    override fun onEnable() {
        graph = Graph("Crystals") { background.value }
    }

    override fun onDisable() {
        attackedCrystals = 0.0
        actualACrystals = 0.0
        timer.reset()
        atimer.reset()
    }

    private var attackedCrystals = 0.0
    private var actualACrystals = 0.0

    val timer = Timer()
    val atimer = Timer()

    override fun onTick() {
        if (minecraft.anyNull) {
            return
        }

        if (atimer.hasMSPassed(1000.0)) {
            attackedCrystals = actualACrystals
            actualACrystals = 0.0
            atimer.reset()
        }

        if (timer.hasMSPassed(updateDelay.value)) {
            graph.update(attackedCrystals)
            timer.reset()
        }
    }

    @Listener
    fun onPacket(event: PacketEvent.PostSend) {
        if (event.packet is CPacketUseEntity && event.packet.action == CPacketUseEntity.Action.ATTACK &&  event.packet.getEntityFromWorld(minecraft.world) is EntityEnderCrystal) {
            actualACrystals++
        }
    }

    override fun render() {
        graph.bounds.setRect(x, y, 75F, 30F)

        RenderUtil.scaleTo(x, y, 1F, scale.value, scale.value, 1.0) {
            graph.render()
        }
    }

    override var width = 75F
    override var height = 30F

}