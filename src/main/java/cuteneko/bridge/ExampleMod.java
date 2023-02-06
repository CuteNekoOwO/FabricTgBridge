package cuteneko.bridge;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("bridge");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ServerMessageEvents.CHAT_MESSAGE.register(
				(message, sender, params) -> LOGGER.info("ChatTest: {} sent \"{}\"", sender, message)
		);
		ServerMessageEvents.GAME_MESSAGE.register(
				(server, message, overlay) -> LOGGER.info("ChatTest: server sent \"{}\"", message)
		);
		ServerMessageEvents.COMMAND_MESSAGE.register(
				(message, source, params) -> LOGGER.info("ChatTest: command sent \"{}\"", message)
		);
	}
}
