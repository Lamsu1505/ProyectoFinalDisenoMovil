package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.data.model.Event.EventCategory


@Composable
fun CategorySelectorBar(
    navController: NavController
) {

    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<EventCategory?>(null) }

    val categories = EventCategory.values().toList()

    if(categories.size > 3){
        CategorySelectorNotExpanded(
            categories = categories.take(3),
            selected = selected
        )
    }



}

@Composable
fun CategorySelectorExpanded(){

}


@Composable
fun CategorySelectorNotExpanded(
    categories: List<EventCategory>,
    selected: EventCategory?
) {

    val visibleCategories =
        if (categories.size > 3) categories.take(3)
        else categories

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        visibleCategories.forEach { category ->

            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == category)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,

                    contentColor = if (selected == category)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = category.label,
                    maxLines = 1
                )
            }
        }

        if (categories.size > 3) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Mas categorias",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategorySelect(){

    ProyectoFinalDisenoMovilTheme() {
        CategorySelectorBar( navController = rememberNavController())
    }
}