package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.core.theme.*
import com.example.proyectofinaldisenomovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    paddingValues: PaddingValues,
    onLogout: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("es") }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.profile_title),
                onNotificationsClick = onNotificationClick,
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(
            selectedRoute = ""
        ) },
        containerColor = whiteBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
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

                    // Profile Image (Initials)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB0BEC5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AZ",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiState.location,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Points Section
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
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.profile_level, uiState.level, uiState.levelName),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                            Text(text = stringResource(R.string.profile_points, uiState.points), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { uiState.points.toFloat() / (uiState.points + uiState.pointsToNextLevel) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = green,
                        trackColor = Color.LightGray,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.profile_points_remaining, uiState.pointsToNextLevel),
                        fontSize = 10.sp,
                        color = blue
                    )
                }
            }

            // Mis eventos section
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
                    EventStat(Icons.Default.DateRange, stringResource(R.string.profile_active), uiState.activeEvents.toString(), blue)
                    VerticalDivider(modifier = Modifier.height(50.dp), color = Color.LightGray)
                    EventStat(Icons.Default.CheckCircle, stringResource(R.string.profile_completed), uiState.completedEvents.toString(), green)
                    VerticalDivider(modifier = Modifier.height(50.dp), color = Color.LightGray)
                    EventStat(Icons.Default.DateRange, stringResource(R.string.profile_pending), uiState.pendingEvents.toString(), Color(0xFFFFA000))
                }
            }

            // Insignias section
            SectionTitle(stringResource(R.string.profile_badges))
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
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BadgeItem(Icons.Default.EmojiEvents, stringResource(R.string.profile_first_publication), Color(0xFFFFD700))
                    BadgeItem(Icons.Default.CheckCircle, stringResource(R.string.profile_10_completed), Color.Gray)
                    BadgeItem(Icons.Default.Star, stringResource(R.string.profile_50_completed), Color.LightGray)
                    BadgeItem(null, stringResource(R.string.profile_rating), Color.Black, isRating = true)
                }
            }

            // Menu Options
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
                    MenuItem(Icons.Default.Edit, stringResource(R.string.profile_edit)) {}
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                    MenuItem(Icons.Default.Language, stringResource(R.string.profile_language)) {
                        showLanguageDialog = true
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                    MenuItem(Icons.Default.Article, stringResource(R.string.profile_terms)) { /* Navigate */ }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                    MenuItem(Icons.AutoMirrored.Filled.ExitToApp, stringResource(R.string.profile_logout), isLogout = true) {
                        viewModel.onLogout()
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showLanguageDialog) {
            LanguageDialog(
                currentLanguage = selectedLanguage,
                onLanguageSelected = { code ->
                    selectedLanguage = code
                    showLanguageDialog = false
                    // aquí llamas tu lógica para cambiar el idioma
                },
                onDismiss = { showLanguageDialog = false }
            )
        }
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
fun BadgeItem(icon: ImageVector?, label: String, tint: Color, isRating: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isRating) {
            Text(text = "+4,7", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        } else if (icon != null) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(32.dp))
        }
        Text(
            text = label,
            fontSize = 8.sp,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp
        )
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, isLogout: Boolean = false, onClick: () -> Unit) {
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
fun LanguageDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        Pair("es", "Español"),
        Pair("en", "English"),
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
                        Text(text = name, fontSize = 16.sp)
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        ProfileScreen(
            paddingValues = PaddingValues(),
            onLogout = {}
        )
    }
}
