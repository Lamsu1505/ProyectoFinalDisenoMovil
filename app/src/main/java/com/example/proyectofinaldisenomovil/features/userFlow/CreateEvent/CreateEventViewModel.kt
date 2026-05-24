package com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.Remote.CloudinaryRepository
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.R
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

sealed class CreateEventResult {
    data object Idle : CreateEventResult()
    data object Loading : CreateEventResult()
    data object Success : CreateEventResult()
    data class Error(val message: String) : CreateEventResult()
}

data class CreateEventUiState(
    val idEvent: String? = null,
    val title: String = "",
    val description: String = "",
    val category: EventCategory = EventCategory.SOCIAL,
    val capacity: String = "",
    val images: List<Uri> = emptyList(),
    val address: String = "",
    val pointerAddres: Location = Location(0.0, 0.0),
    val startDate: String="",
    val startTime: String = "",
    val endDate: String = "",
    val endTime: String = "",
    val titleError: String = "",
    val descriptionError: String = "",
    val addressError: String = "",
    val dateError: String = "",
    val isFormValid: Boolean = false,
    val isSuggestingCategory: Boolean = false,
    val categoryError: String = ""
)

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val cloudinaryRepository: CloudinaryRepository,
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _createResult = MutableStateFlow<CreateEventResult>(CreateEventResult.Idle)
    val createResult: StateFlow<CreateEventResult> = _createResult.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale("es", "CO"))
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    private fun checkFormValidity(state: CreateEventUiState): Boolean {
        return state.title.length >= 1 &&
                state.description.length >= 1 &&
                state.address.length >= 1 &&
                state.startDate.toString().isNotEmpty()
    }

    private fun updateState(update: (CreateEventUiState) -> CreateEventUiState) {
        _uiState.update { currentState ->
            val newState = update(currentState)
            newState.copy(isFormValid = checkFormValidity(newState))
        }
    }

    fun onTitleChange(newTitle: String) {
        updateState { 
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isNotEmpty() && newTitle.length < 5) resourceProvider.getString(R.string.create_event_error_title_short) else ""
            ) 
        }
    }

    fun onDescriptionChange(newDescription: String) {
        updateState { 
            it.copy(
                description = newDescription,
                descriptionError = if (newDescription.isNotEmpty() && newDescription.length < 20) resourceProvider.getString(R.string.create_event_error_desc_short) else ""
            ) 
        }
    }

    fun onCategoryChange(newCategory: EventCategory) {
        updateState { it.copy(category = newCategory) }
    }

    fun onCapacityChange(newCapacity: String) {
        updateState { it.copy(capacity = newCapacity.filter { c -> c.isDigit() }) }
    }

    fun onAddressChange(newAddress: String) {
        updateState { 
            it.copy(
                address = newAddress,
                addressError = if (newAddress.isNotEmpty() && newAddress.length < 10) resourceProvider.getString(R.string.create_event_error_address_short) else ""
            ) 
        }
    }

    fun onPointerAddressChange(newAddress: Location) {
        updateState {
            it.copy(
                pointerAddres = newAddress,
            )
        }
    }

    fun addImage(uri: Uri) {
        if (_uiState.value.images.size < 5) {
            updateState { it.copy(images = it.images + uri) }
        }
    }

    fun removeImage(uri: Uri) {
        updateState { it.copy(images = it.images - uri) }
    }

    fun onStartDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            updateState { state ->
                state.copy(
                    startDate = dateString,
                    startTime = timeString,
                    dateError = ""
                )
            }
        }
    }

    fun onEndDateChange(millis: Long?) {
        millis?.let {
            val dateString = dateFormatter.format(Date(it))
            val timeString = timeFormatter.format(Date(it))
            updateState { state -> 
                state.copy(
                    endDate = dateString,
                    endTime = timeString,
                    dateError = ""
                ) 
            }
        }
    }

    fun createEvent() {

        val state = _uiState.value

        if (!state.isFormValid) {

            val missingFields = mutableListOf<String>()

            if (state.title.length < 1)
                missingFields.add(resourceProvider.getString(R.string.create_event_name))

            if (state.description.length < 1)
                missingFields.add(resourceProvider.getString(R.string.create_event_description))

            if (state.address.length < 1)
                missingFields.add(resourceProvider.getString(R.string.create_event_address))

            if (state.startDate.toString().isEmpty())
                missingFields.add(resourceProvider.getString(R.string.create_event_date))

            _createResult.value = CreateEventResult.Error(
                resourceProvider.getString(R.string.create_event_missing_fields, missingFields.joinToString(", "))
            )

            return
        }

        viewModelScope.launch {

            _createResult.value = CreateEventResult.Loading

            try {
                val capacity =
                    state.capacity.replace(".", "").toIntOrNull()

                val uploadedImageUrls =
                    if (state.images.isNotEmpty()) {
                        state.images.map { imageUri ->
                            cloudinaryRepository.uploadImage(imageUri)
                        }

                    } else {

                        listOf(
                            "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800"
                        )
                    }

                val dateTimestamp = state.startDate.toTimeStamp()

                val location = Location(
                    state.pointerAddres.latitude ,
                    state.pointerAddres.longitude
                )

                eventRepository.createEvent(
                        title = state.title.trim(),
                        description = state.description.trim(),
                        category = state.category,
                        address = state.address.trim(),
                        location = location,
                        imageUrls = uploadedImageUrls,
                        startDate = dateTimestamp,
                        endDate = Timestamp(
                            Date(
                                System.currentTimeMillis() +
                                        24 * 60 * 60 * 1000
                            )
                        ),
                        maxAttendees = capacity
                    )

                _createResult.value = CreateEventResult.Success

            } catch (e: Exception) {
                _createResult.value = CreateEventResult.Error(
                    resourceProvider.getString(R.string.create_event_error) + ": ${e.message}"
                )
            }
        }
    }


    public fun Long.toDateString(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(this))
    }
    public fun Long.toTimestamp(): Timestamp {
        return Timestamp(Date(this))
    }

    public fun String.toTimeStamp(): Timestamp {
        val formats = listOf(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
            SimpleDateFormat("d 'de' MMMM 'del' yyyy", Locale("es")),
            SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es")) 
        )

        for (sdf in formats) {
            try {
                sdf.isLenient = false
                val date = sdf.parse(this)
                if (date != null) return Timestamp(date)
            } catch (e: Exception) {
            }
        }

        throw IllegalArgumentException("Unparseable date: \"$this\"")
    }

    fun resetResult() {
        _createResult.value = CreateEventResult.Idle
    }

    fun clearForm() {
        _uiState.value = CreateEventUiState()
        _createResult.value = CreateEventResult.Idle
    }


    fun suggestCategory() {
        val state = _uiState.value
        if (state.title.isBlank() || state.description.isBlank()) return

        viewModelScope.launch {
            updateState { it.copy(isSuggestingCategory = true) }

            try {
                val responseText = withContext(Dispatchers.IO) {
                    val client = okhttp3.OkHttpClient()

                    val prompt = """
                    Dado este evento:
                    Título: "${state.title}"
                    Descripción: "${state.description}"

                    Clasifícalo en UNA de estas categorías exactas:
                    DEPORTES, CULTURA, SOCIAL, VOLUNTARIADO, DIVERSION, OTRO

                    Responde ÚNICAMENTE con una de estas palabras, SIN tildes, SIN puntuación.
                """.trimIndent()

                    val json = org.json.JSONObject().apply {
                        put("model", "gpt-4o-mini")
                        put("messages", org.json.JSONArray().apply {
                            put(org.json.JSONObject().apply {
                                put("role", "user")
                                put("content", prompt)
                            })
                        })
                        put("max_tokens", 10)
                        put("temperature", 0)
                    }

                    val request = okhttp3.Request.Builder()
                        .url("https://api.openai.com/v1/chat/completions")
                        .addHeader("Authorization", "Bearer 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")
                        .addHeader("Content-Type", "application/json")
                        .post(okhttp3.RequestBody.create(
                            "application/json".toMediaTypeOrNull(),
                            json.toString()
                        ))
                        .build()

                    val response = client.newCall(request).execute()
                    val body = response.body?.string() ?: ""
                    Log.d("OpenAIRaw", "Codigo: ${response.code}")
                    Log.d("OpenAIRaw", "Body: $body")
                    body
                }

                val text = org.json.JSONObject(responseText)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()
                    .uppercase()
                    .replace("\n", "")
                    .replace(".", "")

                Log.d("OpenAIResponse", "Texto: $text")

                val suggested = EventCategory.entries.find { it.name == text }
                if (suggested != null) {
                    updateState { it.copy(category = suggested, isSuggestingCategory = false) }
                } else {
                    updateState { it.copy(isSuggestingCategory = false) }
                }

            } catch (e: Exception) {
                Log.e("OpenAISuggest", "Error: ${e.message}")
                updateState { it.copy(isSuggestingCategory = false) }
            }
        }
    }
}
