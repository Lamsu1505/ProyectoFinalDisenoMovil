package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.core.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Reusable card used in the moderator panel event list.
 *
 * Shows a banner image with a category badge overlay, then the event title,
 * date, time, location and the two action buttons (Aceptar / Rechazar).
 *
 * @param event         The [Event] data to display.
 * @param onCardClick   Called when the user taps the card body → opens detail screen.
 * @param onAccept      Called when "Aceptar" is tapped.
 * @param onReject      Called when "Rechazar" is tapped → opens confirmation dialog.
 * @param modifier      Optional layout modifier.
 */
@Composable
fun ModeratorEventCard(
    navController: NavController,
    event: Event,
    onCardClick: (Event) -> Unit,
    onAccept: (Event) -> Unit,
    onReject: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .clickable { onCardClick(event) },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column {
            // ── Banner image with category badge overlay ───────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            ) {
                // TODO: replace placeholder with real image from event.imageUrls.firstOrNull()
                AsyncImage(
                    model             = event.imageUrls.firstOrNull(),
                    contentDescription = event.title,
                    contentScale      = ContentScale.Crop,
                    modifier          = Modifier.fillMaxSize(),
                )

                // Category badge — top-left overlay
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Text(
                        text     = event.category.label,
                        color    = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                }
            }

            // ── Content area ──────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                // Title
                Text(
                    text       = event.title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    color      = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(6.dp))

                // Date / Time / Location row and action buttons side-by-side
                Row(
                    modifier                  = Modifier.fillMaxWidth(),
                    verticalAlignment         = Alignment.Bottom,
                    horizontalArrangement     = Arrangement.SpaceBetween,
                ) {
                    // ── Left: meta info ──────────────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        MetaRow(
                            icon  = Icons.Outlined.CalendarToday,
                            label = event.startDate?.let {
                                SimpleDateFormat("EEEE d 'de' MMM", Locale("es")).format(it.toDate())
                            } ?: "Fecha no definida",
                        )
                        MetaRow(
                            icon  = Icons.Outlined.Schedule,
                            label = event.startDate?.let {
                                SimpleDateFormat("h:mm a", Locale.getDefault()).format(it.toDate())
                            } ?: "--",
                        )
                        MetaRow(
                            icon  = Icons.Outlined.LocationOn,
                            label = event.address.ifBlank { "Ubicacion no definida" },
                        )
                    }

                    // ── Right: action buttons ────────────────────────────────
                    Column(
                        verticalArrangement   = Arrangement.spacedBy(6.dp),
                        horizontalAlignment   = Alignment.End,
                    ) {
                        ActionButton(
                            label      = "Aceptar",
                            color      = MaterialTheme.colorScheme.primary,
                            icon       = Icons.Rounded.CheckCircle,
                            onClick    = { onAccept(event) },
                        )
                        ActionButton(
                            label      = "Rechazar",
                            color      = MaterialTheme.colorScheme.secondary,
                            icon       = Icons.Rounded.Cancel,
                            onClick    = { onReject(event) },
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MetaRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.onSurface,
            modifier           = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ActionButton(
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    Button(
        onClick  = onClick,
        colors   = ButtonDefaults.buttonColors(containerColor = color),
        shape    = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        modifier = Modifier.height(32.dp),
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            modifier           = Modifier.size(14.dp),
            tint               = Color.White,
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorEventCardPreview(){
    ProyectoFinalDisenoMovilTheme() {
        ModeratorEventCard(
            navController = rememberNavController(),
            event = Event(
                id = "1",
                title = "Concierto de Rock en el Parque",
                description = "Disfruta de una noche llena de música y energía con las mejores bandas de rock locales e internacionales. ¡No te lo pierdas!",
                category = com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory.DEPORTES,
                startDate = com.google.firebase.Timestamp.now(),
                endDate = com.google.firebase.Timestamp.now(),
                address = "Parque Central, Ciudad",
                imageUrls = listOf("https://example.com/event-image.jpg"),
            ),
            onCardClick = {},
            onAccept = {},
            onReject = {},
        )
    }
}