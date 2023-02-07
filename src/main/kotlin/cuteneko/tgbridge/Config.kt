package cuteneko.tgbridge

data class Config (
    var botToken: String = "",
    var chatId: Long = 0,
    var telegramAPI: String = "api.telegram.org",
    var pollTimeout: Int = 5,
    var sendChatMessage: Boolean = true,
    var sendGameMessage: Boolean = true,
    var sendTelegramMessage: Boolean = true,
    var sendServerStarted: Boolean = true,
    var sendServerStopping: Boolean = true,
    var minecraftFormat: String = "<%1\$s> %2\$s",
    var telegramFormat: String = "<b>%1\$s</b> %2\$s",
    var serverStartedMessage: String = "Server has started!",
    var serverStoppingMessage: String = "Server is stopping!",
    var admins: List<String> = listOf(),
    var noPermission: String = "No permission!"
)