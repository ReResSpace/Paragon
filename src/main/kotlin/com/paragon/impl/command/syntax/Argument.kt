package com.paragon.impl.command.syntax

/**
 * @author Surge
 * @since 27/11/2022
 */
class Argument(val name: String, val valid: Array<String>, val excludedBy: Array<Pair<String, String>> = arrayOf()) {

    fun isComplete(input: String): Boolean {
        return valid.any { it.equals(input, true) }
    }

    fun getFirstValidOption(input: String): String {
        if (!valid.any { it.startsWith(input, true) }) {
            return ""
        }

        return valid.first { it.startsWith(input, true) }.replace("any_str", name)
    }

}