package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.R
import java.util.UUID

data class CommentUiModel(
    val id: String,
    val authorName: String,
    val initials: String,
    val timeAgo: String,
    val text: String
)

data class ViewEventUiState(
    val title: String = "Partido de la paz (Real Madrid vs Universidad del Quindío)",
    val category: String = "Deportes",
    val date: String = "Jueves 28 de febrero",
    val time: String = "6:00 pm",
    val location: String = "Estadio centenario Armenia.",
    val distance: String = "2 km",
    val likes: Int = 200,
    val currentAttendees: Int = 10500,
    val maxAttendees: Int = 30000,
    val creatorName: String = "AndresZuniga0fdefd fsf fd5",
    val description: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing",
    val imageRes: Int = R.mipmap.fut_img,
    val totalImages: Int = 8,
    val currentImageIndex: Int = 1,
    val comments: List<CommentUiModel> = listOf(
        CommentUiModel(
            id = "1",
            authorName = "Andres Perez",
            initials = "AP",
            timeAgo = "Ahora",
            text = "Hola quiero saber precio y lugar. Muchas gracias. Nos vemos allí."
        ),
        CommentUiModel(
            id = "2",
            authorName = "Natalia Tejada",
            initials = "NT",
            timeAgo = "Hace 1h",
            text = "Es un evento no apto para mascotas, es muy lamentableeee. s un evento no apto para mascotas, es muy lamentableeee. s un evento no apto para mascotas, es muy lamentableeee. s un evento no apto para mascotas, es muy lamentableeee. s un evento no apto para mascotas, es muy lamentableeee. s un evento no apto para mascotas, es muy lamentableeee."
        ),
        CommentUiModel(
            id = "3",
            authorName = "Santiago Londoño",
            initials = "SL",
            timeAgo = "Hace 2h",
            text = "Soy londoño."
        )
    )
)

class ViewEventViewModel : ViewModel() {

    var uiState by mutableStateOf(ViewEventUiState())
        private set

    var isInterested by mutableStateOf(false)
        private set

    var isConfirmed by mutableStateOf(false)
        private set

    // UI state for commenting
    var isAddingComment by mutableStateOf(false)
        private set

    var newCommentText by mutableStateOf("")
        private set

    fun toggleInterested() {
        isInterested = !isInterested
    }

    fun toggleConfirmed() {
        isConfirmed = !isConfirmed
    }

    fun startAddingComment() {
        isAddingComment = true
    }

    fun onCommentTextChange(text: String) {
        if (text.length <= 500) {
            newCommentText = text
        }
    }

    fun cancelAddingComment() {
        isAddingComment = false
        newCommentText = ""
    }

    fun sendComment() {
        if (newCommentText.isNotBlank()) {
            val newComment = CommentUiModel(
                id = UUID.randomUUID().toString(),
                authorName = "Tú", // In real app, get from user profile
                initials = "YO",
                timeAgo = "Ahora",
                text = newCommentText
            )
            uiState = uiState.copy(
                comments = uiState.comments + newComment
            )
            cancelAddingComment()
        }
    }
}
