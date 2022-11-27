package com.paragon.impl.command.syntax

/**
 * @author Surge
 * @since 27/11/2022
 */
class SyntaxBuilder {

    val arguments = arrayListOf<Argument>()

    fun addArgument(argument: Argument): SyntaxBuilder {
        arguments.add(argument)

        return this
    }

    fun join(): String {
        return arguments.joinToString { "[${it.name}: <${it.valid.joinToString("|", transform = { valid -> valid } )}>]" }
    }

}