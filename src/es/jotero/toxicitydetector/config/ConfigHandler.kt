package es.jotero.toxicitydetector.config

import org.bukkit.plugin.Plugin
import java.io.File

class ConfigHandler (private val plugin: Plugin) {

    // Config declaration
    var config = plugin.config

    /**
     * Init the plugin's config
     */
    fun initConfig() {
        var configFile = File(plugin.dataFolder, "config.yml")

        if (!configFile.exists()) {
            plugin.saveDefaultConfig()
        }
    }

    /**
     * Get string value in [config]'s [path]
     * @return String value in the config
     */
    fun getString(path : String) : String? {
        return config.getString(path)
    }

    /**
     * Get boolean value in [config]'s [path]
     * @return Boolean value in the config
     */
    fun getBoolean(path : String) : Boolean {
        return config.getBoolean(path)
    }

    /**
     * Get string list values in [config]'s [path]
     * @return List of String values in the config
     */
    fun getStringList(path : String) : MutableList<String> {
        return config.getStringList(path)
    }

}