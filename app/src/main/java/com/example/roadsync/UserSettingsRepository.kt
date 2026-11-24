
package com.example.roadsync

import android.content.Context
import android.content.SharedPreferences

// SETTINGS DATA MODEL
data class UserSettings(
    val verifiedIncidentAlerts: Boolean = true,
    val floodSevereAlerts: Boolean = true,
    val notificationRadiusKm: Float = 5f,
    val offlineSmsFallback: Boolean = true,
    val autoCompressPhotos: Boolean = false,
    val defaultAgency: String = "MDRRMO Nasugbu",
    val emergencyContactNumber: String = "",
    val autoAttachGpsLandmark: Boolean = true
)

// REPOSITORY ABSTRACTION
interface UserSettingsRepository {
    fun loadSettings(scope: String): UserSettings
    fun saveSettings(scope: String, settings: UserSettings)
}

// SHARED PREFERENCES IMPLEMENTATION
class SharedPreferencesUserSettingsRepository(
    context: Context
) : UserSettingsRepository {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(
            "roadsync_data",
            Context.MODE_PRIVATE
        )

    override fun loadSettings(scope: String): UserSettings {
        return UserSettings(
            verifiedIncidentAlerts = prefs.getBoolean(
                "pref_verified_incident_alerts_$scope",
                true
            ),
            floodSevereAlerts = prefs.getBoolean(
                "pref_flood_severe_alerts_$scope",
                true
            ),
            notificationRadiusKm = prefs.getFloat(
                "pref_radius_km_$scope",
                prefs.getFloat("pref_radius_km", 5f)
            ),
            offlineSmsFallback = prefs.getBoolean(
                "pref_offline_sms_$scope",
                true
            ),
            autoCompressPhotos = prefs.getBoolean(
                "pref_auto_compress_$scope",
                false
            ),
            defaultAgency = prefs.getString(
                "pref_default_agency_$scope",
                "MDRRMO Nasugbu"
            ) ?: "MDRRMO Nasugbu",
            emergencyContactNumber = prefs.getString(
                "pref_emergency_contact_$scope",
                ""
            ) ?: "",
            autoAttachGpsLandmark = prefs.getBoolean(
                "pref_auto_attach_gps_$scope",
                true
            )
        )
    }

    override fun saveSettings(
        scope: String,
        settings: UserSettings
    ) {
        prefs.edit()
            .putBoolean(
                "pref_verified_incident_alerts_$scope",
                settings.verifiedIncidentAlerts
            )
            .putBoolean(
                "pref_flood_severe_alerts_$scope",
                settings.floodSevereAlerts
            )
            .putFloat(
                "pref_radius_km_$scope",
                settings.notificationRadiusKm
            )
            .putBoolean(
                "pref_offline_sms_$scope",
                settings.offlineSmsFallback
            )
            .putBoolean(
                "pref_auto_compress_$scope",
                settings.autoCompressPhotos
            )
            .putString(
                "pref_default_agency_$scope",
                settings.defaultAgency
            )
            .putString(
                "pref_emergency_contact_$scope",
                settings.emergencyContactNumber
            )
            .putBoolean(
                "pref_auto_attach_gps_$scope",
                settings.autoAttachGpsLandmark
            )
            .apply()
    }
}
