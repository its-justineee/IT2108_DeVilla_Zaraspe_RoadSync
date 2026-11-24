
package com.example.roadsync

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val UserDarkBg = Color(0xFF070B16)
private val UserHeaderBg = Color(0xFF0D1428)
private val UserCardBg = Color(0xFF10182C)
private val UserCardBorder = Color(0xFF1E2B4A)
private val UserPurple = Color(0xFF8B5CF6)
private val UserPurpleSoft = Color(0xFFA78BFA)
private val UserPurpleCardTop = Color(0xFF6D28D9)
private val UserPurpleCardBottom = Color(0xFF4C1D95)
private val UserText = Color(0xFFF8FAFC)
private val UserMuted = Color(0xFF94A3B8)

@Composable
fun UserDashboardScreen(
    userEmail: String,
    reports: List<IncidentReport>,
    onViewMap: () -> Unit,
    onLogout: () -> Unit,
    appLanguage: String = "English"
) {
    val isTagalog = appLanguage == "Tagalog"

    fun translateStatus(status: String): String {
        return when (status.lowercase()) {
            "reported" -> if (isTagalog) "Iniulat" else "Reported"
            "verified" -> if (isTagalog) "Beripikado" else "Verified"
            "resolved" -> if (isTagalog) "Nalutas" else "Resolved"
            else -> status
        }
    }

    val reportedCount = reports.count {
        it.status.equals("reported", ignoreCase = true)
    }

    val verifiedCount = reports.count {
        it.status.equals("verified", ignoreCase = true)
    }

    val resolvedCount = reports.count {
        it.status.equals("resolved", ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UserDarkBg),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {

        // HEADER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UserHeaderBg)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RoadSync",
                        color = UserText,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "PORTAL 02 • USER",
                        color = UserPurpleSoft,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = UserText
                    )
                ) {
                    Text(
                        text = if (isTagalog) "Mag-logout" else "Logout",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(color = UserCardBorder)
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
                    text = if (isTagalog) "Maligayang pagbabalik!"
                    else "Welcome back!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = UserText
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = if (isTagalog)
                        "Subaybayan ang iyong mga naisumiteng ulat tungkol sa insidente sa kalsada."
                    else
                        "Monitor your submitted road incident reports.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = UserMuted
                )
            }
        }

        // ACCOUNT CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = UserCardBg
                ),
                border = BorderStroke(1.dp, UserCardBorder),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = UserPurple.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, UserPurple.copy(alpha = 0.35f)),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            DashboardUserIcon(
                                tint = UserPurpleSoft,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isTagalog) "IYONG ACCOUNT"
                            else "YOUR ACCOUNT",
                            color = UserPurpleSoft,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = userEmail,
                            color = UserText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // REPORT STATISTICS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 22.dp)
            ) {
                Text(
                    text = if (isTagalog) "Pangkalahatang Ulat"
                    else "Report Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = UserText
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, UserPurpleSoft.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        UserPurpleCardTop,
                                        UserPurpleCardBottom
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Text(
                            text = if (isTagalog) "KABUUANG ULAT NG INSIDENTE"
                            else "TOTAL INCIDENT REPORTS",
                            color = Color.White.copy(alpha = 0.88f),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${reports.size}",
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = if (isTagalog)
                                "Mga ulat na ipinadala gamit ang iyong account"
                            else
                                "Reports submitted by your account",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ReportStatCard(
                        title = if (isTagalog) "Iniulat" else "Reported",
                        count = reportedCount,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )

                    ReportStatCard(
                        title = if (isTagalog) "Beripikado" else "Verified",
                        count = verifiedCount,
                        color = Color(0xFF60A5FA),
                        modifier = Modifier.weight(1f)
                    )

                    ReportStatCard(
                        title = if (isTagalog) "Nalutas" else "Resolved",
                        count = resolvedCount,
                        color = Color(0xFF34D399),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // INCIDENT REPORTS TITLE
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 26.dp, bottom = 12.dp)
            ) {
                Text(
                    text = if (isTagalog) "Aking mga Ulat ng Insidente"
                    else "My Incident Reports",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = UserText
                )

                Text(
                    text = if (isTagalog)
                        "Subaybayan ang kalagayan ng iyong mga naisumiteng ulat"
                    else
                        "Track the status of your submissions",
                    style = MaterialTheme.typography.bodySmall,
                    color = UserMuted
                )
            }
        }

        // EMPTY STATE
        if (reports.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = UserCardBg
                    ),
                    border = BorderStroke(1.dp, UserCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DashboardEmptyListIcon(
                            tint = UserMuted,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isTagalog) "Wala Pang mga Ulat"
                            else "No Reports Yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = UserText
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = if (isTagalog)
                                "Wala ka pang naipapasa na ulat tungkol sa insidente sa kalsada."
                            else
                                "You haven't submitted any road incident reports yet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = UserMuted
                        )
                    }
                }
            }
        } else {

            // REPORT LIST
            items(reports) { report ->

                val statusColor = when (report.status.lowercase()) {
                    "reported" -> Color(0xFFF59E0B)
                    "verified" -> Color(0xFF60A5FA)
                    "resolved" -> Color(0xFF34D399)
                    else -> UserMuted
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = UserCardBg
                    ),
                    border = BorderStroke(1.dp, UserCardBorder),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = report.type,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = UserPurpleSoft
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                color = statusColor.copy(alpha = 0.16f),
                                border = BorderStroke(
                                    1.dp,
                                    statusColor.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = translateStatus(report.status),
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

                        Text(
                            text = report.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = UserText
                        )

                        HorizontalDivider(color = UserCardBorder)

                        Text(
                            text = "Latitude: %.5f".format(report.latitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = UserMuted
                        )

                        Text(
                            text = "Longitude: %.5f".format(report.longitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = UserMuted
                        )
                    }
                }
            }
        }

        // ACTION BUTTONS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onViewMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UserPurple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isTagalog) "Tingnan ang Mapa ng Kalsada"
                        else "View Road Map",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, UserCardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = UserCardBg,
                        contentColor = UserText
                    )
                ) {
                    Text(
                        text = if (isTagalog) "Mag-logout" else "Logout",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportStatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = UserCardBg
        ),
        border = BorderStroke(1.dp, UserCardBorder),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                color = color,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                color = UserMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DashboardUserIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(
            width = w * 0.09f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        drawCircle(
            color = tint,
            radius = w * 0.17f,
            center = Offset(w * 0.5f, h * 0.32f),
            style = stroke
        )

        val shoulders = Path().apply {
            moveTo(w * 0.20f, h * 0.84f)
            cubicTo(
                w * 0.22f, h * 0.60f,
                w * 0.78f, h * 0.60f,
                w * 0.80f, h * 0.84f
            )
        }

        drawPath(
            path = shoulders,
            color = tint,
            style = stroke
        )
    }
}

@Composable
private fun DashboardEmptyListIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val stroke = Stroke(
            width = w * 0.08f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        val board = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = w * 0.18f,
                    top = h * 0.16f,
                    right = w * 0.82f,
                    bottom = h * 0.90f,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        w * 0.10f,
                        w * 0.10f
                    )
                )
            )
        }

        drawPath(
            path = board,
            color = tint,
            style = stroke
        )

        drawLine(
            color = tint,
            start = Offset(w * 0.34f, h * 0.42f),
            end = Offset(w * 0.66f, h * 0.42f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = tint,
            start = Offset(w * 0.34f, h * 0.60f),
            end = Offset(w * 0.66f, h * 0.60f),
            strokeWidth = w * 0.08f,
            cap = StrokeCap.Round
        )
    }
}
