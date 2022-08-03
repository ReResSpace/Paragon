package com.paragon.api.util.string

/**
 * @author Surge, SooStrator1136
 */
object StringUtil {

    @JvmStatic
    fun getFormattedText(enumIn: Enum<*>): String {
        val text = enumIn.name
        val formatted = StringBuilder(text.length)
        var isFirst = true
        for (c in text.toCharArray()) {
            if (c == '_') {
                isFirst = true
                continue
            }
            if (isFirst) {
                formatted.append(c.toString().uppercase())
                isFirst = false
            } else {
                formatted.append(c.toString().lowercase())
            }
        }
        return formatted.toString()
    }

    @JvmStatic
    fun wrap(str: String, length: Int): String {
        val result = StringBuilder(str.length)
        var lastDelimPos = 0
        for (token in str.split(" ".toRegex()).toTypedArray()) {
            if (result.length - lastDelimPos + token.length > length) {
                result.append(System.lineSeparator()).append(token)
                lastDelimPos = result.length + 1
            } else {
                result.append(if (result.isEmpty()) "" else " ").append(token)
            }
        }
        return result.toString()
    }

}