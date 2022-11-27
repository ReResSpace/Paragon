package com.paragon.impl.command.impl

import com.paragon.Paragon
import com.paragon.impl.command.Command
import com.paragon.impl.command.syntax.SyntaxBuilder

/**
 * @author Surge
 */
object HelpCommand : Command("Help", SyntaxBuilder()) {

    override fun whenCalled(args: Array<String>, fromConsole: Boolean) {
        Paragon.INSTANCE.commandManager.commands.forEach {
            Paragon.INSTANCE.commandManager.sendClientMessage(it.name, fromConsole)
        }
    }

}