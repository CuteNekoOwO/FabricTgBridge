package cuteneko.tgbridge.tgbot

data class I18n (
    var reply: String = "[Replying to %1\$s: \"%2\$s\"] ",
    var forwarded: String = "[Forwarded] ",
    var forwardedFromUser: String = "Forwarded from user %s ",
    var forwardedFromChannel: String = "Forwarded from channel %s ",
    var forwardedFromGroup: String = "Forwarded from group %s ",
    var photo: String = "[Photo] ",
    var sticker: String = "[%sSticker] ",
    var stickerFrom: String = "From sticker pack %s ",
    var document: String = "[File %s] ",
    var voice: String = "[Voice %ss] ",
    var audio: String = "[Audio %ss] ",
    var video: String = "[Video %ss] ",
    var poll: String = "[Poll %s] ",
    var message: String = "[Message] ",
    var more: String = "[More]"
)