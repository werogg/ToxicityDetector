package es.jotero.toxicitydetector.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

class ConfigHandler (private val plugin: Plugin) {

    // Config declaration
    private var config = plugin.config
    private lateinit var playerDataConfig : FileConfiguration
    private lateinit var playerData : File

    /**
     * Init the plugin's config
     */
    fun initConfig() {
        val configFile = File(plugin.dataFolder, "config.yml")
        playerData = File(plugin.dataFolder, "playerData.yml")

        if (!configFile.exists()) {
            plugin.saveDefaultConfig()
        }

        if (!playerData.exists()) {
            plugin.saveResource("playerData.yml", false)
        }

        playerDataConfig = YamlConfiguration()

        try {
            playerDataConfig.load(playerData)
        } catch (exception : IOException) {
            exception.printStackTrace()
        }
    }

    /**
     * Get string value in [config]'s [path]
     * @return String value in the config
     */
    fun getString(file : String, path : String) : String? {
        if (file.equals("playerData.yml"))
            return playerDataConfig.getString(path)
        return config.getString(path)
    }

    /**
     * Get boolean value in [config]'s [path]
     * @return Boolean value in the config
     */
    fun getBoolean(file : String, path : String) : Boolean {
        if (file.equals("playerData.yml"))
            return playerDataConfig.getBoolean(path)
        return config.getBoolean(path)
    }

    /**
     * Get string list values in [config]'s [path]
     * @return List of String values in the config
     */
    fun getStringList(file : String, path : String) : MutableList<String> {
        if (file.equals("playerData.yml"))
            return playerDataConfig.getStringList(path)
        return config.getStringList(path)
    }

    fun getPlayerDataConfig() : FileConfiguration {
        return playerDataConfig
    }

    fun savePlayerDataConfig() {
        playerDataConfig.save(playerData)
    }

}