package com.mrndstvndv.search.provider.web

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.activity.ComponentActivity
import com.mrndstvndv.search.provider.Provider
import com.mrndstvndv.search.provider.model.ProviderResult
import com.mrndstvndv.search.provider.model.Query
import com.mrndstvndv.search.provider.settings.ProviderSettingsRepository

class WebSearchProvider(
    private val activity: ComponentActivity,
    private val settingsRepository: ProviderSettingsRepository
) : Provider {

    override val id: String = "web-search"
    override val displayName: String = "Web Search"

    override fun canHandle(query: Query): Boolean {
        val cleaned = query.trimmedText
        if (cleaned.isBlank()) return false
        return !Patterns.WEB_URL.matcher(cleaned).matches()
    }

    override suspend fun query(query: Query): List<ProviderResult> {
        val cleaned = query.trimmedText
        if (cleaned.isBlank()) return emptyList()

        val settings = settingsRepository.webSearchSettings.value
        val site = settings.siteForId(settings.defaultSiteId) ?: settings.sites.first()
        val searchUrl = site.buildUrl(cleaned)

        val action = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
            activity.startActivity(intent)
            activity.finish()
        }

        return listOf(
            ProviderResult(
                id = "$id:${site.id}:${cleaned.hashCode()}",
                title = "Search \"$cleaned\"",
                subtitle = site.displayName,
                providerId = id,
                onSelect = action
            )
        )
    }
}
