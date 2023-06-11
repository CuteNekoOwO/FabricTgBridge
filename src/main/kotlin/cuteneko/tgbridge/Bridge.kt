@file:OptIn(DelicateCoroutinesApi::class)

package cuteneko.tgbridge

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import cuteneko.tgbridge.tgbot.TgBot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.network.message.SignedMessage
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException


class Bridge : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Telegram bridge loaded!")
        INSTANCE = this
        CONFIG = ConfigLoader.load()
        ConfigLoader.save(CONFIG)

        if(CONFIG.botToken == "YOUR BOT TOKEN HERE") {
            LOGGER.info("Please edit config file!")
            return
        }

        try {
            LANG = ConfigLoader.getLang()
        } catch (e:FileNotFoundException) {
            LOGGER.error("lang.json not found! Read the document for more info")
            return
        }

        BOT = TgBot(LOGGER)
        GlobalScope.launch { BOT.startPolling() }

        CommandRegistrationCallback.EVENT.register{ dispatcher, _, _ ->
            dispatcher.register(LiteralArgumentBuilder.literal<ServerCommandSource?>("tgbridge_reload")
                .requires {
                    it.hasPermissionLevel(4)
                }
                .executes {
                    if(RELOADING) {
                        it.source.sendMessage(Text.literal("A reload is already in progress!").formatted(Formatting.RED))
                        return@executes 1
                    }
                    RELOADING = true
                    it.source.sendMessage(Text.literal("Reloading!"))
                    CONFIG = ConfigLoader.load()
                    GlobalScope.launch {
                        try {

                            BOT.stop()
                            BOT = TgBot(LOGGER)
                            BOT.startPolling()
                            it.source.sendMessage(Text.literal("Reloaded!"))
                        }
                        catch (e: Exception) {
                            it.source.sendMessage(Text.literal("Error occurred!").formatted(Formatting.RED))
                            it.source.sendMessage(Text.literal(e.message))
                        }
                        finally {
                            RELOADING = false
                        }
                    }
                    0
                })
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            SERVER = it
            if(CONFIG.sendServerStarted) GlobalScope.launch { BOT.sendMessageToTelegram(CONFIG.serverStartedMessage)}
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            GlobalScope.launch {
                BOT.stop()
                if(CONFIG.sendServerStopping)  BOT.sendMessageToTelegram(CONFIG.serverStoppingMessage)
            }

        }

        ServerMessageEvents.CHAT_MESSAGE.register {
                message: SignedMessage?, sender: ServerPlayerEntity?, _ ->
            if(!CONFIG.sendChatMessage) return@register
            val senderName = sender?.displayName.toPlainString()
            val msg = message?.content.toPlainString()
            GlobalScope.launch { BOT.sendMessageToTelegram(msg.escapeHTML(), senderName.escapeHTML()) }
        }

        ServerMessageEvents.GAME_MESSAGE.register {
                _, message: Text?, _ ->
            if(!CONFIG.sendGameMessage) return@register
            val msg = message.toPlainString()
            GlobalScope.launch { BOT.sendMessageToTelegram(msg) }
        }
    }

    companion object {
        // This logger is used to write text to the console and the log file.
        // It is considered best practice to use your mod id as the logger's name.
        // That way, it's clear which mod wrote info, warnings, and errors.
        const val MOD_ID = "tgbridge"
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

        lateinit var INSTANCE: Bridge
        lateinit var SERVER: MinecraftServer
        lateinit var CONFIG: Config
        lateinit var LANG: Map<String, String>
        lateinit var BOT: TgBot
        var RELOADING: Boolean = false
        fun sendMessage(text: Text?) {
            SERVER.playerManager.playerList.forEach{
                it.sendMessage(text)
            }
            SERVER.sendMessage(text)
        }
    }
}