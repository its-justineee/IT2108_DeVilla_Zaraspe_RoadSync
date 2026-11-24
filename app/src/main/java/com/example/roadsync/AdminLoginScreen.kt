
package com.example.roadsync

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminLoginScreen(
onLoginSuccess: (String) -> Unit,
onBack: () -> Unit,
appLanguage: String = "English",
authService: UserAuthService? = null
) {
    val isTagalog = appLanguage == "Tagalog"

    val resolvedAuthService = remember(authService) {
        authService ?: FirebaseManager()
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Dark Navy + Admin Blue Palette
    val darkBg = Color(0xFF070B16)
    val bannerTop = Color(0xFF0E1730)
    val bannerBottom = Color(0xFF070B16)
    val cardBg = Color(0xFF10182C)
    val fieldBg = Color(0xFF0B1120)
    val cardBorder = Color(0xFF1E2B4A)
    val adminBlue = Color(0xFF3B82F6)
    val adminBlueDark = Color(0xFF2563EB)
    val lightText = Color(0xFFF8FAFC)
    val mutedText = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        // TOP DARK GRADIENT BANNER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 36.dp,
                        bottomEnd = 36.dp
                    )
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(bannerTop, bannerBottom)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // ADMIN SHIELD BADGE
            Surface(
                modifier = Modifier.size(68.dp),
                shape = RoundedCornerShape(20.dp),
                color = adminBlue.copy(alpha = 0.16f),
                border = BorderStroke(
                    1.dp,
                    adminBlue.copy(alpha = 0.45f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    WelcomeMatchedShieldGraphic(
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // PORTAL 01 PILL BADGE
            Surface(
                shape = CircleShape,
                color = adminBlue.copy(alpha = 0.16f),
                border = BorderStroke(
                    1.dp,
                    adminBlue.copy(alpha = 0.35f)
                )
            ) {
                Text(
                    text = "PORTAL 01 • ADMIN",
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 4.dp
                    ),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color(0xFF93C5FD)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "RoadSync",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = lightText
            )

            Text(
                text = "NASUGBU",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.4.sp,
                color = Color(0xFF60A5FA)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // DARK FORM CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                ),
                border = BorderStroke(1.dp, cardBorder),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // TITLE
                    Text(
                        text = if (isTagalog)
                            "Pag-login ng Administrator"
                        else
                            "Administrator Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = lightText
                    )

                    // DESCRIPTION
                    Text(
                        text = if (isTagalog)
                            "Mag-login upang pamahalaan ang mga ulat ng insidente, subaybayan ang mga kalsada, at pangalagaan ang kaligtasan ng komunidad."
                        else
                            "Sign in to manage incident reports, monitor roads, and oversee community safety.",
                        fontSize = 13.sp,
                        color = mutedText,
                        lineHeight = 18.sp
                    )

                    // INLINE ERROR ALERT
                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF3B1219),
                            border = BorderStroke(
                                1.dp,
                                Color(0xFFEF4444).copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = errorMessage,
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFFFCA5A5),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // ADMIN EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                if (isTagalog)
                                    "Email ng Admin"
                                else
                                    "Admin Email"
                            )
                        },
                        placeholder = {
                            Text("admin@roadsync.app")
                        },
                        leadingIcon = {
                            WelcomeMatchedMailGraphic(
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = adminBlue,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = Color(0xFF60A5FA),
                            unfocusedLabelColor = mutedText,
                            focusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            unfocusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            focusedContainerColor = fieldBg,
                            unfocusedContainerColor = fieldBg,
                            focusedTextColor = lightText,
                            unfocusedTextColor = lightText
                        )
                    )

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                if (isTagalog)
                                    "Password"
                                else
                                    "Password"
                            )
                        },
                        placeholder = {
                            Text(
                                if (isTagalog)
                                    "Ilagay ang password ng admin"
                                else
                                    "Enter admin password"
                            )
                        },
                        leadingIcon = {
                            WelcomeMatchedLockGraphic(
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                },
                                enabled = !isLoading
                            ) {
                                WelcomeMatchedEyeGraphic(
                                    isVisible = passwordVisible,
                                    tint = mutedText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = adminBlue,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = Color(0xFF60A5FA),
                            unfocusedLabelColor = mutedText,
                            focusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            unfocusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            focusedContainerColor = fieldBg,
                            unfocusedContainerColor = fieldBg,
                            focusedTextColor = lightText,
                            unfocusedTextColor = lightText
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // LOGIN AS ADMIN BUTTON
                    Button(
                        onClick = {
                            isLoading = true
                            errorMessage = ""

                            resolvedAuthService.signInAsAdmin(
                                email = email,
                                password = password,
                                onSuccess = { uid ->
                                    isLoading = false
                                    onLoginSuccess(uid)
                                },
                                onError = { error ->
                                    isLoading = false

                                    errorMessage = when {
                                        isTagalog && error.contains(
                                            "password",
                                            ignoreCase = true
                                        ) -> "Mali ang password ng administrator."

                                        isTagalog && error.contains(
                                            "email",
                                            ignoreCase = true
                                        ) -> "Hindi wasto ang email address ng administrator."

                                        isTagalog -> "Hindi matagumpay ang pag-login. Pakisuri ang iyong mga detalye."

                                        else -> error
                                    }
                                }
                            )
                        },
                        enabled = !isLoading &&
                                email.isNotBlank() &&
                                password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = adminBlue,
                            disabledContainerColor = adminBlueDark.copy(alpha = 0.35f),
                            contentColor = Color.White,
                            disabledContentColor = Color.White.copy(alpha = 0.5f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isTagalog)
                                    "Mag-login bilang Admin"
                                else
                                    "Login as Admin",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // BACK TO MAP BUTTON
            OutlinedButton(
                onClick = onBack,
                enabled = !isLoading,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, cardBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = cardBg,
                    contentColor = lightText
                ),
                modifier = Modifier.height(46.dp)
            ) {
                WelcomeMatchedBackArrowGraphic(
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (isTagalog)
                        "Bumalik sa Mapa"
                    else
                        "Back to Map",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isTagalog)
                    "Bawat kalsada. Bawat insidente. Isang nagkakaisang komunidad."
                else
                    "Every road. Every incident. One connected community.",
                fontSize = 11.sp,
                color = mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==========================================
// SELF-CONTAINED VECTOR GRAPHICS
// ==========================================

@Composable
private fun WelcomeMatchedShieldGraphic(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val stroke = Stroke(
            width = w * 0.085f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.08f)
            lineTo(w * 0.84f, h * 0.22f)
            lineTo(w * 0.80f, h * 0.54f)
            cubicTo(
                w * 0.78f, h * 0.75f,
                w * 0.64f, h * 0.87f,
                w * 0.5f, h * 0.93f
            )
            cubicTo(
                w * 0.36f, h * 0.87f,
                w * 0.22f, h * 0.75f,
                w * 0.20f, h * 0.54f
            )
            lineTo(w * 0.16f, h * 0.22f)
            close()
        }

        drawPath(
            path = shieldPath,
            color = tint,
            style = stroke
        )
    }
}

@Composable
private fun WelcomeMatchedMailGraphic(
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

        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.1f, h * 0.2f),
            size = Size(w * 0.8f, h * 0.6f),
            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
            style = stroke
        )

        val flap = Path().apply {
            moveTo(w * 0.14f, h * 0.26f)
            lineTo(w * 0.5f, h * 0.54f)
            lineTo(w * 0.86f, h * 0.26f)
        }

        drawPath(
            path = flap,
            color = tint,
            style = stroke
        )
    }
}

@Composable
private fun WelcomeMatchedLockGraphic(
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

        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.2f, h * 0.44f),
            size = Size(w * 0.6f, h * 0.46f),
            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
            style = stroke
        )

        val shackle = Path().apply {
            moveTo(w * 0.32f, h * 0.44f)
            lineTo(w * 0.32f, h * 0.30f)
            cubicTo(
                w * 0.32f, h * 0.16f,
                w * 0.68f, h * 0.16f,
                w * 0.68f, h * 0.30f
            )
            lineTo(w * 0.68f, h * 0.44f)
        }

        drawPath(
            path = shackle,
            color = tint,
            style = stroke
        )

        drawCircle(
            color = tint,
            radius = w * 0.055f,
            center = Offset(w * 0.5f, h * 0.66f)
        )
    }
}

@Composable
private fun WelcomeMatchedEyeGraphic(
    isVisible: Boolean,
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

        val eyeOutline = Path().apply {
            moveTo(w * 0.08f, h * 0.5f)
            cubicTo(
                w * 0.24f, h * 0.22f,
                w * 0.76f, h * 0.22f,
                w * 0.92f, h * 0.5f
            )
            cubicTo(
                w * 0.76f, h * 0.78f,
                w * 0.24f, h * 0.78f,
                w * 0.08f, h * 0.5f
            )
            close()
        }

        drawPath(
            path = eyeOutline,
            color = tint,
            style = stroke
        )

        drawCircle(
            color = tint,
            radius = w * 0.12f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = stroke
        )

        if (!isVisible) {
            drawLine(
                color = tint,
                start = Offset(w * 0.16f, h * 0.84f),
                end = Offset(w * 0.84f, h * 0.16f),
                strokeWidth = w * 0.09f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun WelcomeMatchedBackArrowGraphic(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val stroke = Stroke(
            width = w * 0.12f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        drawLine(
            color = tint,
            start = Offset(w * 0.82f, h * 0.5f),
            end = Offset(w * 0.18f, h * 0.5f),
            strokeWidth = w * 0.12f,
            cap = StrokeCap.Round
        )

        val head = Path().apply {
            moveTo(w * 0.45f, h * 0.22f)
            lineTo(w * 0.18f, h * 0.5f)
            lineTo(w * 0.45f, h * 0.78f)
        }

        drawPath(
            path = head,
            color = tint,
            style = stroke
        )
    }
}
