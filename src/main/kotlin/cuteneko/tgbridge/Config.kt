package cuteneko.tgbridge

data class Config (
    var botToken: String = "YOUR BOT TOKEN HERE",
    var chatId: Long = 0,
    var telegramAPI: String = "api.telegram.org",
    var pollTimeout: Int = 5,
    var sendChatMessage: Boolean = true,
    var sendGameMessage: Boolean = true,
    var sendTelegramMessage: Boolean = true,
    var messageTrim: Int = 20,
    var sendServerStarted: Boolean = true,
    var sendServerStopping: Boolean = true,
    var minecraftFormat: String = "<%1\$s> %2\$s",
    var telegramFormat: String = "<b>%1\$s</b> %2\$s",
    var serverStartedMessage: String = "Server has started!",
    var serverStoppingMessage: String = "Server is stopping!",
    var admins: List<String> = listOf(),
    var noPermission: String = "No permission!",
    var proxyEnabled: Boolean = false,
    var proxyHost: String = "localhost",
    var proxyPort: Int = 10809,
)