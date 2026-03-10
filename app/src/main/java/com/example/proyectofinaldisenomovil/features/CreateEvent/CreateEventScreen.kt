package com.example.proyectofinaldisenomovil.features.CreateEvent

import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: CreateEventViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Crear Nuevo Evento"
            )
        },
        bottomBar = { AppBottomBar(navController = navController) },
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
                infoSection(navController, uiState, viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.Image, label = "Imagenes")
                imageSection(navController, uiState, viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.LocationOn, label = "Ubicación")
                locationSection(navController, uiState, viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryBadge(icon = Icons.Default.CalendarToday, label = "Fecha y Hora")
                dateSection(navController, uiState, viewModel)
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ButtonsSection(navController, uiState, viewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun infoSection(
    navController: NavController,
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    val colorLabels = MaterialTheme.colorScheme.onSurface
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
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                placeholder = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(10.dp))
                        Text("Describe de que se trata el evento", color = Color.Gray)
                        Text(
                            "Las descripciones detalladas reciben un 35% mas audiencia.",
                            fontSize = 10.sp,
                            color = Color.Gray,
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
                    OutlinedTextField(
                        value = uiState.category,
                        onValueChange = { },
                        readOnly = true,
                        placeholder = { Text("Categoria", color = Color.Gray) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = green) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Capacidad", fontWeight = FontWeight.Bold, color = colorLabels, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.capacity,
                        onValueChange = { viewModel.onCapacityChange(it) },
                        placeholder = { Text("Ejm: 100", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(11.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black
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
    navController: NavController,
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
                                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
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
    navController: NavController,
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Dirección directa", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = uiState.address,
                onValueChange = { viewModel.onAddressChange(it) },
                placeholder = { Text("Agrega la dirección Ejemplo: Cra 18#49-05", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_mapmode),
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
    navController: NavController,
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Inicio del evento", fontWeight = FontWeight.Bold, fontSize = 16.sp, textDecoration = TextDecoration.Underline)
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = viewModel.eventDate?.toString() ?: "Selecciona una fecha",
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Hora", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.startTime,
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
    navController: NavController,
    uiState: CreateEventUiState,
    viewModel: CreateEventViewModel
) {
    Button(
        onClick = { viewModel.onCreateEvent() },
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        CreateEventScreen(navController = rememberNavController())
    }
}
