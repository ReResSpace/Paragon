package com.paragon.impl.module.hud.impl.graphs

import com.paragon.bus.listener.Listener
import com.paragon.impl.event.network.PacketEvent
import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.setting.Setting
import com.paragon.util.anyNull
import com.paragon.util.calculations.Timer
import com.paragon.util.render.RenderUtil

/**
 * @author SooStrator1136
 */
object GraphReceivedPackets : HUDModule("ReceivedPacketsGraph", "Graph showing the amount of packets you have received") {

    private val scale = Setting(
        "Size", 1.0, 0.1, 2.0, 0.1
    ) describedBy "Size of the graph"

    private val updateDelay = Setting(
        "Delay", 250.0, 75.0, 1000.0, 25.0
    )

    private val background = Setting("Background", Graph.Background.ALL)

    private var graph = Graph("Received") { background.value }

    private var receivedPackets = 0.0
    private var actualReceivedPackets = 0.0

    val timer = Timer()
    val atimer = Timer()

    override fun onEnable() {
        graph = Graph("RPackets") { background.value }
    }

    override fun onDisable() {
        receivedPackets = 0.0
        actualReceivedPackets = 0.0
        timer.reset()
        atimer.reset()
    }

    override fun onTick() {
        if (minecraft.anyNull) {
            return
        }

        if (atimer.hasMSPassed(1000.0)) {
            receivedPackets = actualReceivedPackets
            actualReceivedPackets = 0.0
            atimer.reset()
        }

        if (timer.hasMSPassed(updateDelay.value)) {
            graph.update(receivedPackets)
            timer.reset()
        }
    }

    @Listener
    fun onPacket(event: PacketEvent.PostReceive) {
        actualReceivedPackets++
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