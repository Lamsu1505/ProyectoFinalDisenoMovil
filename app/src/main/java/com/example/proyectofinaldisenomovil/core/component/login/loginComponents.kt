package com.example.proyectofinaldisenomovil.core.component.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinaldisenomovil.R

@Composable
fun LoginHeaderSection(
    onLanguageClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.40f)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onLanguageClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Cambiar idioma",
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "Logo de vive tu zona",
                modifier = Modifier.size(200.dp)
            )


            Text(
                text = "ViveTuZona",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}


@Composable
fun HeaderSectionNonLogued(
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.20f)
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Row(
            modifier = Modifier.align(Alignment.Center).
            fillMaxWidth().
            padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "Logo de vive tu zona",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = "ViveTuZona",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarRegister(
    onBackClick: () -> Unit = {}
) {

    TopAppBar(
        title = {
            Text(
                text = "Volver",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.clickable(onClick = {
                    onBackClick()
                })
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Flecha de Volver",
                    modifier = Modifier.size(35.dp)

                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

