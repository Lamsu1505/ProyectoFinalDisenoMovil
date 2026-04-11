package com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent

import android.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.proyectofinaldisenomovil.core.component.DatePickerModal
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.*
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateEventScreen(
    viewModel: CreateEventViewModel = viewModel(),
    paddingValues: PaddingValues,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Crear Nuevo Evento",
                onNotificationsClick = onNotificationClick,
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(
            selectedRoute = ""
        ) },
        containerColor = whiteBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CategoryBadge(icon = Icons.Default.PushPin, label = "Información")
                infoSection(
                    uiState,
                    viewModel
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.Image, label = "Imagenes")
                imageSection(
                    uiState,
                    viewModel
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.LocationOn, label = "Ubicación")
                locationSection(
                    uiState,
                    viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.CalendarToday, label = "Fecha y Hora")
                dateSection(uiState, viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ButtonsSection( uiState, viewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun infoSection(
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    val colorLabels = MaterialTheme.colorScheme.onSurface
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Titulo del evento", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorLabels)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = uiState.title,
                maxLines = 2,
                onValueChange = { viewModel.onTitleChange(it) },
                placeholder = { Text("Agrega un titulo llamativo", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorLabels)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                maxLines = 10,
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                placeholder = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(10.dp))
                        Text("Describe de que se trata el evento", color = MaterialTheme.colorScheme.outline)
                        Text(
                            "Las descripciones detalladas reciben un 35% mas audiencia.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1.5f)) {
                    Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorLabels)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        OutlinedTextField(
                            value = uiState.category.label,
                            onValueChange = { },
                            readOnly = true,
                            placeholder = { Text("Categoria", color = Color.Gray) },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded },
                            shape = RoundedCornerShape(12.dp),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Black,
                                disabledTextColor = Color.Black,
                                disabledPlaceholderColor = Color.Gray,
                                disabledTrailingIconColor = green
                            )
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.5f),
                            shape = RoundedCornerShape(16.dp),

                            ) {
                            EventCategory.entries.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.label) },
                                    onClick = {
                                        viewModel.onCategoryChange(category)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Capacidad", fontWeight = FontWeight.Bold, color = colorLabels, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    var textFieldValue by remember {
                        mutableStateOf(TextFieldValue(uiState.capacity, TextRange(uiState.capacity.length)))
                    }

                    LaunchedEffect(uiState.capacity) {
                        if (uiState.capacity != textFieldValue.text) {
                            textFieldValue = textFieldValue.copy(
                                text = uiState.capacity,
                                selection = TextRange(uiState.capacity.length)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            val cleanInput = newValue.text.filter { it.isDigit() }

                            if (cleanInput.isEmpty()) {
                                textFieldValue = newValue.copy(text = "", selection = TextRange.Zero)
                                viewModel.onCapacityChange("")
                            } else if (cleanInput.length <= 9) {
                                val formatted = NumberFormat
                                    .getNumberInstance(Locale.forLanguageTag("es-CO"))
                                    .format(cleanInput.toLong())

                                val diff = formatted.length - newValue.text.length
                                val newCursor = (newValue.selection.end + diff).coerceIn(0, formatted.length)

                                textFieldValue = newValue.copy(
                                    text = formatted,
                                    selection = TextRange(newCursor)
                                )
                                viewModel.onCapacityChange(formatted)
                            }
                        },
                        placeholder = { Text("Ejm: 100", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(11.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun imageSection(
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    var selectedImageForDelete by remember { mutableStateOf<Uri?>(null) }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { viewModel.addImage(it) } }
    )

    val labelInfo = if (uiState.images.isEmpty()) {
        "Añade imagenes relevantes para el evento"
    } else {
        if (uiState.images.size == 1)
            "${uiState.images.size} Imagen añadida"
        else
            "${uiState.images.size} Imagenes añadidas"
    }

    Text(
        text = labelInfo,
        color = Color.Gray,
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 2.dp).fillMaxWidth(),
        textAlign = TextAlign.Center
    )


    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3,
                maxLines = 5
            ) {
                if (uiState.images.isEmpty()) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_menu_gallery),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                } else {
                    uiState.images.forEach { uri ->
                        Box(
                            modifier = Modifier
                                .size(width = 110.dp, height = 60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                                .clickable {
                                    selectedImageForDelete = if (selectedImageForDelete == uri) null else uri
                                }
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            
                            if (selectedImageForDelete == uri) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(onClick = { 
                                        viewModel.removeImage(uri)
                                        selectedImageForDelete = null
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        },
                        modifier = Modifier.size(50.dp)

                    ) {
                        Icon(
                            Icons.Default.AddCircleOutline,
                            contentDescription = "Add image",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun locationSection(
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Dirección directa", fontWeight = FontWeight.Bold, fontSize = 16.sp , color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = uiState.address,
                onValueChange = { viewModel.onAddressChange(it) },
                placeholder = { Text("Agrega la dirección del evento", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_mapmode),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f
                )

                Button(
                    onClick = { /* Map selection */ },
                    colors = ButtonDefaults.buttonColors(containerColor = orange),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdsClick, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Seleccionar en el mapa", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun dateSection(
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    if (showStartDatePicker) {
        DatePickerModal(
            onDateSelected = { 
                viewModel.onStartDateChange(it)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        DatePickerModal(
            onDateSelected = { 
                viewModel.onEndDateChange(it)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp).padding(vertical = 15.dp)) {
            // Inicio del evento
            Text(
                text = "Inicio del evento",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1.6f)) {
                    Text(
                        "Fecha",
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        OutlinedTextField(
                            value = uiState.startDate,
                            onValueChange = { },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Black,
                                disabledTextColor = Color.Black,
                                disabledPlaceholderColor = Color.Gray
                            )
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showStartDatePicker = true }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Hora",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.startTime,
                        onValueChange = { },
                        trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedTextColor = Color.Gray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Fin del evento
            Text(
                text = "Fin del evento (Opcional)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.outline,
                textDecoration = TextDecoration.Underline
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(
                        "Fecha",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        OutlinedTextField(
                            value = uiState.endDate,
                            onValueChange = { },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Black,
                                disabledTextColor = Color.Black,
                                disabledPlaceholderColor = Color.Gray
                            )
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showEndDatePicker = true }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Hora",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.endTime,
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonsSection(
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    Button(
        onClick = { viewModel.createEvent() },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = orange),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CreateNewFolder, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Crear evento", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoryBadge(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            color = green,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun LocationPreview() {
//    ProyectoFinalDisenoMovilTheme {
//        locationSection(navController = rememberNavController(), uiState = CreateEventUiState(), viewModel = CreateEventViewModel())
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DatePreview() {
//    ProyectoFinalDisenoMovilTheme {
//        dateSection(navController = rememberNavController(), uiState = CreateEventUiState(), viewModel = CreateEventViewModel())
//    }
//}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        CreateEventScreen(
            paddingValues = PaddingValues(0.dp)
        )
    }
}
