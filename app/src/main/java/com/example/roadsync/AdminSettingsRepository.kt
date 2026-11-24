
package com.example.roadsync

interface AdminSettingsRepository {

    fun loadSettings(
        onSuccess: (AdminSettings) -> Unit,
        onError: (String) -> Unit
    )

    fun saveSettings(
        settings: AdminSettings,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}
