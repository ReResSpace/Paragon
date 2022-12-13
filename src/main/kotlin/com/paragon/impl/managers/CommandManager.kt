package com.paragon.impl.managers

import com.paragon.Paragon
import com.paragon.impl.command.Command
import com.paragon.impl.command.impl.*
import com.paragon.impl.command.syntax.Argument
import com.paragon.impl.command.syntax.SyntaxBuilder
import com.paragon.impl.managers.notifications.Notification
import com.paragon.impl.managers.notifications.NotificationType
import com.paragon.impl.setting.Bind
import com.paragon.impl.setting.Setting
import com.paragon.util.Wrapper
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting.*
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.*

/**
 * @author Surge
 */
class CommandManager : Wrapper {

    val prefix = "$"
    var lastCommand = ""

    val commands = arrayListOf(
        ConfigCommand, CopySkinCommand, HelpCommand, OpenFolderCommand, SaveMapCommand, FriendCommand, SyntaxCommand, SizeCommand, NearestStronghold
    )

    val commonPrefixes = listOf("/", ".", "*", ";", ",") as MutableList<String>

    init {
        Paragon.INSTANCE.moduleManager.modules.forEach {
            val command = object : Command(it.name, SyntaxBuilder()
                .addArgument(Argument("setting", it.settings.map { s -> s.name.replace(" ", "") }.toTypedArray()))
                .addArgument(Argument("value", arrayOf("any_str")))
            ) {
                override fun whenCalled(args: Array<String>, fromConsole: Boolean) {
                    if (args.isNotEmpty()) {
                        val setting = it.settings
                            .find { s -> s.name.replace(" ", "").equals(args[0], true) }

                        if (setting == null) {
                            Paragon.INSTANCE.notificationManager.addNotification(
                                Notification("Invalid setting: ${args[0]}", NotificationType.ERROR)
                            )
                            return
                        }

                        if (args.size == 1) {
                            Paragon.INSTANCE.notificationManager.addNotification(
                                Notification("${args[0]}: ${setting.value}", NotificationType.INFO)
                            )
                            return
                        }
                        try {
                            when (setting.value!!) {
                                is Boolean -> (setting as Setting<Boolean?>).setValue("true" == args[1])
                                is String -> (setting as Setting<String?>).setValue(args.drop(1).joinToString { " " })
                                is Bind -> {
                                    val bind = setting.value as Bind
                                    bind.buttonCode = Keyboard.getKeyIndex(args[1])
                                    bind.device = Bind.Device.KEYBOARD
                                }
                                is Int -> (setting as Setting<Int?>).setValue(args[1].toIntOrNull() ?: setting.value)
                                is Double -> (setting as Setting<Double?>).setValue(
                                    args[1].toDoubleOrNull() ?: setting.value
                                )
                                is Float -> (setting as Setting<Float?>).setValue(
                                    args[1].toFloatOrNull() ?: setting.value
                                )
                                is Enum<*> -> {
                                    val enum = setting.value as Enum<*>
                                    var value: Enum<*>? = null

                                    run breakLoop@{
                                        enum::class.java.enumConstants.forEachIndexed { index, enumValue ->
                                            if (enumValue.name.equals(args[1], true)) {
                                                setting.index = index
                                                value = enumValue
                                                return@breakLoop
                                            }
                                        }
                                    }

                                    (setting as Setting<Enum<*>>).setValueRaw(value!!)
                                }
                                is Color -> {
                                    val values = args[1].split(":".toRegex()).toTypedArray()

                                    val color = Color(
                                        values[0].toInt() / 255f,
                                        values[1].toInt() / 255f,
                                        values[2].toInt() / 255f,
                                        values[3].toFloat() / 255f
                                    )

                                    setting.isRainbow = java.lang.Boolean.parseBoolean(values[4])
                                    setting.rainbowSpeed = values[5].toFloat()
                                    setting.rainbowSaturation = values[6].toFloat()
                                    setting.isSync = java.lang.Boolean.parseBoolean(values[7])
                                    (setting as Setting<Color?>).setValue(color)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Paragon.INSTANCE.notificationManager.addNotification(
                                Notification("Unable to change value of ${args[0]} to ${args[1]}", NotificationType.ERROR)
                            )
                            return
                        }

                        Paragon.INSTANCE.notificationManager.addNotification(
                            Notification("Successfully changed value of ${args[0]} to ${setting.value}", NotificationType.INFO)
                        )
                    }
                }
            }

            commands.add(command)
        }

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
                sendClientMessage(RED.toString() + "Command not found!")
            }
        }
    }

    fun sendClientMessage(message: String) {
        minecraft.player.sendMessage(TextComponentString(LIGHT_PURPLE.toString() + "Paragon " + WHITE + "> " + message))
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatEvent) {
        // Check if the message starts with the prefix
        if (event.message.startsWith(prefix)) {
            event.isCanceled = true
            handleCommands(event.message.substring(prefix.length), false)
        }
    }

    fun startsWithPrefix(message: String) = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].lowercase(Locale.getDefault()).startsWith(prefix.lowercase()) || commonPrefixes.contains(
        message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].lowercase(Locale.getDefault())
    )

}