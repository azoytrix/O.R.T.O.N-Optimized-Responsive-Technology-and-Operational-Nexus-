package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.entry

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
    private var hasContent by mutableStateOf(false)

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
                    val text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
                    body = text.ifBlank { "(empty)" }
                    hasContent = text.isNotBlank()
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

                        if (hasContent) {
                            PlanCard(
                                eventTitle = deriveEventTitle(body),
                                onApprove = { openCalendar(deriveEventTitle(body), body) },
                                onReject = { finish() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deriveEventTitle(text: String): String =
        text.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.isNotEmpty() }
            ?.take(60)
            ?: "New event"

    private fun openCalendar(eventTitle: String, details: String) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, eventTitle)
            putExtra(CalendarContract.Events.DESCRIPTION, details)
        }
        try {
            startActivity(calendarIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No calendar app found", Toast.LENGTH_LONG).show()
        }
    }

    private fun readImage(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, uri)
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    body = result.text.ifBlank { "(no text found in image)" }
                    hasContent = result.text.isNotBlank()
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

@androidx.compose.runtime.Composable
private fun PlanCard(
    eventTitle: String,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Proposed action", style = MaterialTheme.typography.titleMedium)
            Text("Add a calendar event", modifier = Modifier.padding(top = 8.dp))
            Text("Title: $eventTitle", modifier = Modifier.padding(top = 4.dp))
            Text(
                "Data used: the text above, processed on this device",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                "Risk: reversible. You confirm in Calendar before anything is saved.",
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onApprove) { Text("Approve") }
                OutlinedButton(onClick = onReject) { Text("Reject") }
            }
        }
    }
}