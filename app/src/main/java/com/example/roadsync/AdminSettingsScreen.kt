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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext


@Composable
fun AdminSettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val screenBg = Color(0xFF070B16)
    val headerBg = Color(0xFF0D1428)
    val cardBg = Color(0xFF10182C)
    val elevatedBg = Color(0xFF16213B)
    val borderColor = Color(0xFF1E2B4A)
    val purplePrimary = Color(0xFF8B5CF6)
    val purpleSoft = Color(0xFFA78BFA)
    val primaryText = Color(0xFFF8FAFC)
    val secondaryText = Color(0xFF94A3B8)

    // Broadcast Alert Controls state
    var enableCommunityAlerts by remember { mutableStateOf(true) }
    var alertPriority by remember { mutableStateOf("Standard") }
    var priorityExpanded by remember { mutableStateOf(false) }
    var pushNotification by remember { mutableStateOf(true) }
    var inAppBanner by remember { mutableStateOf(true) }
    var smsAlert by remember { mutableStateOf(false) }
    var maxAlertRadiusKm by remember { mutableFloatStateOf(25f) }

    // Emergency Hotlines state
    val hotlines = remember {
        mutableStateListOf(
            EmergencyHotlineItem("1", "MDRRMO Nasugbu", "+63 43 416 xxxx"),
            EmergencyHotlineItem("2", "Nasugbu Police", "+63 43 416 xxxx"),
            EmergencyHotlineItem("3", "BFP Nasugbu", "+63 43 416 xxxx"),
            EmergencyHotlineItem("4", "Apacible Memorial Hospital", "+63 43 416 xxxx")
        )
    }
    var editingHotlineIndex by remember { mutableStateOf<Int?>(null) }
    var showAddHotlineDialog by remember { mutableStateOf(false) }
    var hotlineNameInput by remember { mutableStateOf("") }
    var hotlinePhoneInput by remember { mutableStateOf("") }

    // System Configuration state
    var autoVerifyReports by remember { mutableStateOf(false) }
    var autoVerifyThreshold by remember { mutableIntStateOf(3) }
    var retentionPolicy by remember { mutableStateOf("1 Year") }
    var retentionExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val settingsRepository = remember {
        FirestoreAdminSettingsRepository()
    }

    var isSavingSettings by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        settingsRepository.loadSettings(
            onSuccess = { settings ->
                enableCommunityAlerts = settings.enableCommunityAlerts
                alertPriority = settings.alertPriority
                pushNotification = settings.pushNotification
                inAppBanner = settings.inAppBanner
                smsAlert = settings.smsAlert
                maxAlertRadiusKm = settings.maxAlertRadiusKm

                hotlines.clear()
                hotlines.addAll(settings.hotlines)

                autoVerifyReports = settings.autoVerifyReports
                autoVerifyThreshold = settings.autoVerifyThreshold
                retentionPolicy = settings.retentionPolicy
            },
            onError = { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }
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
            // TOP HEADER
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = headerBg,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = onBack) {
                            Text(
                                "←",
                                color = primaryText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Admin Settings",
                            color = primaryText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = onLogout) {
                        Text(
                            "Logout",
                            color = Color(0xFFF87171),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. BROADCAST ALERT CONTROLS
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(purplePrimary, CircleShape)
                                )
                                Text(
                                    "Broadcast Alert Controls",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Enable Community Alerts",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = enableCommunityAlerts,
                                    onCheckedChange = { enableCommunityAlerts = it },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Default Alert Priority",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )
                                Box {
                                    OutlinedButton(
                                        onClick = { priorityExpanded = true },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = primaryText
                                        )
                                    ) {
                                        Text("$alertPriority ▾", color = primaryText, fontSize = 12.sp)
                                    }
                                    DropdownMenu(
                                        expanded = priorityExpanded,
                                        onDismissRequest = { priorityExpanded = false },
                                        containerColor = cardBg,
                                        border = BorderStroke(1.dp, borderColor)
                                    ) {
                                        listOf("Standard", "High", "Critical").forEach { level ->
                                            DropdownMenuItem(
                                                text = { Text(level, color = primaryText) },
                                                onClick = {
                                                    alertPriority = level
                                                    priorityExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = borderColor)

                            Text(
                                "Alert Delivery Channels",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = secondaryText
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = pushNotification,
                                        onCheckedChange = { pushNotification = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = purplePrimary,
                                            uncheckedColor = secondaryText,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                    Text("Push", color = primaryText, fontSize = 12.sp, maxLines = 1)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = inAppBanner,
                                        onCheckedChange = { inAppBanner = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = purplePrimary,
                                            uncheckedColor = secondaryText,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                    Text("Banner", color = primaryText, fontSize = 12.sp, maxLines = 1)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = smsAlert,
                                        onCheckedChange = { smsAlert = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = purplePrimary,
                                            uncheckedColor = secondaryText,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                    Text("SMS", color = primaryText, fontSize = 12.sp, maxLines = 1)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Max Alert Radius",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText
                                )
                                Text(
                                    "${maxAlertRadiusKm.roundToInt()} km",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = purpleSoft
                                )
                            }

                            Slider(
                                value = maxAlertRadiusKm,
                                onValueChange = { maxAlertRadiusKm = it },
                                valueRange = 1f..50f,
                                colors = SliderDefaults.colors(
                                    thumbColor = purplePrimary,
                                    activeTrackColor = purplePrimary,
                                    inactiveTrackColor = elevatedBg
                                )
                            )
                        }
                    }
                }
                // 2. EMERGENCY HOTLINE MANAGEMENT
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(purplePrimary, CircleShape)
                                )
                                Text(
                                    "Emergency Hotline Management",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText
                                )
                            }

                            hotlines.forEachIndexed { index, item ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = elevatedBg,
                                    border = BorderStroke(1.dp, borderColor)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                item.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryText
                                            )
                                            Text(
                                                item.phone,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = secondaryText
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                editingHotlineIndex = index
                                                hotlineNameInput = item.name
                                                hotlinePhoneInput = item.phone
                                                showAddHotlineDialog = true
                                            },
                                            contentPadding = PaddingValues(horizontal = 8.dp)
                                        ) {
                                            Text(
                                                "Edit",
                                                color = purpleSoft,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    editingHotlineIndex = null
                                    hotlineNameInput = ""
                                    hotlinePhoneInput = ""
                                    showAddHotlineDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, borderColor),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = elevatedBg,
                                    contentColor = purpleSoft
                                )
                            ) {
                                Text(
                                    "+ Add New Agency",
                                    color = purpleSoft,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 3. SYSTEM CONFIGURATION
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(purplePrimary, CircleShape)
                                )
                                Text(
                                    "System Configuration",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Auto-Verify Incident Reports",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = autoVerifyReports,
                                    onCheckedChange = { autoVerifyReports = it },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = purplePrimary,
                                        checkedThumbColor = Color.White,
                                        uncheckedTrackColor = elevatedBg,
                                        uncheckedThumbColor = secondaryText,
                                        uncheckedBorderColor = borderColor
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Auto-Verify Threshold",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = primaryText
                                    )
                                    Text(
                                        "$autoVerifyThreshold reports from trusted users",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = secondaryText
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            if (autoVerifyThreshold > 1) autoVerifyThreshold--
                                        },
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = purpleSoft
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("−", fontWeight = FontWeight.Bold, color = purpleSoft)
                                    }
                                    Text(
                                        "$autoVerifyThreshold",
                                        fontWeight = FontWeight.Bold,
                                        color = primaryText
                                    )
                                    OutlinedButton(
                                        onClick = { autoVerifyThreshold++ },
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = purpleSoft
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("+", fontWeight = FontWeight.Bold, color = purpleSoft)
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Data Retention Policy",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.weight(1f)
                                )
                                Box {
                                    OutlinedButton(
                                        onClick = { retentionExpanded = true },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, borderColor),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = elevatedBg,
                                            contentColor = primaryText
                                        )
                                    ) {
                                        Text("$retentionPolicy ▾", color = primaryText, fontSize = 12.sp)
                                    }
                                    DropdownMenu(
                                        expanded = retentionExpanded,
                                        onDismissRequest = { retentionExpanded = false },
                                        containerColor = cardBg,
                                        border = BorderStroke(1.dp, borderColor)
                                    ) {
                                        listOf("30 Days", "90 Days", "1 Year").forEach { policy ->
                                            DropdownMenuItem(
                                                text = { Text(policy, color = primaryText) },
                                                onClick = {
                                                    retentionPolicy = policy
                                                    retentionExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. SAVE ADMIN SETTINGS
                item {
                    Button(
                        onClick = {
                            isSavingSettings = true

                            val settings = AdminSettings(
                                enableCommunityAlerts = enableCommunityAlerts,
                                alertPriority = alertPriority,
                                pushNotification = pushNotification,
                                inAppBanner = inAppBanner,
                                smsAlert = smsAlert,
                                maxAlertRadiusKm = maxAlertRadiusKm,
                                hotlines = hotlines.toList(),
                                autoVerifyReports = autoVerifyReports,
                                autoVerifyThreshold = autoVerifyThreshold,
                                retentionPolicy = retentionPolicy
                            )

                            Toast.makeText(
                                context,
                                "Saving settings...",
                                Toast.LENGTH_SHORT
                            ).show()

                            settingsRepository.saveSettings(
                                settings = settings,
                                onSuccess = {
                                    isSavingSettings = false

                                    Toast.makeText(
                                        context,
                                        "Admin settings saved successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                onError = { message ->
                                    isSavingSettings = false

                                    Toast.makeText(
                                        context,
                                        "Save failed: $message",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        },
                        enabled = !isSavingSettings,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purplePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        if (isSavingSettings) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving...")
                        } else {
                            Text(
                                "SAVE SETTINGS",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
        }
    }

    if (showAddHotlineDialog) {
        AlertDialog(
            onDismissRequest = { showAddHotlineDialog = false },
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText,
            title = {
                Text(
                    if (editingHotlineIndex != null) "Edit Emergency Hotline" else "Add New Agency",
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = hotlineNameInput,
                        onValueChange = { hotlineNameInput = it },
                        label = { Text("Agency Name", color = secondaryText) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
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
                    OutlinedTextField(
                        value = hotlinePhoneInput,
                        onValueChange = { hotlinePhoneInput = it },
                        label = { Text("Hotline Number", color = secondaryText) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
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
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cleanName = hotlineNameInput.trim()
                        val cleanPhone = hotlinePhoneInput.trim()
                        if (cleanName.isNotEmpty() && cleanPhone.isNotEmpty()) {
                            val currentIdx = editingHotlineIndex
                            if (currentIdx != null && currentIdx in hotlines.indices) {
                                hotlines[currentIdx] = hotlines[currentIdx].copy(
                                    name = cleanName,
                                    phone = cleanPhone
                                )
                            } else {
                                hotlines.add(
                                    EmergencyHotlineItem(
                                        id = System.currentTimeMillis().toString(),
                                        name = cleanName,
                                        phone = cleanPhone
                                    )
                                )
                            }
                            showAddHotlineDialog = false
                        }
                    }
                ) {
                    Text("Save", color = purpleSoft, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHotlineDialog = false }) {
                    Text("Cancel", color = secondaryText)
                }
            }
        )
    }
}