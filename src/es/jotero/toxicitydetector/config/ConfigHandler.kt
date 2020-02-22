package es.jotero.toxicitydetector.config

import au.com.origma.perspectiveapi.v1alpha1.models.AttributeType
import es.jotero.toxicitydetector.apis.PerspectiveHandler
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ConfigHandler (private val plugin: Plugin) {

    // Config declaration
    private var config = plugin.config
    private lateinit var playerDataConfig : FileConfiguration
    private lateinit var playerData : File
    private var messageWeight : Double = config.getDouble("message-weight")

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

    /**
     * PlayerData's [FileConfiguration] getter
     * @return [FileConfiguration] object with PlayerData
     */
    fun getPlayerDataConfig() : FileConfiguration {
        return playerDataConfig
    }

    /**
     * Saves Player Data
     */
    private fun savePlayerDataConfig() {
        playerDataConfig.save(playerData)
    }

    /**
     * Analyze [message] an update the player's toxicity parameters with the results
     * @param playerUUID
     * @param message message to analyze
     */
    suspend fun updatePlayerToxicity(perspectiveHandler : PerspectiveHandler, playerUUID : UUID, message : String) {

        // TODO SOLVE ASYNC PROBLEM HERE
        val requestedAttributeTypes = getEnabledAttributeTypes()

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            kotlin.run {
                val messageToxicityMap = perspectiveHandler.getToxicityMap(message, requestedAttributeTypes)


                Bukkit.getScheduler().runTask(plugin, Runnable {
                    kotlin.run {
                        onQueryDone(messageToxicityMap, playerUUID)
                    }
                })
            }
        })



    }

    private fun onQueryDone(messageToxicityMap : Map<AttributeType, Double>, playerUUID: UUID) {
        for ((attributeType, toxicityValue) in messageToxicityMap) {
            val path = getAttributeTypePath(playerUUID, attributeType)

            if (path == "error") {
                plugin.logger.info("An error has occurred, message analysis skipped!")
                return
            }

            val playerToxicity = playerDataConfig.getDouble(path)
            val newPlayerToxicity = toxicityValue * messageWeight + playerToxicity * (1 - messageWeight)

            playerDataConfig.set(path, newPlayerToxicity)
        }

        savePlayerDataConfig()
    }

    /**
     * Get the config's [AttributeType] path
     * @param playerUUID
     * @param attributeType
     */
    fun getAttributeTypePath(playerUUID: UUID, attributeType: AttributeType) : String {
        val path = "$playerUUID."
        return when (attributeType) {
            AttributeType.TOXICITY -> path + "toxicity"
            AttributeType.SEVERE_TOXICITY -> path + "severe_toxicity"
            AttributeType.TOXICITY_FAST -> path + "toxicity_optimized"
            AttributeType.IDENTITY_ATTACK -> path + "identity_attack"
            AttributeType.INSULT -> path + "insult"
            AttributeType.PROFANITY -> path + "profanity"
            AttributeType.THREAT -> path + "threat"
            AttributeType.SEXUALLY_EXPLICIT -> path + "sexually_explicit"
            AttributeType.FLIRTATION -> path + "flirtation"
            AttributeType.SPAM -> path + "spam"
            else -> "error"
        }
    }

    /**
     * Get a list of the enabled AttributeTypes
     * @return A [AttributeType]'s [ArrayList]
     */
    private fun getEnabledAttributeTypes() : ArrayList<AttributeType> {
        val enabledAttributeTypes = mutableListOf<AttributeType>()

        if (isToxicityEnabled()) enabledAttributeTypes.add(AttributeType.TOXICITY)
        if (isSevereToxicityEnabled()) enabledAttributeTypes.add(AttributeType.SEVERE_TOXICITY)
        if (isToxicityOptimizedEnabled()) enabledAttributeTypes.add(AttributeType.TOXICITY_FAST)
        if (isIdentityAttackEnabled()) enabledAttributeTypes.add(AttributeType.IDENTITY_ATTACK)
        if (isInsultEnabled()) enabledAttributeTypes.add(AttributeType.INSULT)
        if (isProfanityEnabled()) enabledAttributeTypes.add(AttributeType.PROFANITY)
        if (isThreatEnabled()) enabledAttributeTypes.add(AttributeType.THREAT)
        if (isSexuallyExplicityEnabled()) enabledAttributeTypes.add(AttributeType.SEXUALLY_EXPLICIT)
        if (isFlirtationEnabled()) enabledAttributeTypes.add(AttributeType.FLIRTATION)
        if (isSpamEnabled()) enabledAttributeTypes.add(AttributeType.SPAM)

        return enabledAttributeTypes as ArrayList<AttributeType>
    }

    /**
     * Check if toxicity is enabled
     * @return true if enabled
     */
    fun isToxicityEnabled(): Boolean {
        return config.getBoolean("analyze.toxicity")
    }

    /**
     * Check if severe toxicity is enabled
     * @return true if enabled
     */
    fun isSevereToxicityEnabled(): Boolean {
        return config.getBoolean("analyze.severe_toxicity")
    }

    /**
     * Check if toxicity optimized is enabled
     * @return true if enabled
     */
    fun isToxicityOptimizedEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.toxicity_optimized")
    }

    /**
     * Check if identity attack is enabled
     * @return true if enabled
     */
    fun isIdentityAttackEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.identity_attack")
    }

    /**
     * Check if insult is enabled
     * @return true if enabled
     */
    fun isInsultEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.insult")
    }

    /**
     * Check if profanity is enabled
     * @return true if enabled
     */
    fun isProfanityEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.profanity")
    }

    /**
     * Check if threat is enabled
     * @return true if enabled
     */
    fun isThreatEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.threat")
    }

    /**
     * Check if sexually explicit is enabled
     * @return true if enabled
     */
    fun isSexuallyExplicityEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.sexually_explicit")
    }

    /**
     * Check if flirtation is enabled
     * @return true if enabled
     */
    fun isFlirtationEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.flirtation")
    }

    /**
     * Check if spam is enabled
     * @return true if enabled
     */
    fun isSpamEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.spam")
    }

    /**
     * Get the main language set on config
     * @return the main language if set
     */
    fun getMainLanguage() : String? {
        return config.getString("language")
    }

}