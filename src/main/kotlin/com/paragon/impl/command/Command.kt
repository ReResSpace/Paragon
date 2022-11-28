package com.paragon.impl.command

import com.paragon.impl.command.syntax.SyntaxBuilder
import com.paragon.util.Wrapper

/**
 * @author Surge
 */
abstract class Command(val name: String, val syntax: SyntaxBuilder) : Wrapper {

    abstract fun whenCalled(args: Array<String>, fromConsole: Boolean)

}