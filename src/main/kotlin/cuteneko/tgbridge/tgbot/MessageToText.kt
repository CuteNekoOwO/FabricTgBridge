package cuteneko.tgbridge.tgbot

import cuteneko.tgbridge.ConfigLoader
import cuteneko.tgbridge.rawUserMention
import cuteneko.tgbridge.toPlainString
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

val i18n = ConfigLoader.getI18n()
fun Message.toText(trim: Int = 20, showMore: Boolean = true): Text {
    val text = Text.empty()

    replyToMessage?.let {
        text.append(
            Text.literal(
                i18n.reply.format(
                    it.from?.rawUserMention(),
                    it.toText(10, false)
                )
            )
        ).setStyle(
            Style.EMPTY
                .withColor(Formatting.GOLD)
                .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, it.toText()))
        )
    }

    forwardFrom?.let {
        val info = Text.empty()
        info.append(i18n.forwardedFromUser.format(it.rawUserMention()))
        it.username?.let { username -> info.append("\n$username") }
        text.append(
            Text.literal(i18n.forwarded)
                .setStyle(
                    Style.EMPTY
                        .withColor(Formatting.GOLD)
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, info))
                )
        )
    }

    forwardFromChat?.let {
        val info = Text.empty()
        when (it.type) {
            "channel" -> info.append(i18n.forwardedFromChannel.format(it.title))
            "group" -> info.append(i18n.forwardedFromGroup.format(it.title))
            else -> {}
        }
        text.append(
            Text.literal(i18n.forwarded)
                .setStyle(
                    Style.EMPTY
                        .withColor(Formatting.GOLD)
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, info))
                )
        )
    }

    if (!photo.isNullOrEmpty()) {
        text.append(
            Text.literal(i18n.photo)
                .setStyle(
                    Style.EMPTY
                        .withColor(Formatting.BLUE)
                )
        )
    }

    sticker?.let {
        text.append(
            Text.literal(i18n.sticker.format(it.emoji))
                .setStyle(
                    Style.EMPTY
                        .withColor(Formatting.BLUE)
                        .withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text.literal(i18n.stickerFrom.format(it.setName))
                            )
                        )
                )
        )
    }

    document?.let {
        text.append(
            Text.literal(i18n.document.format(it.fileName))
                .setStyle(
                    Style.EMPTY.withColor(Formatting.BLUE)
                )
        )
    }

    voice?.let {
        text.append(
            Text.literal(i18n.voice.format(it.duration))
                .setStyle(
                    Style.EMPTY.withColor(Formatting.BLUE)
                )
        )
    }

    audio?.let {
        text.append(
            Text.literal(i18n.audio.format(it.duration))
                .setStyle(
                    Style.EMPTY.withColor(Formatting.BLUE)
                )
        )
    }

    video?.let {
        text.append(
            Text.literal(i18n.video.format(it.duration))
                .setStyle(
                    Style.EMPTY.withColor(Formatting.BLUE)
                )
        )
    }

    poll?.let {
        text.append(
            Text.literal(i18n.poll.format(it.question.trimMessage(trim, showMore).toPlainString(false)))
                .setStyle(
                    Style.EMPTY.withColor(Formatting.BLUE)
                )
        )
    }

    this.text?.let {
        text.append(it.trimMessage(trim, showMore))
    }

    if (text.siblings.isEmpty()) {
        text.append(
            Text.literal(i18n.message)
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))
        )
    }

    caption?.let {
        text.append(it.trimMessage(trim, showMore))
    }

    return text
}

private fun String.trimMessage(size: Int, showMore: Boolean = true): Text {
    val msg = replace("\n", " ")
    val text = Text.empty()
    if(size == 0 || this.length <= size) {
        text.append(msg)
        return text
    }
    text.append("${this.substring(0, size)}...")
    if(showMore) {
        text.append(
            Text.literal(i18n.more)
                .setStyle(
                    Style.EMPTY
                        .withColor(Formatting.GOLD)
                        .withHoverEvent(
                            HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, Text.literal(this)
                        )
                        )
                )
        )
    }
    return text
}