
package com.example.roadsync

data class EmergencyHotlineItem(
    val id: String,
    val name: String,
    val phone: String
)

data class AdminSettings(
    val enableCommunityAlerts: Boolean = true,
    val alertPriority: String = "Standard",
    val pushNotification: Boolean = true,
    val inAppBanner: Boolean = true,
    val smsAlert: Boolean = false,
    val maxAlertRadiusKm: Float = 25f,

    val hotlines: List<EmergencyHotlineItem> = listOf(
        EmergencyHotlineItem("1", "MDRRMO Nasugbu", "+63 43 416 xxxx"),
        EmergencyHotlineItem("2", "Nasugbu Police", "+63 43 416 xxxx"),
        EmergencyHotlineItem("3", "BFP Nasugbu", "+63 43 416 xxxx"),
        EmergencyHotlineItem("4", "Apacible Memorial Hospital", "+63 43 416 xxxx")
    ),

    val autoVerifyReports: Boolean = false,
    val autoVerifyThreshold: Int = 3,
    val retentionPolicy: String = "1 Year"
)
