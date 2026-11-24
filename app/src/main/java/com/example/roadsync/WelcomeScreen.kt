package com.example.roadsync

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onAdminClick: () -> Unit,
    onUserClick: () -> Unit,
    onGuestClick: () -> Unit = {},
    appLanguage: String = "English"
) {
    val isTagalog = appLanguage == "Tagalog"
    val screenBg = Color(0xFF070B16)
    val topGlow = Color(0xFF101935)
    val portalCardBg = Color(0xFF0E1528)
    val portalCardBorder = Color(0xFF1C2845)
    val adminBlue = Color(0xFF3B82F6)
    val adminBlueSoft = Color(0xFF60A5FA)
    val userPurple = Color(0xFF8B5CF6)
    val userPurpleSoft = Color(0xFFA78BFA)
    val lightText = Color(0xFFF8FAFC)
    val mutedText = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(topGlow, screenBg, screenBg)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // TOP BRAND EMBLEM
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color(0xFF171E3A),
                border = BorderStroke(1.dp, Color(0xFF28355C))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    WelcomeShieldEmblem(
                        tint = Color(0xFF93C5FD),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "RoadSync",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = lightText
            )

            Text(
                text = "NASUGBU",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.6.sp,
                color = mutedText
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = if (isTagalog) {
                    "Bawat kalsada.\nBawat insidente.\nIsang nagkakaisang komunidad."
                } else {
                    "Every road.\nEvery incident.\nOne connected community."
                },
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = lightText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isTagalog) {
                    "Subaybayan ang kondisyon ng mga kalsada, mag-ulat ng mga insidente, at tumulong na gawing mas ligtas ang mga kalsada sa Nasugbu."
                } else {
                    "Monitor road conditions, report incidents, and help make Nasugbu roads safer."
                },
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = mutedText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // PORTAL 01: ADMIN PORTAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = portalCardBg),
                border = BorderStroke(1.dp, portalCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF15223C),
                            border = BorderStroke(1.dp, adminBlue.copy(alpha = 0.25f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                WelcomeShieldEmblem(
                                    tint = adminBlueSoft,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "PORTAL 01",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = adminBlueSoft
                            )
                            Text(
                                text = if (isTagalog) "Portal ng Admin" else "Admin Portal",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText
                            )
                        }
                    }

                    Text(
                        text = if (isTagalog) {
                            "Pamahalaan ang mga ulat ng insidente, subaybayan ang mga kalsada, at pangalagaan ang kaligtasan ng komunidad."
                        } else {
                            "Manage incident reports, monitor roads, and oversee community safety."
                        },
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = mutedText
                    )

                    Button(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = adminBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isTagalog) {
                                "Buksan ang Admin Portal"
                            } else {
                                "Open Admin Portal"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PORTAL 02: USER PORTAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = portalCardBg),
                border = BorderStroke(1.dp, portalCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1A1736),
                            border = BorderStroke(1.dp, userPurple.copy(alpha = 0.25f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                WelcomeUserEmblem(
                                    tint = userPurpleSoft,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "PORTAL 02",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = userPurpleSoft
                            )
                            Text(
                                text = if (isTagalog) "Portal ng User" else "User Portal",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightText
                            )
                        }
                    }

                    Text(
                        text = if (isTagalog) {
                            "Mag-ulat ng mga insidente sa kalsada, tingnan ang mga update, at subaybayan ang iyong mga ulat."
                        } else {
                            "Report road incidents, view updates, and track your submissions."
                        },
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = mutedText
                    )

                    Button(
                        onClick = onUserClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = userPurple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isTagalog) {
                                "Mag-login / Magparehistro"
                            } else {
                                "Login / Register"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = onGuestClick) {
                Text(
                    text = if (isTagalog) {
                        "Magpatuloy bilang Bisita"
                    } else {
                        "Continue as Guest"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = mutedText
                )
            }
        }
    }
}

@Composable
private fun WelcomeShieldEmblem(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        val shield = Path().apply {
            moveTo(w * 0.5f, h * 0.08f)
            lineTo(w * 0.84f, h * 0.22f)
            lineTo(w * 0.80f, h * 0.54f)
            cubicTo(w * 0.78f, h * 0.75f, w * 0.64f, h * 0.87f, w * 0.5f, h * 0.93f)
            cubicTo(w * 0.36f, h * 0.87f, w * 0.22f, h * 0.75f, w * 0.20f, h * 0.54f)
            lineTo(w * 0.16f, h * 0.22f)
            close()
        }
        drawPath(path = shield, color = tint, style = stroke)
    }
}

@Composable
private fun WelcomeUserEmblem(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        drawCircle(
            color = tint,
            radius = w * 0.17f,
            center = Offset(w * 0.5f, h * 0.32f),
            style = stroke
        )
        val shoulders = Path().apply {
            moveTo(w * 0.20f, h * 0.84f)
            cubicTo(w * 0.22f, h * 0.60f, w * 0.78f, h * 0.60f, w * 0.80f, h * 0.84f)
        }
        drawPath(path = shoulders, color = tint, style = stroke)
    }
}