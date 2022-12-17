package com.paragon.impl.command

import com.paragon.Paragon
import com.paragon.impl.command.syntax.SyntaxBuilder
import com.paragon.util.Wrapper
import net.minecraft.util.text.TextFormatting

/**
 * @author Surge
 */
abstract class Command(val name: String, val syntax: SyntaxBuilder) : Wrapper {

    abstract fun call(args: Array<String>, fromConsole: Boolean): Boolean

    fun sendMessage(message: String) = Paragon.INSTANCE.commandManager.sendClientMessage(message)
    fun sendInvalidSyntaxMessage() = sendMessage("${TextFormatting.RED}Invalid syntax! Run '\$syntax $name' to get the correct syntax.")

}