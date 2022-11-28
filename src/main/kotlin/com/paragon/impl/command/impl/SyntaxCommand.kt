package com.paragon.impl.command.impl

import com.paragon.Paragon
import com.paragon.impl.command.Command
import com.paragon.impl.command.syntax.Argument
import com.paragon.impl.command.syntax.SyntaxBuilder
import net.minecraft.util.text.TextFormatting

/**
 * @author Surge
 */
object SyntaxCommand : Command("Syntax", SyntaxBuilder()
        .addArgument(Argument("command", arrayOf("any_str")))
) {

    override fun whenCalled(args: Array<String>, fromConsole: Boolean) {
        if (args.size == 1) {
            for (command in Paragon.INSTANCE.commandManager.commands) {
                if (command.name.equals(args[0], true)) {
                    Paragon.INSTANCE.commandManager.sendClientMessage(command.name + " " + command.syntax.join(), fromConsole)
                    break
                }
            }
        }
        else {
            Paragon.INSTANCE.commandManager.sendClientMessage(
                TextFormatting.RED.toString() + "Invalid syntax!", fromConsole
            )
        }
    }

}