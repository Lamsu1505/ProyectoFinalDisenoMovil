package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBarApp(
    query: String,
    onQueryChange: (String) -> Unit,
    onNotificationsClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            fontSize = 18.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        },

        actions = {
            BadgedBox(
                modifier = Modifier
                    .clickable(onClick = {
                    }),
                badge = {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ) {
                        //TODO cambiar a cantidad real
                        Text("3", fontSize = 11.sp)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.notifications_description),
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                        .clickable(
                            onClick = {
                                onNotificationsClick()
                            }
                        )
                )

            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchTopBar() {
    ProyectoFinalDisenoMovilTheme() {
        SearchTopBarApp(
            query = "",
            onQueryChange = {}
        )
    }
}