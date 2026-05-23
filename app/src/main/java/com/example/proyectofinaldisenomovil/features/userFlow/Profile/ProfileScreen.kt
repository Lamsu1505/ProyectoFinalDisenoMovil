package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppSnackbarHost
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.*
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.features.settings.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onLogout: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onMyEventsClick: (EventStatus) -> Unit = {}
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val saveProfileResult by profileViewModel.saveProfileResult.collectAsState()
    val context = LocalContext.current

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var selectedBadge by remember { mutableStateOf<BadgeType?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    LaunchedEffect(saveProfileResult) {
        saveProfileResult?.let { result ->
            when (result) {
                is RequestResult.Success -> {
                    snackbarHostState.showSnackbar(result.message)
                    profileViewModel.resetSaveProfileResult()
                }
                is RequestResult.Failure -> {
                    snackbarHostState.showSnackbar(result.errorMessage)
                    profileViewModel.resetSaveProfileResult()
                }
                is RequestResult.Loading -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = stringResource(R.string.profile_title),
                onNotificationsClick = onNotificationClick,
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(selectedRoute = "") },
        containerColor = whiteBackground
    ) { innerPadding ->
        if (uiState.isLoading == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = green)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                        .background(green)
                        .padding(bottom = 32.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB0BEC5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uiState.profileImageUrl),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        uiState.name?.let {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            uiState.location?.let {
                                Text(
                                    text = it,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Level and Points Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                uiState.levelEmoji?.let { emoji ->
                                    Text(text = emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                uiState.levelName?.let { levelName ->
                                    Text(
                                        text = levelName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = green
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                uiState.points?.let { points ->
                                    Text(
                                        text = stringResource(R.string.gamification_points, points),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        uiState.level?.let { level ->
                            val progress = calculateProgress(uiState.points ?: 0, level)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = green,
                                trackColor = Color(0xFFE0E0E0),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        uiState.pointsToNextLevel?.let { pointsRemaining ->
                            Text(
                                text = if (pointsRemaining > 0)
                                    stringResource(R.string.gamification_points_remaining, pointsRemaining)
                                else "¡Nivel máximo alcanzado!",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle(stringResource(R.string.profile_my_events))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EventStat(
                            icon = Icons.Default.CheckCircle,
                            label = stringResource(R.string.profile_active),
                            count = uiState.activeEvents?.toString() ?: "0",
                            color = blue,
                            onClick = { onMyEventsClick(EventStatus.VERIFIED) }
                        )
                        Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.LightGray))

                        EventStat(
                            icon = Icons.Default.Cancel,
                            label = "Rechazados",
                            count = uiState.completedEvents?.toString() ?: "0",
                            color = red,
                            onClick = { onMyEventsClick(EventStatus.REJECTED) }
                        )
                        Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.LightGray))

                        EventStat(
                            icon = Icons.Default.DateRange,
                            label = stringResource(R.string.profile_pending),
                            count = uiState.pendingEvents?.toString() ?: "0",
                            color = Color(0xFFFFA000),
                            onClick = { onMyEventsClick(EventStatus.PENDING_REVIEW) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionTitle(stringResource(R.string.profile_trophies))
                TrophiesGrid(
                    earnedBadges = uiState.badges,
                    onBadgeClick = { selectedBadge = it }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column {
                        MenuItem(Icons.Default.Edit, stringResource(R.string.profile_edit), false) {
                            onEditProfileClick()
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                        MenuItem(Icons.Default.Language, stringResource(R.string.profile_language), false) {
                            showLanguageDialog = true
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                        MenuItem(Icons.Default.Article, "Términos y Condiciones", false) {}
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                        MenuItem(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            text = stringResource(R.string.profile_logout),
                            isLogout = true
                        ) { showLogoutDialog = true }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showLanguageDialog) {
        LanguageChangeDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { code ->
                settingsViewModel.setLanguage(code)
                profileViewModel.setLanguage(code)
                showLanguageDialog = false
                (context as? Activity)?.let { activity ->
                    val locale = java.util.Locale(code)
                    val config = android.content.res.Configuration(context.resources.configuration)
                    config.setLocale(locale)
                    context.resources.updateConfiguration(config, context.resources.displayMetrics)
                    activity.recreate()
                }
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = stringResource(R.string.profile_logout)) },
            text = { Text(text = "¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text(text = "Sí, cerrar sesión", color = red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "Cancelar", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }

    selectedBadge?.let { badge ->
        BadgeDetailDialog(
            badge = badge,
            isEarned = uiState.badges.any { it.name == badge.name },
            onDismiss = { selectedBadge = null }
        )
    }
}

@Composable
fun TrophiesGrid(
    earnedBadges: List<BadgeType>,
    onBadgeClick: (BadgeType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val allBadges = BadgeType.entries

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                allBadges.take(4).forEach { badge ->
                    BadgeIconItem(
                        badge = badge,
                        isEarned = earnedBadges.any { it.name == badge.name },
                        onClick = { onBadgeClick(badge) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                allBadges.drop(4).forEach { badge ->
                    BadgeIconItem(
                        badge = badge,
                        isEarned = earnedBadges.any { it.name == badge.name },
                        onClick = { onBadgeClick(badge) }
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeIconItem(
    badge: BadgeType,
    isEarned: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isEarned) green.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = badge.img),
                contentDescription = badge.label,
                modifier = Modifier
                    .size(36.dp)
                    .then(
                        if (!isEarned) Modifier.drawWithGrayscale() else Modifier
                    )
            )
        }
    }
}

fun Modifier.drawWithGrayscale(): Modifier = this.then(
    Modifier.drawWithContent {

        val paint = Paint()

        val colorMatrix = ColorMatrix().apply {
            setToSaturation(0f)
        }

        paint.colorFilter = ColorFilter.colorMatrix(
            androidx.compose.ui.graphics.ColorMatrix(
                floatArrayOf(
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0f,    0f,    0f,    1f, 0f
                )
            )
        )

        drawContext.canvas.saveLayer(size.toRect(), paint)
        drawContent()
        drawContext.canvas.restore()
    }
)

@Composable
fun BadgeDetailDialog(
    badge: BadgeType,
    isEarned: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Image(
                painter = painterResource(id = badge.img),
                contentDescription = badge.label,
                modifier = Modifier.size(64.dp)
            )
        },
        title = {
            Text(text = badge.label, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = badge.description,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isEarned) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = green, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("¡Conseguido!", color = green, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("Aún no has conseguido este trofeo", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = green)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun EventStat(icon: ImageVector, label: String, count: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text(text = count, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    text: String,
    isLogout: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isLogout) red else Color.DarkGray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = if (isLogout) red else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            if (!isLogout) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun LanguageChangeDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        Pair("es", "Español"),
        Pair("en", "English"),
    )

    val flagEmojis = mapOf(
        "es" to "\uD83C\uDDEA\uD83C\uDDF8",
        "en" to "\uD83C\uDDFA\uD83C\uDDF8"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.profile_select_language),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                languages.forEach { (code, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(code) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = flagEmojis[code] ?: "", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = name, fontSize = 16.sp)
                        }
                        if (code == currentLanguage) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = green,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    if (code != languages.last().first) {
                        HorizontalDivider(color = Color.LightGray)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

private fun calculateProgress(points: Int, level: Int): Float {
    val levels = UserLevel.entries
    val currentLevelIndex = (level - 1).coerceIn(0, levels.size - 1)
    val currentLevel = levels[currentLevelIndex]
    val nextLevel = currentLevel.nextLevel() ?: return 1f

    val minPoints = currentLevel.minPoints
    val maxPoints = nextLevel.minPoints
    val range = maxPoints - minPoints

    if (range <= 0) return 1f

    val progress = (points - minPoints).toFloat() / range.toFloat()
    return progress.coerceIn(0f, 1f)
}
