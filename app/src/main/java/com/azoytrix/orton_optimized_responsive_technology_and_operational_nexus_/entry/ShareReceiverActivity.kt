package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.entry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.theme.ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ShareReceiverActivity : ComponentActivity() {

    private var title by mutableStateOf("ORTON received:")
    private var body by mutableStateOf("(nothing shared)")

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mime = intent?.type.orEmpty()
        if (intent?.action == Intent.ACTION_SEND) {
            when {
                mime == "text/plain" -> {
                    title = "Shared text:"
                    body = intent.getStringExtra(Intent.EXTRA_TEXT) ?: "(empty)"
                }
                mime.startsWith("image/") -> {
                    val uri = IntentCompat.getParcelableExtra(
                        intent, Intent.EXTRA_STREAM, Uri::class.java
                    )
                    title = "Text found in image:"
                    body = "Reading image..."
                    if (uri != null) readImage(uri) else body = "(no image received)"
                }
            }
        }

        setContent {
            ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = title, style = MaterialTheme.typography.titleMedium)
                        Text(text = body, modifier = Modifier.padding(top = 12.dp))
                    }
                }
            }
        }
    }

    private fun readImage(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, uri)
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    body = result.text.ifBlank { "(no text found in image)" }
                }
                .addOnFailureListener { e ->
                    body = "OCR failed: ${e.message}"
                }
        } catch (e: Exception) {
            body = "Could not open image: ${e.message}"
        }
    }

    override fun onDestroy() {
        recognizer.close()
        super.onDestroy()
    }
}