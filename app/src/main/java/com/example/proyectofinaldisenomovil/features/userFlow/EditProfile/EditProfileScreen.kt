package com.example.proyectofinaldisenomovil.features.userFlow.EditProfile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.theme.blue
import com.example.proyectofinaldisenomovil.core.theme.green
import com.example.proyectofinaldisenomovil.core.theme.orange
import com.example.proyectofinaldisenomovil.core.theme.red
import com.example.proyectofinaldisenomovil.core.theme.whiteBackground
import com.example.proyectofinaldisenomovil.features.userFlow.Profile.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.editUiState.collectAsState()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var cityExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            viewModel.onPhotoSelected(photoUri!!)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onPhotoSelected(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            photoUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile_title),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = green)
            )
        },
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
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.photoUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(uiState.photoUri),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp),
                                    tint = Color.Gray
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .offset(x = (-4).dp, y = (-4).dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp
                        ) {
                            IconButton(onClick = { viewModel.onShowImagePicker() }) {
                                Icon(
                                    Icons.Default.Camera,
                                    contentDescription = stringResource(R.string.edit_profile_photo),
                                    tint = orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${uiState.name.value} ${uiState.lastName.value}".trim().ifEmpty { "Mi Perfil" },
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
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
                            text = uiState.city.value.ifEmpty { "Sin ciudad" },
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = uiState.email.value,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                EditProfileTextField(
                    label = stringResource(R.string.edit_profile_first_names),
                    value = uiState.name.value,
                    onValueChange = viewModel::onNameChange,
                    error = uiState.name.error,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                EditProfileTextField(
                    label = stringResource(R.string.edit_profile_last_names),
                    value = uiState.lastName.value,
                    onValueChange = viewModel::onLastNameChange,
                    error = uiState.lastName.error,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = cityExpanded,
                    onExpandedChange = { cityExpanded = it }
                ) {
                    OutlinedTextField(
                        value = uiState.city.value,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.edit_profile_city)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        isError = uiState.city.error != null,
                        supportingText = uiState.city.error?.let { { Text(it, color = red) } }
                    )

                    ExposedDropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false }
                    ) {
                        viewModel.cities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city) },
                                onClick = {
                                    viewModel.onCityChange(city)
                                    cityExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                EditProfileTextField(
                    label = stringResource(R.string.edit_profile_address),
                    value = uiState.address.value,
                    onValueChange = viewModel::onAddressChange,
                    error = uiState.address.error,
                    leadingIcon = Icons.Default.LocationOn
                )

                Spacer(modifier = Modifier.height(16.dp))

                EditProfileTextField(
                    label = stringResource(R.string.profile_phone),
                    value = uiState.phone.value,
                    onValueChange = viewModel::onPhoneChange,
                    error = uiState.phone.error,
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = { viewModel.onShowPasswordDialog() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.edit_profile_change_password),
                        color = blue,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onCancel(); onBackClick() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = { viewModel.onSave() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = green),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.save))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (uiState.showImagePickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onDismissImagePicker() },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_select_photo),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    bottomSheetState.hide()
                                    val hasCameraPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED

                                    if (hasCameraPermission) {
                                        val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
                                        photoUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            file
                                        )
                                        photoUri?.let { cameraLauncher.launch(it) }
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Camera,
                            contentDescription = stringResource(R.string.profile_camera),
                            tint = green,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.profile_camera),
                            fontSize = 14.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    bottomSheetState.hide()
                                    galleryLauncher.launch("image/*")
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = stringResource(R.string.profile_gallery),
                            tint = blue,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.profile_gallery),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (uiState.showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissPasswordDialog() },
            title = {
                Text(
                    text = stringResource(R.string.edit_profile_change_password),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(stringResource(R.string.profile_password_change_message))
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onDismissPasswordDialog() }) {
                    Text(stringResource(R.string.ok), color = green)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (uiState.saveSuccess) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, context.getString(R.string.profile_save_success), Toast.LENGTH_SHORT).show()
            viewModel.clearSaveSuccess()
            onBackClick()
        }
    }
}

@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        isError = error != null,
        supportingText = error?.let { { Text(it, color = red) } },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = green,
            unfocusedBorderColor = Color.Black
        )
    )
}