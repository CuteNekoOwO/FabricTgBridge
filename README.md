# FabricTgBridge
Sync message between telegram group and fabric minecraft server.

[中文文档](./README_ZH.md)

## Features

- Sync chat messages between minecraft server and telegram group.
- Good support for different telegram message type.
- Send server messages (_like players joining the server_) to telegram group.
- Query online player list in the group.
- Run server commands from the group (admins only).

## Installation

1. Download this mod from [Releases](./releases) and dependencies [Fabric API](https://modrinth.com/mod/fabric-api/), [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin/). Put them in your server `mods` directory.
2. Start the server to generate a default configuration file.
3. Create a telegram bot at [BotFather](https://t.me/BotFather) and obtain your bot token. Remember to disable `Privacy mode` if you want to send group message to minecraft.
4. Open your configuration file (at `config/tgbridge/config.json`) and write in your bot token.
5. Use command `/tgbridge_reload` on your server to reload the configuration.
6. Add your bot to your group, and run `/chatid` in the group to get group chat ID.
7. Write your group chat ID in your configuration file and reload like step 5.
8. Enjoy!

## Configuration

The configuration file is located at `config/tgbridge/config.json`. It's a json file ~~(obviously😋)~~.

A detailed explanation is here.

| Field | Type | Detail |
| ----- | ---- | ------ |
| botToken | String | Your bot token. |
| chatId | Int | Your group chat ID. |
| telegramAPI | String | The API url. Change this if you want to use a reversed proxy. |
| pollTimeout | Int | The bot keeps sending requests to telegram server to obtain new messages. If no new messages is aquired for `value` seconds, the bot will send a request again. **Smaller value is recommended if your network is not good.** |
| sendChatMessage | Boolean | Whether to send game chat messages to your group. |
| sendGameMessage | Boolean | Whether to send game event messages to your group. |
| sendTelegramMessage | Boolean | Whether to send telegram messages to Minecraft. |
| messageTrim | Int | Trim the message sent to Minecraft if it is longer than `value`. Use 0 to disable trimming. |
| sendServerStarted | Boolean | Whether to send server started message to your group. |
| sendServerStopping | Boolean | Whether to send server stopping message to your group. |
| minecraftFormat | String | How group chat messages look like in minecraft. `%1$s` for sender's name. `%2$s` for the message. |
| telegramFormat | String | How minecraft chat messages look like in telegram group. `%1$s` for sender's name. `%2$s` for the message. [Support some HTML style formatting](https://core.telegram.org/bots/api#html-style) |
| serverStartedMessage | String | What will be sent to group when the server has started. |
| serverStoppingMessage | String | What will be sent to group when the server is stopping. |
| admins | String[] | Admins who can run game commands in the group. Use telegram `username`. |
| noPermission | String | What will be replied when someone who is not admin uses `/cmd` in the group. |

## Localization

### Special telegram message in game

Edit `config/tgbridge/i18n.json`.

### In group server event message

On the PC you play Minecraft, open `minecraft/assets/indexes/{version}.json`. (change {version} to your server version). Search for `{your language code}.json` (such as `en_gb.json`) and remember the `hash` next to it. Now we will use `482e0dae05abfa35ab5cb076e41fda77b4fb9a08` as an example.
Go to `.minecraft/assets/objects/48/` (change `48` to the first to char of the hash), copy the file with name of that hash to somewhere else and rename it `lang.json`. Now you can replace `config/tgbridge/lang.json` with your new file.
