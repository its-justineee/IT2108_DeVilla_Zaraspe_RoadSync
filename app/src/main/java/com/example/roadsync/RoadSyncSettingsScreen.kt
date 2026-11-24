
package com.example.roadsync

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.roundToInt
import androidx.compose.ui.res.stringResource

@Composable
fun RoadSyncSettingsScreen(
    userEmail: String = "justinedevilla01@gmail.com",
    sessionId: String? = null,
    appLanguage: String,
    onLanguageChange: (String) -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onDownloadOfflineMap: () -> Unit = {}
) {
    // DARK NAVY / SLATE PALETTE
    val screenBg = Color(0xFF070B16)
    val topBarBg = Color(0xFF0D1428)
    val cardBg = Color(0xFF10182C)
    val elevatedBg = Color(0xFF16213B)
    val borderColor = Color(0xFF1E2B4A)
    val purplePrimary = Color(0xFF8B5CF6)
    val purpleSoft = Color(0xFFA78BFA)
    val purpleSurfaceSoft = Color(0xFF1B1938)
    val primaryText = Color(0xFFF8FAFC)
    val secondaryText = Color(0xFF94A3B8)

    // ==========================================
    // ACCOUNT-SPECIFIC SETTINGS SCOPE
    // ==========================================
    val currentAuthUser = FirebaseAuth.getInstance().currentUser

    val settingsScope = remember(
        sessionId,
        currentAuthUser?.uid,
        currentAuthUser?.isAnonymous
    ) {
        when {
            currentAuthUser != null &&
                    !currentAuthUser.isAnonymous ->
                currentAuthUser.uid

            !sessionId.isNullOrBlank() &&
                    currentAuthUser?.isAnonymous != true ->
                sessionId

            else -> "guest"
        }
    }

    // ==========================================
    // SETTINGS REPOSITORY
    // ==========================================
    val context = LocalContext.current

    val settingsRepository: UserSettingsRepository = remember(context) {
        SharedPreferencesUserSettingsRepository(context)
    }

    val initialSettings = remember(settingsRepository, settingsScope) {
        settingsRepository.loadSettings(settingsScope)
    }

    // ==========================================
    // 1. ALERT & NOTIFICATION PREFERENCES
    // ==========================================
    var verifiedIncidentAlerts by remember(settingsScope) {
        mutableStateOf(initialSettings.verifiedIncidentAlerts)
    }

    var floodSevereAlerts by remember(settingsScope) {
        mutableStateOf(initialSettings.floodSevereAlerts)
    }

    var notificationRadiusKm by remember(settingsScope) {
        mutableFloatStateOf(initialSettings.notificationRadiusKm)
    }

    // ==========================================
    // 2. OFFLINE & DATA USAGE
    // ==========================================
    var offlineSmsFallback by remember(settingsScope) {
        mutableStateOf(initialSettings.offlineSmsFallback)
    }

    var autoCompressPhotos by remember(settingsScope) {
        mutableStateOf(initialSettings.autoCompressPhotos)
    }

    // ==========================================
    // 3. EMERGENCY & LOCATION
    // ==========================================
    var defaultAgency by remember(settingsScope) {
        mutableStateOf(initialSettings.defaultAgency)
    }

    var emergencyContactNumber by remember(settingsScope) {
        mutableStateOf(initialSettings.emergencyContactNumber)
    }

    var autoAttachGpsLandmark by remember(settingsScope) {
        mutableStateOf(initialSettings.autoAttachGpsLandmark)
    }

    var agencyExpanded by remember {
        mutableStateOf(false)
    }

    // ==========================================
    // 4. ACCOUNT & APP INFO
    // ==========================================
    var languageExpanded by remember {
        mutableStateOf(false)
    }

    // ==========================================
    // SAVE SETTINGS THROUGH REPOSITORY
    // ==========================================
    fun saveSettings(
        verifiedIncidentAlertsValue: Boolean = verifiedIncidentAlerts,
        floodSevereAlertsValue: Boolean = floodSevereAlerts,
        notificationRadiusKmValue: Float = notificationRadiusKm,
        offlineSmsFallbackValue: Boolean = offlineSmsFallback,
        autoCompressPhotosValue: Boolean = autoCompressPhotos,
        defaultAgencyValue: String = defaultAgency,
        emergencyContactNumberValue: String = emergencyContactNumber,
        autoAttachGpsLandmarkValue: Boolean = autoAttachGpsLandmark
    ) {
        settingsRepository.saveSettings(
            scope = settingsScope,
            settings = UserSettings(
                verifiedIncidentAlerts = verifiedIncidentAlertsValue,
                floodSevereAlerts = floodSevereAlertsValue,
                notificationRadiusKm = notificationRadiusKmValue,
                offlineSmsFallback = offlineSmsFallbackValue,
                autoCompressPhotos = autoCompressPhotosValue,
                defaultAgency = defaultAgencyValue,
                emergencyContactNumber = emergencyContactNumberValue,
                autoAttachGpsLandmark = autoAttachGpsLandmarkValue
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = screenBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ==========================================
            // TOP HEADER
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = topBarBg,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onBack) {
                            Text(
                                "←",
                                color = primaryText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Column {
                            Text(
                                stringResource(R.string.settings_title),
                                color = primaryText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                stringResource(R.string.settings_subtitle),
                                color = secondaryText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    TextButton(onClick = onLogout) {
                        Text(
                            stringResource(R.string.logout),
                            color = Color(0xFFF87171),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // ==========================================
            // SETTINGS CONTENT
            // ==========================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // ======================================
                // 1. ALERT & NOTIFICATION PREFERENCES
                // ======================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg
                        ),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingsSectionTitle(
                                title = stringResource(R.string.alert_notification_preferences),
                                purple = purplePrimary,
                                textColor = purpleSoft
                            )

                            // VERIFIED INCIDENT ALERTS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        stringResource(R.string.verified_incident_alerts),
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )

                                    Text(
                                        stringResource(R.string.verified_incident_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }

                                Switch(
                                    checked = verifiedIncidentAlerts,
                                    onCheckedChange = { enabled ->
                                        verifiedIncidentAlerts = enabled
                                        saveSettings(
                                            verifiedIncidentAlertsValue = enabled
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            // FLOOD & SEVERE ALERTS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        stringResource(R.string.flood_severe_alerts),
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )

                                    Text(
                                        stringResource(R.string.flood_severe_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }

                                Switch(
                                    checked = floodSevereAlerts,
                                    onCheckedChange = { enabled ->
                                        floodSevereAlerts = enabled
                                        saveSettings(
                                            floodSevereAlertsValue = enabled
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            // NOTIFICATION RADIUS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.notification_proximity_radius),
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText
                                )

                                Text(
                                    "${notificationRadiusKm.roundToInt()} km",
                                    fontWeight = FontWeight.Bold,
                                    color = purpleSoft
                                )
                            }

                            Slider(
                                value = notificationRadiusKm,
                                onValueChange = { newValue ->
                                    notificationRadiusKm = newValue
                                },
                                onValueChangeFinished = {
                                    saveSettings(
                                        notificationRadiusKmValue = notificationRadiusKm
                                    )
                                },
                                valueRange = 1f..30f,
                                colors = SliderDefaults.colors(
                                    thumbColor = purplePrimary,
                                    activeTrackColor = purplePrimary,
                                    inactiveTrackColor = elevatedBg
                                )
                            )
                        }
                    }
                }

                // ======================================
                // 2. OFFLINE & DATA USAGE
                // ======================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg
                        ),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingsSectionTitle(
                                title = stringResource(R.string.offline_data_usage),
                                purple = purplePrimary,
                                textColor = purpleSoft
                            )

                            // OFFLINE SMS FALLBACK
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        stringResource(R.string.offline_sms_fallback),
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )

                                    Text(
                                        stringResource(R.string.offline_sms_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }

                                Switch(
                                    checked = offlineSmsFallback,
                                    onCheckedChange = { enabled ->
                                        offlineSmsFallback = enabled
                                        saveSettings(
                                            offlineSmsFallbackValue = enabled
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            // AUTO COMPRESS PHOTOS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        stringResource(R.string.auto_compress_photos),
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )

                                    Text(
                                        stringResource(R.string.auto_compress_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }

                                Switch(
                                    checked = autoCompressPhotos,
                                    onCheckedChange = { enabled ->
                                        autoCompressPhotos = enabled
                                        saveSettings(
                                            autoCompressPhotosValue = enabled
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            // OFFLINE MAP DOWNLOAD
                            Surface(
                                onClick = onDownloadOfflineMap,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = purpleSurfaceSoft,
                                border = BorderStroke(1.dp, borderColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 14.dp,
                                            vertical = 12.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            stringResource(R.string.download_offline_map),
                                            fontWeight = FontWeight.Bold,
                                            color = primaryText
                                        )

                                        Text(
                                            stringResource(R.string.offline_map_description),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = secondaryText
                                        )
                                    }

                                    Text(
                                        "12 MB",
                                        fontWeight = FontWeight.Bold,
                                        color = purpleSoft
                                    )
                                }
                            }
                        }
                    }
                }

                // ======================================
                // 3. EMERGENCY & LOCATION
                // ======================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg
                        ),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingsSectionTitle(
                                title = stringResource(R.string.emergency_location),
                                purple = purplePrimary,
                                textColor = purpleSoft
                            )

                            // DEFAULT EMERGENCY AGENCY
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.default_emergency_agency),
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )

                                Box {
                                    OutlinedButton(
                                        onClick = {
                                            agencyExpanded = true
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = primaryText
                                        )
                                    ) {
                                        Text(
                                            "$defaultAgency ▾",
                                            color = primaryText,
                                            fontSize = 12.sp
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = agencyExpanded,
                                        onDismissRequest = {
                                            agencyExpanded = false
                                        }
                                    ) {
                                        listOf(
                                            "MDRRMO Nasugbu",
                                            "Nasugbu Police",
                                            "BFP Nasugbu",
                                            "Apacible Memorial Hospital"
                                        ).forEach { agency ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(agency)
                                                },
                                                onClick = {
                                                    defaultAgency = agency
                                                    saveSettings(
                                                        defaultAgencyValue = agency
                                                    )
                                                    agencyExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // EMERGENCY CONTACT NUMBER
                            OutlinedTextField(
                                value = emergencyContactNumber,
                                onValueChange = { number ->
                                    emergencyContactNumber = number
                                    saveSettings(
                                        emergencyContactNumberValue = number.trim()
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(
                                        stringResource(R.string.emergency_contact_number),
                                        color = secondaryText
                                    )
                                },
                                placeholder = {
                                    Text(
                                        stringResource(R.string.phone_number_hint),
                                        color = secondaryText.copy(alpha = 0.7f)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = elevatedBg,
                                    unfocusedContainerColor = elevatedBg,
                                    focusedBorderColor = purplePrimary,
                                    unfocusedBorderColor = borderColor,
                                    focusedTextColor = primaryText,
                                    unfocusedTextColor = primaryText,
                                    cursorColor = purpleSoft
                                )
                            )

                            Text(
                                stringResource(R.string.sms_fallback_description),
                                style = MaterialTheme.typography.bodySmall,
                                color = secondaryText
                            )

                            // AUTO ATTACH GPS & LANDMARK
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        stringResource(R.string.auto_attach_gps),
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )

                                    Text(
                                        stringResource(R.string.auto_attach_gps_description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }

                                Switch(
                                    checked = autoAttachGpsLandmark,
                                    onCheckedChange = { enabled ->
                                        autoAttachGpsLandmark = enabled
                                        saveSettings(
                                            autoAttachGpsLandmarkValue = enabled
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }
                        }
                    }
                }

                // ======================================
                // 4. ACCOUNT & APP INFO
                // ======================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg
                        ),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingsSectionTitle(
                                title = stringResource(R.string.account_app_info),
                                purple = purplePrimary,
                                textColor = purpleSoft
                            )

                            // PROFILE DETAILS
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    stringResource(R.string.profile_details),
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryText
                                )

                                Text(
                                    userEmail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = secondaryText
                                )
                            }

                            HorizontalDivider(
                                color = borderColor
                            )

                            // APP LANGUAGE
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.app_language),
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )

                                Box {
                                    OutlinedButton(
                                        onClick = {
                                            languageExpanded = true
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = primaryText
                                        )
                                    ) {
                                        Text(
                                            "$appLanguage ▾",
                                            color = primaryText,
                                            fontSize = 12.sp
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = languageExpanded,
                                        onDismissRequest = {
                                            languageExpanded = false
                                        }
                                    ) {
                                        listOf(
                                            "English / Tagalog",
                                            "English",
                                            "Tagalog"
                                        ).forEach { lang ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(lang)
                                                },
                                                onClick = {
                                                    onLanguageChange(lang)
                                                    languageExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// REUSABLE SETTINGS SECTION TITLE
// ==========================================
@Composable
private fun SettingsSectionTitle(
    title: String,
    purple: Color,
    textColor: Color = Color(0xFFF8FAFC)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    purple,
                    CircleShape
                )
        )

        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
