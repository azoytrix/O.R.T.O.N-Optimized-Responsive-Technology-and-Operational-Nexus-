package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.entry

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.theme.ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme

class ShareReceiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText: String? =
            if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                intent.getStringExtra(Intent.EXTRA_TEXT)
            } else null

        setContent {
            ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "ORTON received:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = sharedText ?: "(nothing shared)",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}