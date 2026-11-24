
package com.example.roadsync

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreAdminSettingsRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AdminSettingsRepository {

    companion object {
        private const val COLLECTION = "admin_settings"
    }

    override fun loadSettings(
        onSuccess: (AdminSettings) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("You must be logged in to load admin settings.")
            return
        }

        db.collection(COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    onSuccess(AdminSettings())
                    return@addOnSuccessListener
                }

                val hotlineData =
                    document.get("hotlines") as? List<*>

                val loadedHotlines = hotlineData
                    ?.mapNotNull { item ->
                        val data = item as? Map<*, *> ?: return@mapNotNull null

                        val id = data["id"] as? String ?: return@mapNotNull null
                        val name = data["name"] as? String ?: return@mapNotNull null
                        val phone = data["phone"] as? String ?: return@mapNotNull null

                        EmergencyHotlineItem(id, name, phone)
                    }
                    ?: AdminSettings().hotlines

                val settings = AdminSettings(
                    enableCommunityAlerts =
                        document.getBoolean("enableCommunityAlerts") ?: true,

                    alertPriority =
                        document.getString("alertPriority") ?: "Standard",

                    pushNotification =
                        document.getBoolean("pushNotification") ?: true,

                    inAppBanner =
                        document.getBoolean("inAppBanner") ?: true,

                    smsAlert =
                        document.getBoolean("smsAlert") ?: false,

                    maxAlertRadiusKm =
                        (document.get("maxAlertRadiusKm") as? Number)?.toFloat()
                            ?: 25f,

                    hotlines = loadedHotlines,

                    autoVerifyReports =
                        document.getBoolean("autoVerifyReports") ?: false,

                    autoVerifyThreshold =
                        (document.get("autoVerifyThreshold") as? Number)?.toInt()
                            ?: 3,

                    retentionPolicy =
                        document.getString("retentionPolicy") ?: "1 Year"
                )

                onSuccess(settings)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to load admin settings.")
            }
    }

    override fun saveSettings(
        settings: AdminSettings,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("You must be logged in to save admin settings.")
            return
        }

        if (settings.alertPriority !in listOf("Standard", "High", "Critical")) {
            onError("Invalid alert priority.")
            return
        }

        if (!settings.maxAlertRadiusKm.isFinite() ||
            settings.maxAlertRadiusKm !in 1f..50f
        ) {
            onError("Alert radius must be between 1 and 50 km.")
            return
        }

        if (settings.autoVerifyThreshold < 1) {
            onError("Auto-verify threshold must be at least 1.")
            return
        }

        if (settings.retentionPolicy !in listOf("30 Days", "90 Days", "1 Year")) {
            onError("Invalid data retention policy.")
            return
        }

        if (settings.hotlines.any {
                it.name.isBlank() || it.phone.isBlank()
            }
        ) {
            onError("Hotline names and phone numbers cannot be empty.")
            return
        }

        val data = hashMapOf(
            "userId" to userId,
            "enableCommunityAlerts" to settings.enableCommunityAlerts,
            "alertPriority" to settings.alertPriority,
            "pushNotification" to settings.pushNotification,
            "inAppBanner" to settings.inAppBanner,
            "smsAlert" to settings.smsAlert,
            "maxAlertRadiusKm" to settings.maxAlertRadiusKm,
            "hotlines" to settings.hotlines.map { hotline ->
                mapOf(
                    "id" to hotline.id,
                    "name" to hotline.name,
                    "phone" to hotline.phone
                )
            },
            "autoVerifyReports" to settings.autoVerifyReports,
            "autoVerifyThreshold" to settings.autoVerifyThreshold,
            "retentionPolicy" to settings.retentionPolicy
        )

        db.collection(COLLECTION)
            .document(userId)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to save admin settings.")
            }
    }
}
