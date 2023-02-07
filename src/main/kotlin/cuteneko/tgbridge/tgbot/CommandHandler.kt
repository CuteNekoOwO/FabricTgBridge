package cuteneko.tgbridge.tgbot

import cuteneko.tgbridge.Bridge
import cuteneko.tgbridge.toPlainString
import net.minecraft.server.command.ServerCommandSource
import com.mojang.brigadier.ResultConsumer
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.server.command.CommandOutput
import net.minecraft.text.Text
import java.util.*


typealias CmdHandler = suspend (HandlerContext) -> Unit

private val meow = listOf("Meow!", "Nya!", "Meow~", "Nya~")


private class MyOutput(): CommandOutput {
    var commandResult = ""
    var commandTimer: Timer? = null
    override fun sendMessage(message: Text?) {
        commandResult += message.toPlainString() + "\n"
        commandTimer?.cancel()
        commandTimer = Timer()
        commandTimer!!.schedule(object : TimerTask() {
            override fun run() {
                if(commandResult.isNullOrBlank()) return
                GlobalScope.launch {
                    try {
                        Bridge.BOT.api.sendMessage(Bridge.CONFIG.chatId, commandResult)
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

private val myOutput = MyOutput()

val TgBot.commandMap: Map<String?, CmdHandler>
    get() = mapOf(
        "chatID" to ::chatIdHandler,
        "online" to ::onlineHandler,
        "meow" to ::meowHandler,
        "cmd" to ::commandHandler
    )

suspend fun TgBot.chatIdHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    val chatId = msg.chat.id
    val text = "Chat ID: <code>$chatId</code>."
    api.sendMessage(chatId, text, msg.messageId)
}

suspend fun TgBot.onlineHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    if(msg.chat.id != Bridge.CONFIG.chatId) return
    val players = Bridge.SERVER.playerManager.playerList
    val list = players.joinToString("\n") { it.displayName.toPlainString() }
    api.sendMessage(msg.chat.id, list, msg.messageId)
}

suspend fun TgBot.meowHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    api.sendMessage(msg.chat.id, meow.shuffled()[0], msg.messageId)
}

suspend fun TgBot.commandHandler(ctx: HandlerContext) {
    val msg = ctx.message!!
    val cmd = ctx.commandArgs.subList(1, ctx.commandArgs.size).joinToString(" ")
    if(!Bridge.CONFIG.admins.contains(msg.from?.username)) {
        api.sendMessage(msg.chat.id, Bridge.CONFIG.noPermission, msg.messageId)
        return
    }
    val cmdMgr = Bridge.SERVER.commandManager
    val source = Bridge.SERVER.commandSource.withOutput(myOutput)
    cmdMgr.executeWithPrefix(source, cmd)
    //cmdMgr.dispatcher.execute(cmd, source)

}
