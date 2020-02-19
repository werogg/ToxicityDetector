package es.jotero.toxicitydetector.apis

import es.jotero.toxicitydetector.ToxicityDetector
import es.jotero.toxicitydetector.config.ConfigHandler
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderAPI(val toxicityDetector: ToxicityDetector, val configHandler: ConfigHandler) : PlaceholderExpansion() {


    override fun getVersion(): String {
        return toxicityDetector.description.version
    }

    override fun getAuthor(): String {
        return toxicityDetector.description.authors.toString()
    }

    override fun getIdentifier(): String {
        return toxicityDetector.description.name
    }

    override fun persist(): Boolean {
        return true
    }

    // TODO Diversify placeholders
    override fun onPlaceholderRequest(player: Player?, identifier: String?): String {

        if (player == null) {
            return ""
        }

        // TODO Top X toxicity players
        if (identifier.equals("player_toxicity")) {
            return configHandler.getPlayerDataConfig().get("${player.uniqueId.toString()}.toxicity", "0").toString()
        }

        return "Error"
    }

}