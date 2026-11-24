
package com.example.roadsync

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val RoadNavy = Color(0xFF0D1428)
private val RoadBlue = Color(0xFF38BDF8)
private val RoadPurple = Color(0xFF8B5CF6)
private val RoadPurpleSoft = Color(0xFFA78BFA)
private val RoadBackground = Color(0xFF070B16)
private val RoadCardSurface = Color(0xFF10182C)
private val RoadElevatedSurface = Color(0xFF16213B)
private val RoadBorder = Color(0xFF1E2B4A)
private val RoadText = Color(0xFFF8FAFC)
private val RoadMuted = Color(0xFF94A3B8)

@Composable
fun AdminDashboardScreen(
    reports: List<IncidentReport>,
    onUpdateStatus: (IncidentReport, String) -> Unit,
    onLogout: () -> Unit,
    onViewMap: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    appLanguage: String = "English"
) {
    val isTagalog = appLanguage == "Tagalog"

    val reportedCount = reports.count {
        it.status.equals("Reported", ignoreCase = true)
    }

    val verifiedCount = reports.count {
        it.status.equals("Verified", ignoreCase = true)
    }

    val resolvedCount = reports.count {
        it.status.equals("Resolved", ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(RoadBackground)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        // HEADER
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = RoadNavy,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "RoadSync",
                            color = RoadText,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "ADMIN CONTROL CENTER",
                            color = RoadPurpleSoft,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TextButton(onClick = onOpenSettings) {
                            Text(
                                "Settings",
                                color = RoadPurpleSoft,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(onClick = onLogout) {
                            Text(
                                if (isTagalog) "Mag-logout" else "Logout",
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // WELCOME SECTION
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 18.dp)
            ) {
                Text(
                    if (isTagalog) "Pangkalahatang Dashboard"
                    else "Dashboard Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = RoadText
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    if (isTagalog)
                        "Subaybayan at pamahalaan ang mga ulat tungkol sa insidente sa kalsada."
                    else
                        "Monitor and manage road incident reports.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RoadMuted
                )
            }
        }

        // TOTAL REPORTS CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RoadPurple
                ),
                border = BorderStroke(
                    1.dp,
                    RoadPurpleSoft.copy(alpha = 0.45f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        if (isTagalog) "KABUUANG ULAT NG INSIDENTE"
                        else "TOTAL INCIDENT REPORTS",
                        color = Color.White.copy(alpha = 0.88f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "${reports.size}",
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        if (isTagalog)
                            "Lahat ng ulat sa kalsada na ipinadala sa RoadSync"
                        else
                            "All road incidents submitted to RoadSync",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // STATUS STATISTICS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 22.dp)
            ) {
                Text(
                    if (isTagalog) "Kalagayan ng mga Insidente"
                    else "Incident Status",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = RoadText
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatusSummaryCard(
                        title = if (isTagalog) "Iniulat" else "Reported",
                        count = reportedCount,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )

                    StatusSummaryCard(
                        title = if (isTagalog) "Beripikado" else "Verified",
                        count = verifiedCount,
                        color = RoadBlue,
                        modifier = Modifier.weight(1f)
                    )

                    StatusSummaryCard(
                        title = if (isTagalog) "Nalutas" else "Resolved",
                        count = resolvedCount,
                        color = Color(0xFF34D399),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // INCIDENT MANAGEMENT HEADER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 26.dp, bottom = 12.dp)
            ) {
                Text(
                    if (isTagalog) "Pamahalaan ang mga Ulat ng Insidente"
                    else "Manage Incident Reports",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = RoadText
                )

                Text(
                    if (isTagalog)
                        "Suriin ang mga ulat at i-update ang kanilang kalagayan"
                    else
                        "Review reports and update their status",
                    style = MaterialTheme.typography.bodySmall,
                    color = RoadMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onViewMap,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, RoadBorder),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoadElevatedSurface,
                            contentColor = RoadText
                        )
                    ) {
                        Text(
                            if (isTagalog) "Subaybayan ang Mapa"
                            else "Monitor Road Map",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoadPurple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            if (isTagalog) "Settings ng Admin"
                            else "Admin Settings",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // EMPTY STATE OR REPORT LIST
        if (reports.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RoadCardSurface
                    ),
                    border = BorderStroke(1.dp, RoadBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "📋",
                            style = MaterialTheme.typography.displaySmall
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            if (isTagalog) "Walang Ulat ng Insidente"
                            else "No Incident Reports",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RoadText
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            if (isTagalog)
                                "Wala pang mga ulat na kailangang pamahalaan."
                            else
                                "There are currently no reports to manage.",
                            style = MaterialTheme.typography.bodySmall,
                            color = RoadMuted
                        )
                    }
                }
            }
        } else {
            items(
                items = reports,
                key = { it.id }
            ) { report ->

                var expanded by remember(report.id) {
                    mutableStateOf(false)
                }

                val statusColor = when (report.status.lowercase()) {
                    "reported" -> Color(0xFFF59E0B)
                    "verified" -> RoadBlue
                    "resolved" -> Color(0xFF34D399)
                    else -> RoadMuted
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RoadCardSurface
                    ),
                    border = BorderStroke(1.dp, RoadBorder),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // REPORT TYPE AND STATUS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                report.type,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RoadPurpleSoft
                            )

                            Surface(
                                color = statusColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    AdminDashboardHelper.translateStatus(
                                        report.status,
                                        isTagalog
                                    ),
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    ),
                                    color = statusColor,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        // DESCRIPTION
                        Text(
                            report.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = RoadText
                        )

                        HorizontalDivider(color = RoadBorder)

                        // LOCATION
                        Text(
                            "Latitude: %.5f".format(report.latitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = RoadMuted
                        )

                        Text(
                            "Longitude: %.5f".format(report.longitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = RoadMuted
                        )

                        // UPDATE STATUS
                        Box {
                            OutlinedButton(
                                onClick = {
                                    expanded = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, RoadBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = RoadElevatedSurface,
                                    contentColor = RoadText
                                )
                            ) {
                                Text(
                                    if (isTagalog) {
                                        "I-update ang Kalagayan: ${
                                            AdminDashboardHelper.translateStatus(
                                                report.status,
                                                isTagalog
                                            )
                                        }"
                                    } else {
                                        "Update Status: ${report.status}"
                                    },
                                    fontWeight = FontWeight.SemiBold,
                                    color = RoadText
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                listOf(
                                    "Reported",
                                    "Verified",
                                    "Resolved"
                                ).forEach { newStatus ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                AdminDashboardHelper.translateStatus(
                                                    newStatus,
                                                    isTagalog
                                                )
                                            )
                                        },
                                        onClick = {
                                            expanded = false

                                            // Keep English values for Firebase.
                                            onUpdateStatus(
                                                report,
                                                newStatus
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // LOGOUT BUTTON
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, RoadBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = RoadCardSurface,
                    contentColor = Color(0xFFF87171)
                )
            ) {
                Text(
                    if (isTagalog) "Mag-logout" else "Logout",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF87171)
                )
            }
        }
    }
}

// STATUS SUMMARY CARD
@Composable
private fun StatusSummaryCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = RoadCardSurface
        ),
        border = BorderStroke(1.dp, RoadBorder),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$count",
                color = color,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                title,
                color = RoadMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
