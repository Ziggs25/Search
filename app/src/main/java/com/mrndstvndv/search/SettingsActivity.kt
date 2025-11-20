package com.mrndstvndv.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mrndstvndv.search.alias.AliasRepository
import com.mrndstvndv.search.provider.files.FileSearchRepository
import com.mrndstvndv.search.provider.ProviderRankingRepository
import com.mrndstvndv.search.provider.settings.ProviderSettingsRepository
import com.mrndstvndv.search.settings.AssistantRoleManager
import com.mrndstvndv.search.ui.settings.GeneralSettingsScreen
import com.mrndstvndv.search.ui.settings.WebSearchSettingsScreen
import com.mrndstvndv.search.ui.theme.SearchTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : ComponentActivity() {
    private val assistantRoleManager by lazy { AssistantRoleManager(this) }
    private val defaultAssistantState = mutableStateOf(false)

    private enum class Screen { General, WebSearch }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val aliasRepository = remember { AliasRepository(this@SettingsActivity) }
            val settingsRepository = remember { ProviderSettingsRepository(this@SettingsActivity) }
            val fileSearchRepository = remember { FileSearchRepository.getInstance(this@SettingsActivity) }
            val rankingRepository = remember { ProviderRankingRepository.getInstance(this@SettingsActivity) }
            val isDefaultAssistant by defaultAssistantState
            val motionPreferences by settingsRepository.motionPreferences.collectAsState()
            val webSearchSettings by settingsRepository.webSearchSettings.collectAsState()
            var currentScreen by remember { mutableStateOf(Screen.General) }
            val appName = getString(R.string.app_name)
            SearchTheme(motionPreferences = motionPreferences) {
                LaunchedEffect(Unit) {
                    refreshDefaultAssistantState()
                }
                when (currentScreen) {
                    Screen.General -> {
                        GeneralSettingsScreen(
                            aliasRepository = aliasRepository,
                            settingsRepository = settingsRepository,
                            fileSearchRepository = fileSearchRepository,
                            rankingRepository = rankingRepository,
                            appName = appName,
                            isDefaultAssistant = isDefaultAssistant,
                            onRequestSetDefaultAssistant = { assistantRoleManager.launchDefaultAssistantSettings() },
                            onOpenWebSearchSettings = { currentScreen = Screen.WebSearch },
                            onClose = { finish() }
                        )
                    }
                    Screen.WebSearch -> {
                        BackHandler { currentScreen = Screen.General }
                        WebSearchSettingsScreen(
                            initialSettings = webSearchSettings,
                            onBack = { currentScreen = Screen.General },
                            onSave = { newSettings ->
                                settingsRepository.saveWebSearchSettings(newSettings)
                                currentScreen = Screen.General
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshDefaultAssistantState()
    }

    private fun refreshDefaultAssistantState() {
        lifecycleScope.launch(Dispatchers.Default) {
            val isDefault = assistantRoleManager.isDefaultAssistant()
            withContext(Dispatchers.Main) {
                defaultAssistantState.value = isDefault
            }
        }
    }
}
