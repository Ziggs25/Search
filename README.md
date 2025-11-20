# Search (WIP)

Search is a work-in-progress Android universal search surface built with Kotlin and Jetpack Compose. It provides a fast command palette-style overlay that fans out queries across multiple providers while staying customizable for power users.

## Features
- Multi-provider querying that covers installed apps, calculator expressions, text utilities, on-device files with thumbnails, and web search fallbacks.
- Alias system for defining quick keywords that launch apps or reroute queries to preferred search engines.
- Provider ranking repository that tracks usage frequency, lets you reorder sources, and boosts the results you act on most.
- Material 3 UI with translucent results, adjustable blur/opacity, and motion-aware animations that respect accessibility preferences.
- Settings surface for per-provider tuning (e.g., web defaults, background behavior, loading indicators).
