
package com.example.roadsync

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardScreen(
    reports: List<IncidentReport>,
    onUpdateStatus: (Long, String) -> Unit,
    onLogout: () -> Unit,
    onViewMap: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // DASHBOARD HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "RoadSync Admin",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Incident Management Dashboard",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextButton(onClick = onLogout) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TOTAL REPORTS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Total Incident Reports")

                Text(
                    "${reports.size}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Manage Incident Reports",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onViewMap,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Monitor Road Map")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(8.dp))

        if (reports.isEmpty()) {

            Text("No incident reports available.")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = reports,
                    key = { it.id }
                ) { report ->

                    var expanded by remember(report.id) {
                        mutableStateOf(false)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                report.type,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(report.description)

                            Text(
                                "Status: ${report.status}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Latitude: %.5f".format(report.latitude),
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                "Longitude: %.5f".format(report.longitude),
                                style = MaterialTheme.typography.bodySmall
                            )

                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Update Status: ${report.status}")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf(
                                    "Reported",
                                    "Verified",
                                    "Resolved"
                                ).forEach { newStatus ->

                                    DropdownMenuItem(
                                        text = { Text(newStatus) },
                                        onClick = {
                                            expanded = false
                                            onUpdateStatus(
                                                report.id,
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
    }
}
