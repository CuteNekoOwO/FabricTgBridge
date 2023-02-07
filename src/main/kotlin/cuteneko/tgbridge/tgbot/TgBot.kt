package cuteneko.tgbridge.tgbot

import cuteneko.tgbridge.Bridge
import cuteneko.tgbridge.rawUserMention
import cuteneko.tgbridge.tgbot.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import net.minecraft.text.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class TgBot {
    private val config = Bridge.CONFIG
    private val api = Retrofit.Builder()
        .baseUrl("https://${config.telegramAPI}/bot${config.botToken}/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TgApi::class.java)

    private val updateChan = Channel<Update>()
    private var pollJob: Job? = null
    private var handlerJob: Job? = null
    private var currentOffset: Long = -1
    private var me: User? = null


    private suspend fun initialize() {
        me = api.getMe().result!!
    }


    suspend fun startPolling() {
        initialize()
        pollJob = initPolling()
        handlerJob = initHandler()
    }

    suspend fun stop() {
        pollJob?.cancelAndJoin()
        handlerJob?.join()
    }



    private fun initPolling() = GlobalScope.launch {
        loop@while (true) {
            try {
                api.getUpdates(
                    offset = currentOffset,
                    timeout = config.pollTimeout,
                ).result?.let { updates ->
                    if (updates.isNotEmpty()) {
                        updates.forEach { updateChan.send(it) }
                        currentOffset = updates.last().updateId + 1
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> break@loop
                    else -> {
                        e.printStackTrace()
                        continue@loop
                    }
                }
            }
        }
        updateChan.close()
    }

    private fun initHandler() = GlobalScope.launch {
        updateChan.consumeEach {
            try {
                handleUpdate(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleUpdate(update: Update) {
        // Ignore private message or channel post
        if (update.message?.chat?.type != "group" && update.message?.chat?.type != "supergroup")
            return
        val ctx = HandlerContext(
            update,
            update.message,
            update.message.chat,
        )
        update.message.let {
            if (it.text?.startsWith("/") == true) {
                val args = it.text.split(" ")
                val cmd = if (args[0].contains("@")) {
                    val cmds = args[0].split("@")
                    if (cmds[1] != me!!.username) return
                    cmds[0].substring(1)
                } else args[0].substring(1)

            }
            onMessageHandler(ctx)
        }
    }


    private fun onMessageHandler(
        @Suppress("unused_parameter") ctx: HandlerContext
    ) {
        val msg = ctx.message!!
        if (config.chatId != msg.chat.id) {
            return
        }
        val txt = config.minecraftFormat.replace("%1\$s", msg.from?.rawUserMention()!!)
        //val txt = String.format(config.minecraftFormat, msg.from?.rawUserMention())
        val msgs = txt.split("%2\$s")
        val text = Text.empty()
        repeat(msgs.size - 1) { i ->
            text.append(msgs[i])
            text.append(msg.toText())
        }
        text.append(msgs.last())

        Bridge.sendMessage(text)

    }



    suspend fun sendMessageToTelegram(text: String, username: String? = null) {
        val formatted = username?.let {
            String.format(config.telegramFormat, username, text)
        } ?: text
        api.sendMessage(config.chatId, formatted)
    }

    suspend fun sendMessageWithoutParse(text: String, username: String? = null) {
        val formatted = username?.let {
            String.format(config.telegramFormat, username, text)
        } ?: text
        api.sendMessageWithoutParse(config.chatId, formatted)
    }
}