package com.example.proyectofinaldisenomovil.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategorySelectorBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.SearchTopBarApp
import com.example.proyectofinaldisenomovil.core.component.login.LoginHeaderSection
import com.example.proyectofinaldisenomovil.core.component.login.TopBarRegister
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.features.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.login.LoginViewModel
import com.example.proyectofinaldisenomovil.features.register.RegisterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    var query by remember { mutableStateOf("")  }

    Scaffold(
        topBar = {
            SearchTopBarApp(
                query = query,
                onQueryChange = { query = it },
                onNotificationClick = {
                }
            )
        },
        bottomBar = {
            AppBottomBar(navController = navController)
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {

            CategorySelectorBar( navController)

            Spacer( modifier = Modifier.size(7.dp))

            FiltersBar()
        }

    }
}

@Composable
fun FiltersBar(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.weight(4f),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    "Ordenar por:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,

                )
                SimpleDropdown()
            }

            Row(
                modifier = Modifier.weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                "Distancia:",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
                )

                SimpleDropdownDistance()
            }
        }
    }
}


@Composable
fun SimpleDropdown() {

    val options = listOf("Nombre", "Fecha", "Popularidad")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },

    ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
                .padding(start = 8.dp)
                .width(100.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = selectedOption,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(3f)
            )

            Icon(

                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Icono de flecha",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(25.dp)
                    .weight(1f)
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 20.dp,
                shadowElevation = 10.dp
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }

    }
}


@Composable
fun SimpleDropdownDistance() {

    val options = listOf("1Km", "5Km", "10Km" , "30Km" , "50km" , "100Km" , "+150Km")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[3]) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        contentAlignment = Alignment.Center,

        ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
                .padding(start = 8.dp)
                .width(100.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = selectedOption,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Icono de flecha",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 20.dp,
                shadowElevation = 10.dp
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.width(120.dp),
                        text = {
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                text = option,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    ProyectoFinalDisenoMovilTheme() {
        HomeScreen(navController = rememberNavController())
    }

}