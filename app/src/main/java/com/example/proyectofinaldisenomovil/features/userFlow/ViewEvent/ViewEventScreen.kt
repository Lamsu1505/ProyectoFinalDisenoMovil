package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// ─────────────────────────────────────────────
//  DATA CLASS  – Comentario
// ─────────────────────────────────────────────
data class Comment(
    val authorInitials: String,
    val authorName: String,
    val timeLabel: String,
    val text: String
)

// ─────────────────────────────────────────────
//  PANTALLA PRINCIPAL
// ─────────────────────────────────────────────
@Composable
fun ViewEventScreen(
    eventId: String,
    onBackClick: () -> Unit,
    viewEventViewModel: ViewEventViewModel = hiltViewModel()
) {
    Log.e("ID", eventId)

    val event        by viewEventViewModel.currentEvent.collectAsState()
    val detailResult by viewEventViewModel.detailResult.collectAsState()
    val isInterested by viewEventViewModel.isInterested.collectAsState()
    val isConfirmed  by viewEventViewModel.isConfirmed.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(eventId) { viewEventViewModel.findEventById(eventId) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = event?.title ?: stringResource(R.string.view_event_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(selectedRoute = "") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (detailResult) {
                is RequestResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is RequestResult.Failure -> {
                    ErrorState((detailResult as RequestResult.Failure).errorMessage)
                }
                is RequestResult.Success -> {
                    event?.let { safeEvent ->
                        EventDetailContent(
                            event        = safeEvent,
                            isInterested = isInterested,
                            isConfirmed  = isConfirmed,
                            onInterestedClick = viewEventViewModel::toggleInterested,
                            onConfirmedClick  = viewEventViewModel::toggleConfirmed,
                            listState    = listState
                        )
                    }
                }
                else -> { /* Estado inicial */ }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  CONTENIDO PRINCIPAL  (LazyColumn)
// ─────────────────────────────────────────────
@Composable
private fun EventDetailContent(
    event: Event,
    isInterested: Boolean,
    isConfirmed: Boolean,
    onInterestedClick: () -> Unit,
    onConfirmedClick: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    // Estado local para el campo de nuevo comentario
    var newCommentText by remember { mutableStateOf("") }
    var comments by remember {
        mutableStateOf(
            listOf(
                Comment("AP", "Andres Perez",      "Ahora",   "Hola quiero saber precio y lugar. Muchas gracias. Nos vemos allí."),
                Comment("NT", "Natalia Tejada",    "Hace 1h", "Es un evento no apto para mascotas, es muy lamentableeee."),
                Comment("SL", "Santiago Londoño",  "Hace 2h", "Soy londoño."),
                Comment("SL", "Santiago Londoño",  "Hace 2h", "Soy londoño."),
                Comment("SL", "Santiago Londoño",  "Hace 2h", "Soy londoño.")
            )
        )
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        item { EventImageHeader(event) }
        item {
            EventInfoSection(
                event             = event,
                isInterested      = isInterested,
                isConfirmed       = isConfirmed,
                onInterestedClick = onInterestedClick,
                onConfirmedClick  = onConfirmedClick
            )
        }
        item { EventDescription(event.description) }
        item { EventMapPlaceholder() }

        // ── SECCIÓN COMENTARIOS ──────────────────
        item { CommentsSection(comments = comments) }

        // ── CAMPO PARA NUEVO COMENTARIO ──────────
        item {
            NewCommentButton(
                value    = newCommentText,
                onValueChange = { newCommentText = it },
                onSend   = {
                    if (newCommentText.isNotBlank()) {
                        comments = listOf(
                            Comment("YO", "Tú", "Ahora", newCommentText)
                        ) + comments
                        newCommentText = ""
                    }
                }
            )
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// ─────────────────────────────────────────────
//  CABECERA  – Imagen + badge categoría
// ─────────────────────────────────────────────
@Composable
private fun EventImageHeader(event: Event) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)) {
        AsyncImage(
            model              = event.imageUrls.firstOrNull(),
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )
        // Badge categoría – color secondary (naranja) igual que mockup
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            color    = MaterialTheme.colorScheme.secondary,
            shape    = RoundedCornerShape(50)
        ) {
            Text(
                text     = event.category.label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color    = Color.White,
                style    = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
        // Indicador de fotos  "1 / 5"
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.BottomEnd),
            color    = Color.Black.copy(alpha = 0.45f),
            shape    = RoundedCornerShape(6.dp)
        ) {
            Text(
                text     = "1 / ${event.imageUrls.size.coerceAtLeast(1)}",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                color    = Color.White,
                fontSize = 11.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
//  SECCIÓN INFO  – tarjeta verde redondeada
// ─────────────────────────────────────────────
@Composable
private fun EventInfoSection(
    event: Event,
    isInterested: Boolean,
    isConfirmed: Boolean,
    onInterestedClick: () -> Unit,
    onConfirmedClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "CO"))
    val timeFormat = SimpleDateFormat("h:mm a",           Locale("es", "CO"))

    // Fondo blanco/hueso detrás para que la tarjeta verde se vea bien
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título centrado
            Text(
                text       = event.title,
                style      = MaterialTheme.typography.headlineSmall,
                color      = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.25f))
            Spacer(Modifier.height(16.dp))

            Row(
                modifier             = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment    = Alignment.Top
            ) {
                // ── Columna izquierda: metadatos ──
                Column(
                    modifier             = Modifier.weight(1f),
                    verticalArrangement  = Arrangement.spacedBy(10.dp)
                ) {
                    IconLabel(Icons.Default.CalendarToday,
                        event.startDate?.let { dateFormat.format(it.toDate()) } ?: "")
                    IconLabel(Icons.Default.Schedule,
                        event.startDate?.let { timeFormat.format(it.toDate()) } ?: "")
                    IconLabel(Icons.Default.LocationOn, event.address)
                    IconLabel(
                        icon  = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        label = "${event.importantVotes} interesados",
                        tint  = if (isInterested) Color(0xFFFF6B6B) else Color.White
                    )
                    // Asistentes confirmados  (ícono Personas)
                    IconLabel(
                        icon  = Icons.Default.Group,
                        label = "${event.id ?: 0} / ${event.id ?: "∞"}"
                    )
                }

                Spacer(Modifier.width(12.dp))

                // ── Columna derecha: organizado por + botones ──
                Column(
                    modifier            = Modifier.width(148.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text  = "Organizado por:",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.sp
                    )
                    Text(
                        text       = event.authorName,
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        fontSize   = 13.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    // Botón "Me interesa" / "Interesado"
                    ActionButton(
                        text       = if (isInterested) "Interesado" else "Me interesa",
                        icon       = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        isSelected = isInterested,
                        isPrimary  = false,
                        onClick    = onInterestedClick
                    )

                    Spacer(Modifier.height(8.dp))

                    // Botón "Confirmar" / "Confirmado"  – siempre fondo blanco (isPrimary=true)
                    ActionButton(
                        text       = if (isConfirmed) "Estás confirmado" else "Confirmar",
                        icon       = Icons.Default.Check,
                        isSelected = isConfirmed,
                        isPrimary  = true,
                        onClick    = onConfirmedClick
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  BOTÓN DE ACCIÓN  (Me interesa / Confirmar)
// ─────────────────────────────────────────────
@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    val filled = isSelected || isPrimary
    Button(
        onClick         = onClick,
        modifier        = Modifier
            .fillMaxWidth()
            .height(40.dp),
        colors          = ButtonDefaults.buttonColors(
            containerColor = if (filled) Color.White else Color.Transparent,
            contentColor   = if (filled) MaterialTheme.colorScheme.primary else Color.White
        ),
        border          = if (!filled) BorderStroke(1.5.dp, Color.White) else null,
        contentPadding  = PaddingValues(horizontal = 10.dp),
        shape           = RoundedCornerShape(10.dp),
        elevation       = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(5.dp))
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

// ─────────────────────────────────────────────
//  ICONO + ETIQUETA
// ─────────────────────────────────────────────
@Composable
private fun IconLabel(
    icon: ImageVector,
    label: String,
    tint: Color = Color.White
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(7.dp))
        Text(
            text     = label,
            color    = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─────────────────────────────────────────────
//  DESCRIPCIÓN
// ─────────────────────────────────────────────
@Composable
private fun EventDescription(description: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Text(
            "Descripción del evento",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text      = description,
            style     = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            color     = MaterialTheme.colorScheme.onSurface
        )
    }
}

// ─────────────────────────────────────────────
//  MAPA PLACEHOLDER  (sin cambios)
// ─────────────────────────────────────────────
@Composable
private fun EventMapPlaceholder() {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            "Ubicación exacta",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))
        Box(
            modifier           = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.LightGray),
            contentAlignment   = Alignment.Center
        ) {
            Text("Mapa próximamente", color = Color.DarkGray)
        }
    }
}

// ─────────────────────────────────────────────
//  SECCIÓN COMENTARIOS  ← NUEVA
// ─────────────────────────────────────────────
@Composable
private fun CommentsSection(comments: List<Comment>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Text(
            text       = "Comentarios",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        // Tarjeta contenedora color surface (hueso claro según tema)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(16.dp),
            color    = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                comments.forEachIndexed { index, comment ->
                    CommentItem(comment)
                    if (index < comments.lastIndex) {
                        HorizontalDivider(
                            modifier  = Modifier.padding(horizontal = 16.dp),
                            color     = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  ITEM COMENTARIO  ← NUEVO
// ─────────────────────────────────────────────
@Composable
private fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar circular con iniciales
        Box(
            modifier         = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = comment.authorInitials,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement  = Arrangement.SpaceBetween,
                modifier               = Modifier.fillMaxWidth()
            ) {
                Text(
                    text       = comment.authorName,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground,
                    modifier   = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text     = comment.timeLabel,
                    fontSize = 11.sp,
                    color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text     = comment.text,
                fontSize = 13.sp,
                color    = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ─────────────────────────────────────────────
//  BOTÓN / CAMPO NUEVO COMENTARIO  ← NUEVO
// ─────────────────────────────────────────────
@Composable
private fun NewCommentButton(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Si hay texto mostramos el campo, si no el botón naranja del mockup
        if (value.isEmpty()) {
            Button(
                onClick  = { onValueChange(" ") }, // abre el campo
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(44.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // naranja
                    contentColor   = Color.White
                ),
                shape    = RoundedCornerShape(50)
            ) {
                Text("Hacer comentario", fontWeight = FontWeight.Bold)
            }
        } else {
            OutlinedTextField(
                value           = value.trimStart(),
                onValueChange   = onValueChange,
                modifier        = Modifier.fillMaxWidth(),
                placeholder     = { Text("Escribe tu comentario…") },
                maxLines        = 4,
                shape           = RoundedCornerShape(12.dp),
                trailingIcon    = {
                    IconButton(onClick = onSend) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}

// ─────────────────────────────────────────────
//  ESTADO DE ERROR
// ─────────────────────────────────────────────
@Composable
private fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

// ─────────────────────────────────────────────
//  PREVIEW
// ─────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun ViewEventScreenPreview() {
    ViewEventScreen(eventId = "event_013", onBackClick = {})
}