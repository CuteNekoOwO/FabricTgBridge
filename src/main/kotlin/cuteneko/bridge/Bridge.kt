package cuteneko.bridge

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CommandMessage
import net.minecraft.network.message.MessageType
import net.minecraft.network.message.SignedMessage
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Bridge : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Hello Fabric world!")
        Instance = this

        ServerLifecycleEvents.SERVER_STARTED.register {
            Server = it
        }

        ServerMessageEvents.CHAT_MESSAGE.register {
                message: SignedMessage?, sender: ServerPlayerEntity?, _ ->
            val senderName = sender?.displayName.getFormattedString()
            val msg = message?.content.getFormattedString()
            LOGGER.info(
                "ChatTest: {} sent \"{}\"",
                senderName,
                msg
            )
        }
        ServerMessageEvents.GAME_MESSAGE.register {
                _, message: Text?, _ ->
            val msg = message.getFormattedString()
            LOGGER.info(
                "ChatTest: server sent \"{}\"",
                msg
            )
        }
        ServerMessageEvents.COMMAND_MESSAGE.register(
            CommandMessage { message: SignedMessage?, source: ServerCommandSource?, params: MessageType.Parameters? ->
                val msg = message?.content.getFormattedString()
                LOGGER.info(
                    "ChatTest: command sent \"{}\"",
                    msg
                )
            }
        )
    }

    companion object {
        // This logger is used to write text to the console and the log file.
        // It is considered best practice to use your mod id as the logger's name.
        // That way, it's clear which mod wrote info, warnings, and errors.
        val LOGGER: Logger = LoggerFactory.getLogger("bridge")

        lateinit var Instance: Bridge
        lateinit var Server: MinecraftServer
        fun SendMessage(text: Text?) {
            Server.playerManager.playerList.forEach{
                it.sendMessage(text)
            }
        }
    }
}