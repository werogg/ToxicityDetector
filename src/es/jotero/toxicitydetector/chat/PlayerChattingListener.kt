package es.jotero.toxicitydetector.chat

import es.jotero.toxicitydetector.apis.PerspectiveHandler
import es.jotero.toxicitydetector.config.ConfigHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChattingListener(var configHandler: ConfigHandler) : Listener {

    var playerDataConfig = configHandler.getPlayerDataConfig()

    /**
     * Calculate message toxicity for every chat [event]
     */
    @EventHandler
    fun onPlayerChats(event : AsyncPlayerChatEvent) {
        val playerUUID = event.player.uniqueId
        val message = event.message
        configHandler.updatePlayerToxicity(playerUUID, message)
    }

}