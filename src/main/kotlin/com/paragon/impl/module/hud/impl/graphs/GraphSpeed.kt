package com.paragon.impl.module.hud.impl.graphs

import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.module.hud.impl.Speed
import com.paragon.impl.setting.Setting
import com.paragon.util.anyNull
import com.paragon.util.mc
import com.paragon.util.render.RenderUtil.scaleTo

/**
 * @author SooStrator1136
 */
object GraphSpeed : HUDModule("SpeedGraph", "Graph showing your speed") {

    private val scale = Setting(
        "Size", 1.0, 0.1, 2.0, 0.1
    ) describedBy "Size of the graph"

    private val background = Setting("Background", Graph.Background.ALL)

    private var graph = Graph("Speed") { background.value }

    override fun onEnable() {
        graph = Graph("Speed") { background.value }
    }

    override fun onTick() {
        if (mc.anyNull || mc.player.ticksExisted < 10) {
            this.graph.points = Array(75) { 0.0 }
            return
        }

        graph.update(
            Speed.getPlayerSpeed(
                mc.player.posX - mc.player.lastTickPosX, mc.player.posZ - mc.player.lastTickPosZ
            )
        )
    }

    override fun render() {
        graph.bounds.setRect(x, y, 75F, 30F)

        scaleTo(x, y, 1F, scale.value, scale.value, 1.0) {
            graph.render()
        }
    }

    override var width = 75F
    override var height = 30F

}