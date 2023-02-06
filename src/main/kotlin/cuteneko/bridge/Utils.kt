package cuteneko.bridge

import net.minecraft.text.LiteralTextContent
import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent

fun Text?.getFormattedString() : String {
    if (this == null) {
        return ""
    }
    Bridge.LOGGER.info(this::class.java.typeName)
    var result = when(val content = this.content) {
        is LiteralTextContent -> {
            content.string.escapeHTML()
        }
        is TranslatableTextContent -> {
            TODO()
        }
        else -> {
            println(content)
            content.toString()
        }
    }
    this.siblings.forEach {
        result += it.getFormattedString()
    }
    if(style.isBold) result = String.format("<b>%s</b>", result)
    if(style.isItalic) result = String.format("<i>%s</i>", result)
    if(style.isUnderlined) result = String.format("<u>%s</u>", result)
    if(style.isStrikethrough) result = String.format("<s>%s</s>", result)
    if(style.isObfuscated) result = String.format("<tg-spoiler>%s</tg-spoiler>", result)

    return result
}

fun String.escapeHTML() = this
    .replace("&", "&amp;")
    .replace(">", "&gt;")
    .replace("<", "&lt;")