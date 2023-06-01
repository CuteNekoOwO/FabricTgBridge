@file:OptIn(DelicateCoroutinesApi::class)

package cuteneko.tgbridge

import com.google.gson.GsonBuilder
import cuteneko.tgbridge.tgbot.I18n
import kotlinx.coroutines.DelicateCoroutinesApi
import net.fabricmc.loader.api.FabricLoader
import java.io.InputStreamReader
import kotlin.io.path.*


object ConfigLoader {

    private val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    fun load(): Config {
        val dir = FabricLoader.getInstance().configDir.resolve(Bridge.MOD_ID)
        if(!dir.exists()) dir.createDirectory()
        val path = dir.resolve("config.json")
        if(!path.exists()) return Config()
        return gson.fromJson(path.reader(), Config::class.java)
    }

    fun save(config: Config) {
        val dir = FabricLoader.getInstance().configDir.resolve(Bridge.MOD_ID)
        if(!dir.exists()) dir.createDirectory()
        val path = dir.resolve("config.json")
        val json = gson.toJson(config)
        path.writeText(json, Charsets.UTF_8)
    }

    fun getLang(): Map<String, String> {
        val path = FabricLoader.getInstance().configDir.resolve(Bridge.MOD_ID).resolve("lang.json")
        if(!path.exists()) {
            val stream = javaClass.classLoader.getResourceAsStream("assets/lang.json")
            val reader = InputStreamReader(stream!!)
            path.writeText(reader.readText())
        }
        return gson.fromJson<Map<String, String>>(path.reader(), Map::class.java)
    }

    fun getI18n(): I18n {
        val path = FabricLoader.getInstance().configDir.resolve(Bridge.MOD_ID).resolve("i18n.json")
        if(!path.exists()) {
            path.writeText(gson.toJson(I18n()))
        }
        return gson.fromJson(path.reader(), I18n::class.java)
    }
}