package com.paragon.impl.module.misc

import com.paragon.impl.module.Category
import com.paragon.impl.module.Module

/**
 * @author Surge
 * @since 26/11/2022
 */
object AutoSable : Module("AutoSable", Category.MISC, "Does sable things") {

    override fun onEnable() {
        minecraft.player.sendChatMessage("i just got ratted by sable")

        // easy crash
        println((null as Int).plus(5))
    }

}