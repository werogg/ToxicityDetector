package es.jotero.toxicitydetector.chat

import es.jotero.toxicitydetector.apis.PerspectiveHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChattingListener(var perspectiveHandler: PerspectiveHandler) : Listener {

    /**
     * Calculate message toxicity for every chat [event]
     */
    @EventHandler
    fun onPlayerChats(event : AsyncPlayerChatEvent) {
        var player = event.player
        var message = event.message

        player.sendMessage(perspectiveHandler.getToxicity(message).toString())
    }

}