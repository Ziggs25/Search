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
import com.mrndstvndv.search.ui.settings.FileSearchSettingsScreen
import com.mrndstvndv.search.ui.settings.TextUtilitiesSettingsScreen
import com.mrndstvndv.search.ui.settings.ProviderListScreen
import com.mrndstvndv.search.ui.theme.SearchTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : ComponentActivity() {
    private val assistantRoleManager by lazy { AssistantRoleManager(this) }
    private val defaultAssistantState = mutableStateOf(false)

    private enum class Screen { General, WebSearch, FileSearch, TextUtilities, ProviderList }

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
            
            val initialScreen = remember {
                when (intent.getStringExtra(EXTRA_SCREEN)) {
                    SCREEN_PROVIDERS -> Screen.ProviderList
                    else -> Screen.General
                }
            }
            var currentScreen by remember { mutableStateOf(initialScreen) }
            
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
                            rankingRepository = rankingRepository,
                            appName = appName,
                            isDefaultAssistant = isDefaultAssistant,
                            onRequestSetDefaultAssistant = { assistantRoleManager.launchDefaultAssistantSettings() },
                            onOpenWebSearchSettings = { currentScreen = Screen.WebSearch },
                            onOpenFileSearchSettings = { currentScreen = Screen.FileSearch },
                            onOpenTextUtilitiesSettings = { currentScreen = Screen.TextUtilities },
                            onClose = { finish() }
                        )
                    }
                    Screen.ProviderList -> {
                        // Legacy/Deep-link support if needed, or can be removed if unused
                        if (initialScreen != Screen.ProviderList) {
                            BackHandler { currentScreen = Screen.General }
                        }
                        ProviderListScreen(
                            settingsRepository = settingsRepository,
                            onBack = {
                                if (initialScreen == Screen.ProviderList) {
                                    finish()
                                } else {
                                    currentScreen = Screen.General
                                }
                            },
                            onOpenWebSearchSettings = { currentScreen = Screen.WebSearch },
                            onOpenFileSearchSettings = { currentScreen = Screen.FileSearch },
                            onOpenTextUtilitiesSettings = { currentScreen = Screen.TextUtilities }
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
                    Screen.FileSearch -> {
                        BackHandler { currentScreen = Screen.General }
                        FileSearchSettingsScreen(
                            settingsRepository = settingsRepository,
                            fileSearchRepository = fileSearchRepository,
                            onBack = { currentScreen = Screen.General }
                        )
                    }
                    Screen.TextUtilities -> {
                        BackHandler { currentScreen = Screen.General }
                        TextUtilitiesSettingsScreen(
                            settingsRepository = settingsRepository,
                            onBack = { currentScreen = Screen.General }
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_SCREEN = "screen"
        const val SCREEN_PROVIDERS = "providers"
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
