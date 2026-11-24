
package com.example.roadsync

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserAuthScreen(
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit,
    appLanguage: String = "English",
    authService: UserAuthService? = null
) {
    val isTagalog = appLanguage == "Tagalog"

    val context = LocalContext.current
    val resolvedAuthService = remember(authService) {
        authService ?: FirebaseManager()
    }

    var isRegisterMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Dark Navy + User Purple Palette
    val darkBg = Color(0xFF070B16)
    val bannerTop = Color(0xFF17102E)
    val bannerBottom = Color(0xFF070B16)
    val cardBg = Color(0xFF10182C)
    val fieldBg = Color(0xFF0B1120)
    val cardBorder = Color(0xFF1E2B4A)
    val userPurple = Color(0xFF8B5CF6)
    val userPurpleSoft = Color(0xFFA78BFA)
    val userPurpleDark = Color(0xFF6D28D9)
    val lightText = Color(0xFFF8FAFC)
    val mutedText = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        // TOP DARK PURPLE-NAVY GRADIENT BANNER
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

            // USER ICON BADGE
            Surface(
                modifier = Modifier.size(68.dp),
                shape = RoundedCornerShape(20.dp),
                color = userPurple.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, userPurple.copy(alpha = 0.45f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    WelcomeMatchedUserGraphic(
                        tint = userPurpleSoft,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // PORTAL BADGE
            Surface(
                shape = CircleShape,
                color = userPurple.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, userPurple.copy(alpha = 0.35f))
            ) {
                Text(
                    text = if (isTagalog)
                        "PORTAL 02 • USER"
                    else
                        "PORTAL 02 • USER",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color(0xFFC4B5FD)
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
                color = userPurpleSoft
            )

            Spacer(modifier = Modifier.height(22.dp))

            // DARK FORM CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.dp, cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // TITLE
                    Text(
                        text = if (isRegisterMode) {
                            if (isTagalog) "Gumawa ng Account"
                            else "Create User Account"
                        } else {
                            if (isTagalog) "Pag-login ng User"
                            else "User Login"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = lightText
                    )

                    // DESCRIPTION
                    Text(
                        text = if (isRegisterMode) {
                            if (isTagalog)
                                "Gumawa ng account upang makapag-ulat ng mga insidente sa kalsada, makakita ng mga update, at masubaybayan ang iyong mga ulat."
                            else
                                "Create an account to report road incidents, view updates, and track your submissions."
                        } else {
                            if (isTagalog)
                                "Mag-login upang makapag-ulat ng mga insidente sa kalsada, makakita ng mga update, at masubaybayan ang iyong mga ulat."
                            else
                                "Sign in to report road incidents, view updates, and track your submissions."
                        },
                        fontSize = 13.sp,
                        color = mutedText,
                        lineHeight = 18.sp
                    )

                    // INLINE ERROR ALERT
                    AnimatedVisibility(visible = errorMessage != null) {
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
                                text = errorMessage.orEmpty(),
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFFFCA5A5),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                if (isTagalog) "Email Address" else "Email Address"
                            )
                        },
                        placeholder = { Text("name@example.com") },
                        leadingIcon = {
                            WelcomeMatchedMailGraphic(
                                tint = userPurpleSoft,
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
                            focusedBorderColor = userPurple,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = userPurpleSoft,
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
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(if (isTagalog) "Password" else "Password")
                        },
                        placeholder = {
                            Text(
                                if (isTagalog) "Ilagay ang iyong password"
                                else "Enter your password"
                            )
                        },
                        leadingIcon = {
                            WelcomeMatchedLockGraphic(
                                tint = userPurpleSoft,
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
                            imeAction = if (isRegisterMode)
                                ImeAction.Next
                            else
                                ImeAction.Done
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = userPurple,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = userPurpleSoft,
                            unfocusedLabelColor = mutedText,
                            focusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            unfocusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                            focusedContainerColor = fieldBg,
                            unfocusedContainerColor = fieldBg,
                            focusedTextColor = lightText,
                            unfocusedTextColor = lightText
                        )
                    )

                    // CONFIRM PASSWORD FIELD
                    AnimatedVisibility(visible = isRegisterMode) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                errorMessage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    if (isTagalog) "Kumpirmahin ang Password"
                                    else "Confirm Password"
                                )
                            },
                            placeholder = {
                                Text(
                                    if (isTagalog) "Ilagay muli ang iyong password"
                                    else "Re-enter your password"
                                )
                            },
                            leadingIcon = {
                                WelcomeMatchedLockGraphic(
                                    tint = userPurpleSoft,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        confirmPasswordVisible =
                                            !confirmPasswordVisible
                                    },
                                    enabled = !isLoading
                                ) {
                                    WelcomeMatchedEyeGraphic(
                                        isVisible = confirmPasswordVisible,
                                        tint = mutedText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            singleLine = true,
                            enabled = !isLoading,
                            visualTransformation = if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = userPurple,
                                unfocusedBorderColor = cardBorder,
                                focusedLabelColor = userPurpleSoft,
                                unfocusedLabelColor = mutedText,
                                focusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                                unfocusedPlaceholderColor = mutedText.copy(alpha = 0.7f),
                                focusedContainerColor = fieldBg,
                                unfocusedContainerColor = fieldBg,
                                focusedTextColor = lightText,
                                unfocusedTextColor = lightText
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // PRIMARY ACTION BUTTON
                    Button(
                        onClick = {
                            errorMessage = null

                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = if (isTagalog)
                                    "Ilagay ang iyong email address at password."
                                else
                                    "Please enter both your email address and password."
                                return@Button
                            }

                            if (isRegisterMode && password != confirmPassword) {
                                errorMessage = if (isTagalog)
                                    "Hindi magkapareho ang mga password."
                                else
                                    "Passwords do not match."
                                return@Button
                            }

                            isLoading = true

                            if (isRegisterMode) {
                                resolvedAuthService.registerUser(
                                    email = email.trim(),
                                    password = password,
                                    onSuccess = { uid ->
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            if (isTagalog)
                                                "Matagumpay na nagawa ang account!"
                                            else
                                                "Account created successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onLoginSuccess(uid)
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                    }
                                )
                            } else {
                                resolvedAuthService.signInAsUser(
                                    email = email.trim(),
                                    password = password,
                                    onSuccess = { uid ->
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            if (isTagalog)
                                                "Matagumpay ang pag-login!"
                                            else
                                                "Login successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onLoginSuccess(uid)
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = userPurple,
                            disabledContainerColor = userPurpleDark.copy(alpha = 0.35f),
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
                                text = if (isRegisterMode) {
                                    if (isTagalog) "Gumawa ng Account"
                                    else "Create Account"
                                } else {
                                    if (isTagalog) "Mag-login"
                                    else "Login"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // SWITCH BETWEEN LOGIN & REGISTER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isRegisterMode) {
                                if (isTagalog)
                                    "May account ka na ba? "
                                else
                                    "Already have an account? "
                            } else {
                                if (isTagalog)
                                    "Wala ka pang account? "
                                else
                                    "Don't have an account? "
                            },
                            fontSize = 13.sp,
                            color = mutedText
                        )

                        TextButton(
                            onClick = {
                                isRegisterMode = !isRegisterMode
                                password = ""
                                confirmPassword = ""
                                passwordVisible = false
                                confirmPasswordVisible = false
                                errorMessage = null
                            },
                            enabled = !isLoading,
                            contentPadding = PaddingValues(
                                horizontal = 4.dp,
                                vertical = 0.dp
                            )
                        ) {
                            Text(
                                text = if (isRegisterMode) {
                                    if (isTagalog) "Mag-login" else "Login"
                                } else {
                                    if (isTagalog) "Magparehistro" else "Register"
                                },
                                color = userPurpleSoft,
                                fontSize = 13.sp,
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
                    tint = userPurpleSoft,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTagalog) "Bumalik sa Mapa" else "Back to Map",
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
private fun WelcomeMatchedUserGraphic(
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
