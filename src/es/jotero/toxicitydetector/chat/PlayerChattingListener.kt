package es.jotero.toxicitydetector.chat

import es.jotero.toxicitydetector.apis.PerspectiveHandler
import es.jotero.toxicitydetector.config.ConfigHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChattingListener(var perspectiveHandler: PerspectiveHandler, var configHandler: ConfigHandler) : Listener {

    var playerDataConfig = configHandler.getPlayerDataConfig()

    /**
     * Calculate message toxicity for every chat [event]
     */
    @EventHandler
    fun onPlayerChats(event : AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message
        val toxicity = perspectiveHandler.getToxicity(message)
        player.sendMessage("Message Toxicity: $toxicity")
        val playerUUID = player.uniqueId
        val old_toxicity = playerDataConfig.getDouble("$playerUUID.toxicity")
        val new_toxicity = old_toxicity * 0.8 + toxicity * 0.2
        player.sendMessage("New toxicity level: $new_toxicity")

        playerDataConfig.set("$playerUUID.toxicity", new_toxicity)
        configHandler.savePlayerDataConfig()
    }

}