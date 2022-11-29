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
object GraphSentPackets : HUDModule("SentPacketsGraph", "Graph showing the amount of packets you send") {

    private val scale = Setting(
        "Size", 1.0, 0.1, 2.0, 0.1
    ) describedBy "Size of the graph"

    private val updateDelay = Setting(
        "Delay", 250.0, 75.0, 1000.0, 25.0
    )

    private val background = Setting("Background", Graph.Background.ALL)

    private var graph = Graph("SPackets") { background.value }

    private var sentPackets = 0.0
    private var actualSentPackets = 0.0

    val timer = Timer()
    val atimer = Timer()

    override fun onEnable() {
        graph = Graph("SPackets") { background.value }
    }

    override fun onDisable() {
        sentPackets = 0.0
        actualSentPackets = 0.0
        timer.reset()
        atimer.reset()
    }

    override fun onTick() {
        if (minecraft.anyNull) {
            return
        }

        if (atimer.hasMSPassed(1000.0)) {
            sentPackets = actualSentPackets
            actualSentPackets = 0.0
            atimer.reset()
        }

        if (timer.hasMSPassed(updateDelay.value)) {
            graph.update(sentPackets)
            timer.reset()
        }
    }

    @Listener
    fun onPacket(event: PacketEvent.PostSend) {
        actualSentPackets++
    }

    override fun render() {
        graph.bounds.setRect(x, y, 75F, 30F)

        RenderUtil.scaleTo(x, y, 1F, scale.value, scale.value, 1.0) {
            graph.render()
        }
    }

    override var width = 77F
    override var height = 32F

}