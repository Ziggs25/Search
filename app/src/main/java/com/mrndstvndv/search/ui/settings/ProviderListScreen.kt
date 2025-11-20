package com.mrndstvndv.search.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mrndstvndv.search.provider.settings.ProviderSettingsRepository

@Composable
fun ProviderListScreen(
    settingsRepository: ProviderSettingsRepository,
    onBack: () -> Unit,
    onOpenWebSearchSettings: () -> Unit,
    onOpenFileSearchSettings: () -> Unit,
    onOpenTextUtilitiesSettings: () -> Unit
) {
    val enabledProviders by settingsRepository.enabledProviders.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                SettingsHeader(onBack = onBack)
            }

            item {
                SettingsCardGroup {
                    ProviderRow(
                        id = "app-list",
                        name = "Applications",
                        description = "Search installed apps",
                        enabled = enabledProviders["app-list"] ?: true,
                        onToggle = { settingsRepository.setProviderEnabled("app-list", it) }
                    )
                    SettingsDivider()
                    ProviderRow(
                        id = "web-search",
                        name = "Web Search",
                        description = "Search the web",
                        enabled = enabledProviders["web-search"] ?: true,
                        onToggle = { settingsRepository.setProviderEnabled("web-search", it) },
                        onClick = onOpenWebSearchSettings
                    )
                    SettingsDivider()
                    ProviderRow(
                        id = "file-search",
                        name = "Files & Folders",
                        description = "Search local files",
                        enabled = enabledProviders["file-search"] ?: true,
                        onToggle = { settingsRepository.setProviderEnabled("file-search", it) },
                        onClick = onOpenFileSearchSettings
                    )
                    SettingsDivider()
                    ProviderRow(
                        id = "calculator",
                        name = "Calculator",
                        description = "Solve math expressions",
                        enabled = enabledProviders["calculator"] ?: true,
                        onToggle = { settingsRepository.setProviderEnabled("calculator", it) }
                    )
                    SettingsDivider()
                    ProviderRow(
                        id = "text-utilities",
                        name = "Text Utilities",
                        description = "Base64 encoding/decoding",
                        enabled = enabledProviders["text-utilities"] ?: true,
                        onToggle = { settingsRepository.setProviderEnabled("text-utilities", it) },
                        onClick = onOpenTextUtilitiesSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Providers",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Manage search sources.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SettingsCardGroup(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(content = content)
    }
}

@Composable
private fun ProviderRow(
    id: String,
    name: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = enabled,
                onCheckedChange = onToggle
            )
            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Settings",
                    modifier = Modifier.padding(start = 12.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}
