package es.jotero.toxicitydetector.utils

import es.jotero.toxicitydetector.config.ConfigHandler

class ConfigUtil(var config : ConfigHandler) {

    /**
     * Check if toxicity is enabled
     * @return true if enabled
     */
    fun isToxicityEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.toxicity")
    }

    /**
     * Check if severe toxicity is enabled
     * @return true if enabled
     */
    fun isSevereToxicityEnabled(): Boolean {
        return config.getBoolean("config.yml", "analyze.severe_toxicity")
    }

    /**
     * Check if toxicity optimized is enabled
     * @return true if enabled
     */
    fun isToxicityOptimizedEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.toxicity_optimized")
    }

    /**
     * Check if identity attack is enabled
     * @return true if enabled
     */
    fun isIdentityAttackEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.identity_attack")
    }

    /**
     * Check if insult is enabled
     * @return true if enabled
     */
    fun isInsultEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.insult")
    }

    /**
     * Check if profanity is enabled
     * @return true if enabled
     */
    fun isProfanityEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.profanity")
    }

    /**
     * Check if threat is enabled
     * @return true if enabled
     */
    fun isThreatEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.threat")
    }

    /**
     * Check if sexually explicit is enabled
     * @return true if enabled
     */
    fun isSexuallyExplicityEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.sexually_explicit")
    }

    /**
     * Check if flirtation is enabled
     * @return true if enabled
     */
    fun isFlirtationEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.flirtation")
    }

    /**
     * Check if spam is enabled
     * @return true if enabled
     */
    fun isSpamEnabled(): Boolean {
        return config.getBoolean("config.yml","analyze.experimental.spam")
    }

    /**
     * Get the main language set on config
     * @return the main language if set
     */
    fun getMainLanguage() : String? {
        return config.getString("config.yml","language")
    }
}