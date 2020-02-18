package es.jotero.toxicitydetector.utils

import es.jotero.toxicitydetector.config.ConfigHandler

class ConfigUtil(var config : ConfigHandler) {

    fun isToxicityEnabled(): Boolean {
        return config.getBoolean("analyze.toxicity")
    }
    fun isSevereToxicityEnabled(): Boolean {
        return config.getBoolean("analyze.severe_toxicity")
    }
    fun isToxicityOptimizedEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.toxicity_optimized")
    }
    fun isIdentityAttackEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.identity_attack")
    }
    fun isInsultEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.insult")
    }
    fun isProfanityEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.profanity")
    }
    fun isThreatEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.threat")
    }
    fun isSexuallyExplicityEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.sexually_explicit")
    }
    fun isFlirtationEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.flirtation")
    }
    fun isSpamEnabled(): Boolean {
        return config.getBoolean("analyze.experimental.spam")
    }

    fun getMainLanguage() : String? {
        return config.getString("language")
    }
}