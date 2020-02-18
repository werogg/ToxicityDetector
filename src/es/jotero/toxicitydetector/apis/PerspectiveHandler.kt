package es.jotero.toxicitydetector.apis

import au.com.origma.perspectiveapi.v1alpha1.PerspectiveAPI
import au.com.origma.perspectiveapi.v1alpha1.models.AnalyzeCommentRequest
import au.com.origma.perspectiveapi.v1alpha1.models.AttributeType
import au.com.origma.perspectiveapi.v1alpha1.models.ContentType
import au.com.origma.perspectiveapi.v1alpha1.models.Entry
import es.jotero.toxicitydetector.config.ConfigHandler
import es.jotero.toxicitydetector.utils.ConfigUtil

class PerspectiveHandler(apikey: String?, configHandler: ConfigHandler) {

    // Prespective API instance init
    val pApi = PerspectiveAPI.create(apikey)
    // ConfigUtil declaration
    var configUtil = ConfigUtil(configHandler)

    /**
     * Get toxicity of [message]
     * @return message's toxicity.
     */
    fun getToxicity(message: String) : Float {

        // Declare the PerspectiveAPI Request Builder
        var analyzeCommentRequestBuilder = AnalyzeCommentRequest.Builder()
                .comment(
                        Entry.Builder()
                                .type(ContentType.PLAIN_TEXT)
                                .text(message)
                                .build()
                )

        // Set languages (Multiple lang disabled for Google-PerspectiveAPI-Java-Client issue #3)
        if (!configUtil.getMainLanguage().equals("all"))
            analyzeCommentRequestBuilder.addLanguage(configUtil.getMainLanguage())

        // Check enabled attributes for the request
        if (configUtil.isToxicityEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.TOXICITY, null)
        if (configUtil.isSevereToxicityEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.SEVERE_TOXICITY, null)
        if (configUtil.isToxicityOptimizedEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.TOXICITY_FAST, null)
        if (configUtil.isIdentityAttackEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.IDENTITY_ATTACK, null)
        if (configUtil.isInsultEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.INSULT, null)
        if (configUtil.isProfanityEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.PROFANITY, null)
        if (configUtil.isThreatEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.THREAT, null)
        if (configUtil.isSexuallyExplicityEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.SEXUALLY_EXPLICIT, null)
        if (configUtil.isFlirtationEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.FLIRTATION, null)
        if (configUtil.isSpamEnabled()) analyzeCommentRequestBuilder.addRequestedAttribute(AttributeType.SPAM, null)

        // Build and analyze the request
        var result = pApi.analyze(analyzeCommentRequestBuilder.build())

        // TODO diversify results depending on active attributes
        return result.getAttributeScore(AttributeType.TOXICITY).summaryScore.value
    }

}