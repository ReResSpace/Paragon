package com.paragon.impl.command

import com.paragon.Paragon
import com.paragon.impl.command.syntax.SyntaxBuilder
import com.paragon.util.Wrapper

/**
 * @author Surge
 */
abstract class Command(val name: String, val syntax: SyntaxBuilder) : Wrapper {

    abstract fun whenCalled(args: Array<String>, fromConsole: Boolean)

    fun sendMessage(message: String) = Paragon.INSTANCE.commandManager.sendClientMessage(message)

}