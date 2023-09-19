# 这个仓库的未来
这个Github仓库很可能被永久停用，因为主要开发者MeowBot233于2023年9月16日因为尤文肉瘤去世。
我们永远纪念她。

# FabricTgBridge

在Fabric Minecraft服务器和Telegram群组间同步消息。

[English](./README.md)

## 功能

- 在Minecraft服务器和Telegram群组间同步消息。
- 支持各种不同的消息类型。
- 转发服务器消息（比如玩家上线）到群组。
- 在群组内直接执行服务器指令（仅限管理）。

## 安装

1. 从 [Releases](https://github.com/CuteNekoOwO/FabricTgBridge/releases) 下载本mod，并下载依赖 [Fabric API](https://modrinth.com/mod/fabric-api/) 和 [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin/)。把他们丢进服务器的`mods`文件夹。
2. 启动服务器来生成默认的配置文件。
3. 用 [BotFather](https://t.me/BotFather) 创建一个 Telegram 机器人，并获取Token。要把群消息转发到Minecraft还需关闭 `Privacy mode` 。
4. 打开配置文件（在 `config/tgbridge/config.json`），把Token写进去。
5. 在服务器上使用指令`/tgbridge_reload` 来重新加载配置。
6. 把机器人添加到你的群，在群里使用 `/chatid` 命令来获取群组ID。
7. 把群组ID写到配置文件里，和第五步一样重新加载配置。
8. 大功告成！

## 配置

配置文件在 `config/tgbridge/config.json` 。这是个JSON文件 ~~（显而易见😋）~~。

具体说明如下。

| 名称                    | 类型       | 说明                                                                                                                 |
|-----------------------|----------|--------------------------------------------------------------------------------------------------------------------|
| botToken              | String   | 机器人的 Token。                                                                                                        |
| chatId                | Int      | 群聊ID。                                                                                                              |
| telegramAPI           | String   | API地址。使用反向代理时修改这里。                                                                                                 |
| pollTimeout           | Int      | **当你的网络连接不太好的时候调小一点**                                                                                              |
| useHtmlFormat         | Boolean  | 是否使用Telegram的HTML格式。                                                                                               |
| sendChatMessage       | Boolean  | 是否发送游戏聊天到群。                                                                                                        |
| sendGameMessage       | Boolean  | 是否发送游戏事件到群。                                                                                                        |
| sendTelegramMessage   | Boolean  | 是否发送群聊消息到游戏。                                                                                                       |
| messageTrim           | Int      | 如果消息比此数值长，把它切断。设为0禁用。                                                                                              |
| sendServerStarted     | Boolean  | 是否发送服务器启动消息。                                                                                                       |
| sendServerStopping    | Boolean  | 是否发送服务器关闭消息。                                                                                                       |
| minecraftFormat       | String   | Minecraft中看到的群消息样式。 `%1$s` 为发送者名字。 `%2$s` 为消息内容。                                                                   |
| telegramFormat        | String   | 群聊中看到的Minecraft消息样式 `%1$s` 为发送者名字。 `%2$s` 为消息内容。 [支持一些 HTML 风格的格式化](https://core.telegram.org/bots/api#html-style) |
| serverStartedMessage  | String   | 服务器启动时发送的消息内容。                                                                                                     |
| serverStoppingMessage | String   | 服务器关闭时发送的消息内容。                                                                                                     |
| admins                | String[] | 能够在群聊中执行服务器指令的管理员列表。使用Telegram的 `username` 。                                                                       |
| noPermission          | String   | 非管理在群聊使用 `/cmd` 命令时的提示。                                                                                            |
| proxyEnabled          | Boolean  | 是否开启http代理。                                                                                                        |
| proxyHost             | String   | http代理服务器地址。                                                                                                       |
| proxyPort             | Int      | http代理端口。                                                                                                          |

## 本地化

### 游戏中的特殊群聊消息

编辑 `config/tgbridge/i18n.json` 。
[如果你需要简体中文版，点这里](./localization_zh_cn/i18n.json)

### 群聊中的游戏事件

[如果你需要简体中文版，点这里](./localization_zh_cn/lang.json)
使用此文件替换 `config/tgbridge/lang.json`

在你玩Minecraft的电脑上，打开 `minecraft/assets/indexes/{version}.json` 其中 `{version}` 为你服务器对应的版本。 搜索 `{语言代码}.json` （例如 `zh_cn.json`），记住它后面相连的 `hash` 。 现在我们以 `a68eabebe9c731a51708e9208b436d6f7a38c934` 为例。 进入 `.minecraft/assets/objects/a6` （把 `a6` 换成前文 `hash` 的前两个字符），找到以前文 `hash` 命名的文件，复制并改名为 `lang.json` 。用这个文件替换服务器上 `config/tgbridge/lang.json`。
