package com.paragon.impl.event

import com.paragon.Paragon
import com.paragon.bus.listener.Listener
import com.paragon.impl.event.network.PacketEvent.PreReceive
import com.paragon.impl.event.network.PlayerEvent.PlayerJoinEvent
import com.paragon.impl.event.network.PlayerEvent.PlayerLeaveEvent
import com.paragon.impl.event.render.gui.RenderChatGuiEvent
import com.paragon.util.Wrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.network.play.server.SPacketPlayerListItem
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color

class EventFactory : Wrapper {

    init {
        MinecraftForge.EVENT_BUS.register(this)
        Paragon.INSTANCE.eventBus.register(this)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent?) {
        Paragon.INSTANCE.moduleManager.modules.forEach {
            if (it.isEnabled) {
                it.onTick()
            }

            if (it.bind.value.isPressed() && Minecraft.getMinecraft().currentScreen == null) {
                Paragon.INSTANCE.eventBus.unregister(it)
                it.toggle()
            }
        }
    }

    @SubscribeEvent
    fun onRender2D(event: RenderGameOverlayEvent.Post) {
        if (event.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            Paragon.INSTANCE.moduleManager.modules.forEach {
                if (it.isEnabled) {
                    it.onRender2D()
                }
            }
        }
    }

    @SubscribeEvent
    fun onRender3D(event: RenderWorldLastEvent?) {
        Paragon.INSTANCE.moduleManager.modules.forEach {
            if (it.isEnabled) {
                it.onRender3D()
            }
        }
    }

    @Listener
    fun onPacketReceive(event: PreReceive) {
        if (event.packet is SPacketPlayerListItem) {
            val packet = event.packet

            when (packet.action) {
                SPacketPlayerListItem.Action.ADD_PLAYER -> packet.entries.forEach { entry: AddPlayerData ->
                    if (entry.profile.name != null) {
                        Paragon.INSTANCE.eventBus.post(
                            PlayerJoinEvent(entry.profile.name)
                        )
                    }
                }

                SPacketPlayerListItem.Action.REMOVE_PLAYER -> packet.entries.forEach { entry: AddPlayerData ->
                    if (entry.profile.name != null) {
                        Paragon.INSTANCE.eventBus.post(
                            PlayerLeaveEvent(entry.profile.name)
                        )
                    }
                }

                else -> {}
            }
        }
    }

    @Listener
    fun onRenderChatGui(event: RenderChatGuiEvent) {
        if (event.text.startsWith(Paragon.INSTANCE.commandManager.prefix)) {
            val full = event.text.substring(1, event.text.length).split(" ").toMutableList()

            // no command
            if (full[0].isEmpty()) {
                return
            }

            // get command
            val command = Paragon.INSTANCE.commandManager.commands.firstOrNull { it.name.startsWith(full[0], true) } ?: return
            val givenArguments = arrayListOf<String>()

            // oh my
            full.forEachIndexed { index, part ->
                if (index > 0) {
                    givenArguments.add(part)
                }
            }

            // COMPLETE SYNTAX

            var nonTypedSyntax = ""
            var markInvalid = false

            // command name
            if (!nonTypedSyntax.contains(command.name, true)) {
                nonTypedSyntax += command.name.replaceFirst(full[0], "", true)
            }

            command.syntax.arguments.forEachIndexed { index, argument ->
                // LMAOOOOOO WHAT THE FUCK
                // Throwback to https://github.com/momentumdevelopment/cosmos/pull/171/commits/816b5636b68378226f3570ce8fc6ae7946bdaa0f
                runCatching {
                    val given = givenArguments[index]

                    if (given.isEmpty()) {
                        nonTypedSyntax += "<${argument.valid.joinToString("|", transform = { valid -> valid })}>".replace("any_str", argument.name)
                    } else if (!argument.isComplete(givenArguments[index])) {
                        val firstValid = argument.getFirstValidOption(givenArguments[index]).replace(givenArguments[index], "", true)

                        nonTypedSyntax += firstValid.ifEmpty {
                            markInvalid = true
                            ""
                        }
                    }
                }.onFailure {
                    nonTypedSyntax += " <${argument.valid.joinToString("|", transform = { valid -> valid } )}>".replace("any_str", argument.name)
                }
            }

            // draw behind text
            minecraft.fontRenderer.drawStringWithShadow(
                (if (markInvalid) "${TextFormatting.RED}" else "") + nonTypedSyntax,
                4f + minecraft.fontRenderer.getStringWidth(event.text),
                ScaledResolution(minecraft).scaledHeight - 12f,
                Color.GRAY.rgb
            )
        }
    }

}