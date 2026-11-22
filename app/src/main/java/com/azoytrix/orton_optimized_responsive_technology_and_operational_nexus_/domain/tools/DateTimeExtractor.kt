package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.domain.tools

import com.google.mlkit.nl.entityextraction.DateTimeEntity
import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions

data class ExtractedDateTime(
    val startMillis: Long,
    val hasTime: Boolean
)

/** The rest of the app only knows this interface, so the engine can be swapped later. */
interface DateTimeExtractor {
    fun extract(text: String, onResult: (ExtractedDateTime?, String?) -> Unit)
    fun close()
}

class MlKitDateTimeExtractor : DateTimeExtractor {

    private val extractor = EntityExtraction.getClient(
        EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH).build()
    )

    override fun extract(text: String, onResult: (ExtractedDateTime?, String?) -> Unit) {
        extractor.downloadModelIfNeeded()
            .addOnSuccessListener {
                val params = EntityExtractionParams.Builder(text)
                    .setEntityTypesFilter(setOf(Entity.TYPE_DATE_TIME))
                    .build()
                extractor.annotate(params)
                    .addOnSuccessListener { annotations ->
                        val first = annotations
                            .flatMap { it.entities }
                            .filterIsInstance<DateTimeEntity>()
                            .firstOrNull()
                        val result = first?.let {
                            ExtractedDateTime(
                                startMillis = it.timestampMillis,
                                hasTime = it.dateTimeGranularity >= DateTimeEntity.GRANULARITY_HOUR
                            )
                        }
                        onResult(result, null)
                    }
                    .addOnFailureListener { e -> onResult(null, e.message ?: "analysis failed") }
            }
            .addOnFailureListener { e -> onResult(null, e.message ?: "model download failed") }
    }

    override fun close() = extractor.close()
}