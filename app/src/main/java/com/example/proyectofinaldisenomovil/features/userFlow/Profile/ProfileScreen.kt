package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppSnackbarHost
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.theme.*
import com.example.proyectofinaldisenomovil.domain.model.BadgeCategory
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.features.settings.SettingsViewModel

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
    onMyEventsClick: () -> Unit = {}
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val saveProfileResult by profileViewModel.saveProfileResult.collectAsState()
    val context = LocalContext.current

    var showLanguageDialog by remember { mutableStateOf(false) }
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        Text(
                            text = "img",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.name?.let {
                        Log.i("ProfileScreen", "Nombre en perfil: $it")
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
                                Text(
                                    text = emoji,
                                    fontSize = 20.sp
                                )
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
                            text = stringResource(R.string.gamification_points_remaining, pointsRemaining),
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
                    .padding(horizontal = 24.dp)
                    .clickable { onMyEventsClick() },
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
                    EventStat(Icons.Default.DateRange, stringResource(R.string.profile_active), uiState.activeEvents.toString(), blue)
                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.LightGray))
                    EventStat(Icons.Default.CheckCircle, stringResource(R.string.profile_completed), uiState.completedEvents.toString(), green)
                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.LightGray))
                    EventStat(Icons.Default.DateRange, stringResource(R.string.profile_pending), uiState.pendingEvents.toString(), Color(0xFFFFA000))
                }
            }

            SectionTitle(stringResource(R.string.profile_badges))
            BadgesCarousel(
                badges = uiState.badges,
                onViewAllClick = { /* TODO: Navigate to all badges screen */ }
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

                    MenuItem(Icons.Default.Article, stringResource(R.string.profile_terms), false) {}
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                    MenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        text = stringResource(R.string.profile_logout),
                        isLogout = true
                    ) { onLogout() }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
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
        textAlign = TextAlign.Center
    )
}

@Composable
fun EventStat(icon: ImageVector, label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text(text = count, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun BadgeItem(icon: ImageVector?, label: String, tint: Color, isRating: Boolean = false , uiState: ProfileUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isRating) {
            Text(text = ("+" + uiState.rating), fontSize = 25.sp, fontWeight = FontWeight.Bold)
        } else if (icon != null) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(32.dp))
        }
        Text(
            text = ("+" + uiState.rating +" "+ label),
            fontSize = 8.sp,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp
        )
    }
}

@Composable
fun BadgesCarousel(
    badges: List<BadgeType>,
    onViewAllClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (badges.isEmpty()) {
                EmptyBadgesState()
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${badges.size} insignias",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    TextButton(onClick = onViewAllClick) {
                        Text(
                            text = stringResource(R.string.gamification_view_all_badges),
                            fontSize = 12.sp,
                            color = blue
                        )
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = blue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(badges.take(6)) { badge ->
                        BadgeCard(badge = badge)
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeCard(badge: BadgeType) {
    val (icon, tint) = getBadgeIconAndColor(badge)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = badge.label,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = badge.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            color = Color.DarkGray
        )
    }
}

@Composable
fun EmptyBadgesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.gamification_no_badges),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.gamification_earn_badges),
            fontSize = 12.sp,
            color = Color.LightGray
        )
    }
}

fun getBadgeIconAndColor(badge: BadgeType): Pair<ImageVector, Color> {
    return when (badge.category) {
        BadgeCategory.CREADOR -> when (badge) {
            BadgeType.PRIMERA_PUBLICACION -> Icons.Default.Edit to Color(0xFF4CAF50)
            BadgeType.PRODUCTOR -> Icons.Default.EmojiEvents to Color(0xFF8BC34A)
            BadgeType.ORGANIZADOR_EXPERTO -> Icons.Default.Star to Color(0xFFFFD700)
            BadgeType.MAESTRO_EVENTOS -> Icons.Default.WorkspacePremium to Color(0xFFFF9800)
            else -> Icons.Default.EmojiEvents to Color(0xFF4CAF50)
        }
        BadgeCategory.SOCIAL -> when (badge) {
            BadgeType.PRIMER_PASO -> Icons.Default.CheckCircle to Color(0xFF2196F3)
            BadgeType.ASISTENTE_FIEL -> Icons.Default.Groups to Color(0xFF03A9F4)
            BadgeType.VETERANO -> Icons.Default.Groups to Color(0xFF00BCD4)
            BadgeType.LEYENDA -> Icons.Default.Star to Color(0xFF9C27B0)
            else -> Icons.Default.Groups to Color(0xFF2196F3)
        }
        BadgeCategory.COMUNIDAD -> when (badge) {
            BadgeType.PRIMER_COMENTARIO -> Icons.Default.Forum to Color(0xFF607D8B)
            BadgeType.CONVERSADOR -> Icons.Default.Forum to Color(0xFF795548)
            BadgeType.HISTORIADOR -> Icons.Default.Forum to Color(0xFF9E9E9E)
            BadgeType.VOZ_COMUNIDAD -> Icons.Default.Forum to Color(0xFF3F51B5)
            else -> Icons.Default.Forum to Color(0xFF607D8B)
        }
        BadgeCategory.POPULAR -> when (badge) {
            BadgeType.RELEVANTE -> Icons.Default.TrendingUp to Color(0xFFE91E63)
            BadgeType.POPULAR -> Icons.Default.TrendingUp to Color(0xFFFF5722)
            BadgeType.ESTRELLA -> Icons.Default.Star to Color(0xFFFFD700)
            BadgeType.ICONO -> Icons.Default.WorkspacePremium to Color(0xFFFF9800)
            else -> Icons.Default.TrendingUp to Color(0xFFE91E63)
        }
        BadgeCategory.ESPECIAL -> when (badge) {
            BadgeType.FUNDADOR -> Icons.Default.WorkspacePremium to Color(0xFFFFD700)
            BadgeType.EARLY_ADOPTER -> Icons.Default.Star to Color(0xFF9C27B0)
            BadgeType.MODERATOR -> Icons.Default.CheckCircle to Color(0xFF4CAF50)
            BadgeType.VOLUNTARIO -> Icons.Default.Groups to Color(0xFF2196F3)
            else -> Icons.Default.WorkspacePremium to Color(0xFFFFD700)
        }
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        ProfileScreen(
            paddingValues = PaddingValues(),
            onLogout = {}
        )
    }
}

private fun calculateProgress(points: Int, level: Int): Float {
    if (level < 1 || level > 7) return 0f
    
    val levels = listOf(
        0 to 100,    // ESPECTADOR -> NOVATO (100)
        100 to 300, // NOVATO -> PARTICIPANTE (200)
        300 to 600, // PARTICIPANTE -> ENTUSIASTA (300)
        600 to 1200, // ENTUSIASTA -> CREADOR (600)
        1200 to 2500, // CREADOR -> ORGANIZADOR (1300)
        2500 to 5000, // ORGANIZADOR -> LIDER (2500)
        5000 to 5000  // LIDER max
    )
    
    val (minPoints, maxPoints) = levels.getOrElse(level - 1) { 0 to 100 }
    val range = maxPoints - minPoints
    
    if (range <= 0) return 1f
    
    val progress = (points - minPoints).toFloat() / range.toFloat()
    return progress.coerceIn(0f, 1f)
}
