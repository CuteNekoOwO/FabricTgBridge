package cuteneko.bridge

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CommandMessage
import net.minecraft.network.message.MessageType
import net.minecraft.network.message.SignedMessage
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KtMod : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Hello Fabric world!")
        ServerMessageEvents.CHAT_MESSAGE.register(
            ServerMessageEvents.ChatMessage { message: SignedMessage?, sender: ServerPlayerEntity?, params: MessageType.Parameters? ->
                LOGGER.info(
                    "ChatTest: {} sent \"{}\"",
                    sender,
                    message
                )
            }
        )
        ServerMessageEvents.GAME_MESSAGE.register(
            ServerMessageEvents.GameMessage { server: MinecraftServer?, message: Text?, overlay: Boolean ->
                LOGGER.info(
                    "ChatTest: server sent \"{}\"",
                    message
                )
            }
        )
        ServerMessageEvents.COMMAND_MESSAGE.register(
            CommandMessage { message: SignedMessage?, source: ServerCommandSource?, params: MessageType.Parameters? ->
                LOGGER.info(
                    "ChatTest: command sent \"{}\"",
                    message
                )
            }
        )
    }

    companion object {
        // This logger is used to write text to the console and the log file.
        // It is considered best practice to use your mod id as the logger's name.
        // That way, it's clear which mod wrote info, warnings, and errors.
        val LOGGER: Logger = LoggerFactory.getLogger("bridge")
    }
}