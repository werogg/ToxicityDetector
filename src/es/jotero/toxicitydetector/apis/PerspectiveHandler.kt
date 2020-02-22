package es.jotero.toxicitydetector.apis

import au.com.origma.perspectiveapi.v1alpha1.PerspectiveAPI
import au.com.origma.perspectiveapi.v1alpha1.models.*
import es.jotero.toxicitydetector.config.ConfigHandler

class PerspectiveHandler(apikey: String?, private var configHandler: ConfigHandler) {

    // Prespective API instance init
    private val pApi = PerspectiveAPI.create(apikey)

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
        if (configHandler.getMainLanguage().equals("all"))
            analyzeCommentRequestBuilder.addLanguage(configHandler.getMainLanguage())

        for (attributeType in requestedAttributeTypes) {
            analyzeCommentRequestBuilder.addRequestedAttribute(attributeType, RequestedAttribute.Builder().build())
        }

        val debug1 = analyzeCommentRequestBuilder.build()

        // Build and analyze the request
        val result = pApi.analyze(debug1)

        for (attributeType in requestedAttributeTypes) {
            toxicityMap.put(attributeType, result.getAttributeScore(attributeType).summaryScore.value.toDouble())
        }

        return toxicityMap
    }

}