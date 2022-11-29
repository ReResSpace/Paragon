package com.paragon.impl.module.hud.impl.graphs

import com.paragon.impl.module.hud.HUDModule
import com.paragon.impl.setting.Setting
import com.paragon.util.anyNull
import com.paragon.util.render.RenderUtil
import net.minecraft.client.Minecraft

/**
 * @author SooStrator1136
 */
object GraphFPS : HUDModule("FPSGraph", "Graph showing your Ping") {

    private val scale = Setting(
        "Size", 1.0, 0.1, 2.0, 0.1
    ) describedBy "Size of the graph"

    private val background = Setting("Background", Graph.Background.ALL)

    private var graph = Graph("FPS") { background.value }

    override fun onEnable() {
        graph = Graph("FPS") { background.value }
    }

    override fun onTick() {
        if (minecraft.anyNull) {
            return
        }

        graph.update(Minecraft.getDebugFPS().toDouble())
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