package es.jotero.toxicitydetector.apis

import au.com.origma.perspectiveapi.v1alpha1.models.AttributeType
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

        val playerUUID = player.uniqueId

        when (identifier) {
            "player_toxicity" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.TOXICITY),
                    "0"
            ).toString()

            "player_severe_toxicity" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.SEVERE_TOXICITY),
                    "0"
            ).toString()

            "player_toxicity_optimized" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.TOXICITY_FAST),
                    "0"
            ).toString()

            "player_identity_attack" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.IDENTITY_ATTACK),
                    "0"
            ).toString()

            "player_insult" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.INSULT),
                    "0"
            ).toString()

            "player_profanity" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.PROFANITY),
                    "0"
            ).toString()

            "player_threat" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.THREAT),
                    "0"
            ).toString()

            "player_sexually_explicit" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.SEXUALLY_EXPLICIT),
                    "0"
            ).toString()

            "player_flirtation" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.FLIRTATION),
                    "0"
            ).toString()

            "player_spam" -> return configHandler.getPlayerDataConfig().get(
                    configHandler.getAttributeTypePath(playerUUID, AttributeType.SPAM),
                    "0"
            ).toString()

        }

        return "Error"

    }
}