package es.jotero.toxicitydetector

import es.jotero.toxicitydetector.apis.PerspectiveHandler
import es.jotero.toxicitydetector.apis.PlaceholderAPI
import es.jotero.toxicitydetector.chat.PlayerChattingListener
import es.jotero.toxicitydetector.config.ConfigHandler
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger


class ToxicityDetector() : JavaPlugin() {

    lateinit var configHandler : ConfigHandler
    lateinit var perspectiveHandler : PerspectiveHandler

    /**
     * Executes on plugin enable
     */
    override fun onEnable() {
        // If api-key is not set, perspective api won't work so we disable the plugin and return the execution of onEnable()
        if (!initPerspectiveHandler()) {
            logger.info("Disabling $name, Perspective API key is not set")
            pluginLoader.disablePlugin(this)
            return
        }


        // perspective handler referenced before init, should be fixed in future
        configHandler = ConfigHandler(this, perspectiveHandler)
        configHandler.initConfig()

        // If PlaceholderAPI is installed, we load Placeholder compatibility class
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            PlaceholderAPI(this, configHandler).register()

        registerEvents()

        logger.info("$name detected Perspective API key correctly, plugin loaded but may not work if the " +
                "api key is not correct")
    }

    /**
     * Init the Perspective API Handler
     */
    private fun initPerspectiveHandler() : Boolean {
        val apiKey = config.getString("config.yml", "api-key")

        if (apiKey.equals("")) return false

        perspectiveHandler = PerspectiveHandler(apiKey, configHandler)
        return true
    }

    /**
     * Register all spigot events
     */
    private fun registerEvents() {

        // Register player chat events
        server.pluginManager.registerEvents(PlayerChattingListener(configHandler), this)
    }

}