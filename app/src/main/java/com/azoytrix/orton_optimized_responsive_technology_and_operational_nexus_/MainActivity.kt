package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.chat.ChatScreen
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.chat.ChatViewModel
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.ledger.ActionLogScreen
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.ledger.ActionLogViewModel
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.memory.MemoryVaultScreen
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.memory.MemoryVaultViewModel
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.settings.SettingsScreen
import com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.theme.ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme

enum class Screen(val title: String, val icon: ImageVector) {
    CHAT("Chat", Icons.Default.Send),
    MEMORY("Memory", Icons.Default.Star),
    LEDGER("Ledger", Icons.Default.List),
    SETTINGS("Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    private val chatViewModel: ChatViewModel by viewModels()
    private val memoryViewModel: MemoryVaultViewModel by viewModels()
    private val actionLogViewModel: ActionLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme {
                var currentScreen by remember { mutableStateOf(Screen.CHAT) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            Screen.entries.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentScreen == screen,
                                    onClick = { currentScreen = screen }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.CHAT -> ChatScreen(viewModel = chatViewModel)
                            Screen.MEMORY -> MemoryVaultScreen(viewModel = memoryViewModel)
                            Screen.LEDGER -> ActionLogScreen(viewModel = actionLogViewModel)
                            Screen.SETTINGS -> SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}
