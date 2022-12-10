package com.paragon.impl.command.syntax

/**
 * @author Surge
 * @since 28/11/2022
 */
data class ArgumentData(val name: String, val valid: Array<String>, val visibleWhen: Array<Pair<String, String>> = arrayOf())