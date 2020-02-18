package es.jotero.toxicitydetector

import es.jotero.toxicitydetector.apis.PerspectiveHandler
import es.jotero.toxicitydetector.chat.PlayerChattingListener
import es.jotero.toxicitydetector.config.ConfigHandler
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger


public class ToxicityDetector() : JavaPlugin() {

    lateinit var configHandler : ConfigHandler
    lateinit var perspectiveHandler : PerspectiveHandler

    // Logger companion
    companion object {
        val logger : Logger = Bukkit.getLogger()
    }

    /**
     * Executes on plugin enable
     */
    override fun onEnable() {
        configHandler = ConfigHandler(this)
        configHandler.initConfig()

        if (!initPerspectiveHandler()) {
            pluginLoader.disablePlugin(this)
            return
        }

        registerEvents()

        logger.info("[DEBUG] Plugin loaded correctly!")

        for (language in configHandler.getStringList("languages")) {
            logger.info(language)
        }
    }

    /**
     * Init the Perspective API Handler
     */
    private fun initPerspectiveHandler() : Boolean {
        var apiKey = configHandler.getString("api-key")

        if (apiKey.equals("")) return false

        perspectiveHandler = PerspectiveHandler(apiKey, configHandler)
        return true
    }

    /**
     * Register all spigot events
     */
    private fun registerEvents() {

        // Register player chat events
        server.pluginManager.registerEvents(PlayerChattingListener(perspectiveHandler), this)
    }

}