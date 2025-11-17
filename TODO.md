# Performance Considerations

- Move the SharedPreferences reads in `ProviderSettingsRepository` and `AliasRepository` onto `Dispatchers.IO` and emit their values to `MutableStateFlow` afterward. This keeps cold launches from blocking the UI thread while flows initialize.
- Consider caching the initial state for those repositories via `DataStore` or a precomputed snapshot so the Compose tree can render immediately while background work catches up.
- Evaluate whether the assistant-role detection needs to run on every `onResume`. If not, debounce it or gate on actual user triggers to avoid redundant work.
