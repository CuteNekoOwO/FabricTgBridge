@file:OptIn(DelicateCoroutinesApi::class)

package cuteneko.tgbridge.tgbot

import cuteneko.tgbridge.Bridge
import cuteneko.tgbridge.escapeHTML
import cuteneko.tgbridge.toPlainString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import java.util.*


typealias CmdHandler = suspend (HandlerContext) -> Unit

private val meow = listOf("Meow!", "Nya!", "Meow~", "Nya~")


private class MyOutput(val superSource: ServerCommandSource): CommandOutput {
    var commandResult = ""
    var commandTimer: Timer? = null
    override fun sendMessage(message: Text?) {
        superSource.sendMessage(message);
        commandResult += message.toPlainString() + "\n"
        commandTimer?.cancel()
        commandTimer = Timer()
        commandTimer!!.schedule(object : TimerTask() {
            override fun run() {
                if(commandResult.isBlank()) return
                GlobalScope.launch {
                    try {
                        Bridge.BOT.sendMessageToTelegram(commandResult)
                    } finally {
                        commandResult = ""
                    }
                }
            }
        }, 100)
        //GlobalScope.launch { Bridge.BOT.api.sendMessage(Bridge.CONFIG.chatId, message.toPlainString()) }
    }

    override fun shouldReceiveFeedback() = true

    override fun shouldTrackOutput() = true

    override fun shouldBroadcastConsoleToOps() = true

}

private val myOutput = MyOutput(Bridge.SERVER.commandSource)

val TgBot.commandMap: Map<String?, CmdHandler>
    get() = mapOf(
        "chat_id" to ::chatIdHandler,
        "list" to ::listHandler,
        "meow" to ::meowHandler,
        "cmd" to ::commandHandler
    )

suspend fun TgBot.chatIdHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    val chatId = msg.chat.id
    val text = "Chat ID: <code>$chatId</code>."
    this.sendMessageToTelegram(text, reply = msg.messageId)
}

suspend fun TgBot.listHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    if(msg.chat.id != Bridge.CONFIG.chatId) return
    val players = Bridge.SERVER.playerManager.playerList
    var list = players.joinToString("\n") { it.displayName.toPlainString().escapeHTML() }
    if(list.isBlank()) list = "No players online." // TODO: i18n
    this.sendMessageToTelegram(list, reply =  msg.messageId)
}

suspend fun TgBot.meowHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    this.sendMessageToTelegram(meow.shuffled()[0], reply = msg.messageId)
}

suspend fun TgBot.commandHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    val cmd = ctx.commandArgs.subList(1, ctx.commandArgs.size).joinToString(" ")
    if(!Bridge.CONFIG.admins.contains(msg.from?.username)) {
        this.sendMessageToTelegram(Bridge.CONFIG.noPermission, reply = msg.messageId)
        return
    }
    val cmdMgr = Bridge.SERVER.commandManager
    Bridge.SERVER.commandSource.sendMessage(Text.literal("Executing command: /$cmd"))
    val source = Bridge.SERVER.commandSource.withOutput(myOutput)
    cmdMgr.executeWithPrefix(source, cmd)
    //cmdMgr.dispatcher.execute(cmd, source)

}
