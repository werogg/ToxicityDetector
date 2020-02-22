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
    private val pApi = PerspectiveAPI.create(apikey)

    private var configUtil = ConfigUtil(configHandler)

    /**
     * Get toxicity of [message]
     * @return message's toxicity.
     */
    fun getToxicityMap(message: String, requestedAttributeTypes : ArrayList<AttributeType>) : Map<AttributeType, Double> {

        val toxicityMap = mutableMapOf<AttributeType, Double>()

        // Declare the PerspectiveAPI Request Builder
        val analyzeCommentRequestBuilder = AnalyzeCommentRequest.Builder()
                .comment(
                        Entry.Builder()
                                .type(ContentType.PLAIN_TEXT)
                                .text(message)
                                .build()
                )

        // Set languages (Multiple lang disabled for Google-PerspectiveAPI-Java-Client issue #3)
        if (!configUtil.getMainLanguage().equals("all"))
            analyzeCommentRequestBuilder.addLanguage(configUtil.getMainLanguage())

        for (attributeType in requestedAttributeTypes) {
            analyzeCommentRequestBuilder.addRequestedAttribute(attributeType, null)
        }

        // Build and analyze the request
        val result = pApi.analyze(analyzeCommentRequestBuilder.build())

        for (attributeType in requestedAttributeTypes) {
            toxicityMap[attributeType] = result.getAttributeScore(attributeType).summaryScore.value.toDouble()
        }

        return toxicityMap
    }

}