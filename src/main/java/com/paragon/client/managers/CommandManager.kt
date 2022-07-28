package com.paragon.client.managers

import com.paragon.Paragon
import com.paragon.api.util.Wrapper
import com.paragon.client.systems.command.impl.*
import com.paragon.client.systems.module.impl.misc.Cryptic
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

/**
 * @author Surge
 */
class CommandManager : Wrapper {

    private val prefix = "$"
    var lastCommand = ""

    val commands = arrayOf(
        ConfigCommand,
        HelpCommand,
        OpenFolderCommand,
        SocialCommand,
        SyntaxCommand,
        SizeCommand
    )

    val commonPrefixes = listOf("/", ".", "*", ";", ",") as MutableList<String>

    init {
        MinecraftForge.EVENT_BUS.register(this)
        Paragon.INSTANCE.logger.info("Loaded Command Manager")
    }

    fun handleCommands(message: String, fromConsole: Boolean) {
        if (message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().isNotEmpty()) {
            var commandFound = false
            val commandName = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            for (command in commands) {
                if (command.name.equals(commandName, ignoreCase = true)) {
                    command.whenCalled(message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().copyOfRange(1, message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size), fromConsole)
                    lastCommand = prefix + message
                    commandFound = true
                    break
                }
            }
            if (!commandFound) {
                sendClientMessage(TextFormatting.RED.toString() + "Command not found!", fromConsole)
            }
        }
    }

    fun sendClientMessage(message: String, fromConsole: Boolean) {
        // Only send chat message if the message wasn't sent from the console
        if (!fromConsole) {
            minecraft.player.sendMessage(TextComponentString(TextFormatting.LIGHT_PURPLE.toString() + "Paragon " + TextFormatting.WHITE + "> " + message))
        }
        Paragon.INSTANCE.console.addLine(TextFormatting.LIGHT_PURPLE.toString() + "Paragon " + TextFormatting.WHITE + "> " + message)
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatEvent) {
        // Check if the message starts with the prefix
        if (event.message.startsWith(prefix)) {
            event.isCanceled = true
            handleCommands(event.message.substring(prefix.length), false)
        }
    }

    fun startsWithPrefix(message: String) = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()[0].lowercase(Locale.getDefault())
        .startsWith(prefix.lowercase()) || commonPrefixes.contains(
        message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].lowercase(Locale.getDefault())
    ) || message.startsWith("crypt") && Cryptic.isEnabled //WTF

}