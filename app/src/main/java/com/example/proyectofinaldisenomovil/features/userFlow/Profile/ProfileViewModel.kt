package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.core.utils.ValidatedField
import com.example.proyectofinaldisenomovil.data.local.SettingsDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.User.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String?,
    val location: String?,
    val level: Int?,
    val levelName: String?,
    val points: Int?,
    val pointsToNextLevel: Int?,
    val activeEvents: Int?,
    val completedEvents: Int?,
    val pendingEvents: Int?,
    val rating: Double?,
    val isLoading: Boolean?
)

data class EditProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val photoUri: Uri? = null,
    val name: ValidatedField = ValidatedField(),
    val lastName: ValidatedField = ValidatedField(),
    val email: ValidatedField = ValidatedField(),
    val city: ValidatedField = ValidatedField(),
    val address: ValidatedField = ValidatedField(),
    val phone: ValidatedField = ValidatedField(),
    val showPasswordDialog: Boolean = false,
    val showImagePickerSheet: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val settingsDataStore: SettingsDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(
        name = null,
        location = null,
        level = null,
        levelName = null,
        points = null,
        pointsToNextLevel = null,
        activeEvents = null,
        completedEvents = null,
        pendingEvents = null,
        rating = null,
        isLoading = true
    ))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _editUiState = MutableStateFlow(EditProfileUiState())
    val editUiState: StateFlow<EditProfileUiState> = _editUiState.asStateFlow()

    private val _saveProfileResult = MutableStateFlow<RequestResult?>(null)
    val saveProfileResult: StateFlow<RequestResult?> = _saveProfileResult.asStateFlow()

    val currentLanguage: StateFlow<String> = settingsDataStore.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsDataStore.DEFAULT_LANGUAGE
        )

    val cities = listOf(
        "Armenia, Quindío",
        "Montenegro, Quindío",
        "Pereira, Risaralda",
        "Manizales, Caldas",
        "Cali, Valle del Cauca",
        "Bogotá, Cundinamarca",
        "Medellín, Antioquia",
        "Barranquilla, Atlántico"
    )

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            delay(500)

            val loggedInUser = MockDataRepository.getLoggedInUser()
            val user = loggedInUser?.let { userRepository.getUserById(it.uid) }
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    name = user.fullName,
                    location = user.city,
                    level = 1,
                    levelName = user.level.toString(),
                    points = user.reputationPoints,
                    pointsToNextLevel = user.pointsToNextLevel() ?: 0,
                    activeEvents = 10,
                    completedEvents = 13,
                    pendingEvents = 20,
                    rating = user.rating,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _editUiState.update { it.copy(isLoading = true) }
            val currentUser = MockDataRepository.getLoggedInUser()
            if (currentUser != null) {
                val user = userRepository.getUserById(currentUser.uid)
                if (user != null) {
                    _editUiState.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            name = ValidatedField(value = user.firstName, isValid = true),
                            lastName = ValidatedField(value = user.lastName, isValid = true),
                            email = ValidatedField(value = user.email, isValid = true),
                            city = ValidatedField(value = user.city, isValid = true),
                            address = ValidatedField(value = user.location?.let { loc -> "Lat: ${loc.latitude}, Lon: ${loc.longitude}" } ?: ""),
                            phone = ValidatedField(value = ""),
                            photoUri = user.profileImageUrl?.let { Uri.parse(it) }
                        )
                    }
                }
            }
        }
    }

    fun onEditModeToggle() {
        _editUiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun onNameChange(value: String) {
        val error = if (value.isBlank()) resourceProvider.getString(R.string.validation_name_required) else null
        _editUiState.update {
            it.copy(name = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onLastNameChange(value: String) {
        val error = if (value.isBlank()) resourceProvider.getString(R.string.validation_lastname_required) else null
        _editUiState.update {
            it.copy(lastName = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onCityChange(value: String) {
        val error = if (value.isBlank()) resourceProvider.getString(R.string.validation_city_required) else null
        _editUiState.update {
            it.copy(city = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onAddressChange(value: String) {
        _editUiState.update {
            it.copy(address = ValidatedField(value = value, isValid = true))
        }
    }

    fun onPhoneChange(value: String) {
        val error = if (value.isNotBlank() && !value.all { it.isDigit() || it == '-' || it == ' ' }) {
            resourceProvider.getString(R.string.validation_phone_invalid)
        } else null
        _editUiState.update {
            it.copy(phone = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onShowImagePicker() {
        _editUiState.update { it.copy(showImagePickerSheet = true) }
    }

    fun onDismissImagePicker() {
        _editUiState.update { it.copy(showImagePickerSheet = false) }
    }

    fun onPhotoSelected(uri: Uri) {
        _editUiState.update { it.copy(photoUri = uri, showImagePickerSheet = false) }
    }

    fun onShowPasswordDialog() {
        _editUiState.update { it.copy(showPasswordDialog = true) }
    }

    fun onDismissPasswordDialog() {
        _editUiState.update { it.copy(showPasswordDialog = false) }
    }

    fun onSave() {
        val state = _editUiState.value
        val nameValid = state.name.value.isNotBlank()
        val lastNameValid = state.lastName.value.isNotBlank()
        val cityValid = state.city.value.isNotBlank()

        if (!nameValid || !lastNameValid || !cityValid) {
            _editUiState.update {
                it.copy(
                    name = it.name.copy(error = if (!nameValid) resourceProvider.getString(R.string.validation_name_required) else null, isValid = nameValid),
                    lastName = it.lastName.copy(error = if (!lastNameValid) resourceProvider.getString(R.string.validation_lastname_required) else null, isValid = lastNameValid),
                    city = it.city.copy(error = if (!cityValid) resourceProvider.getString(R.string.validation_city_required) else null, isValid = cityValid)
                )
            }
            return
        }

        viewModelScope.launch {
            state.user?.let { currentUser ->
                val updatedUser = currentUser.copy(
                    firstName = state.name.value,
                    lastName = state.lastName.value,
                    city = state.city.value,
                    profileImageUrl = state.photoUri?.toString() ?: currentUser.profileImageUrl
                )
                userRepository.updateUser(updatedUser)
                MockDataRepository.setLoggedInUser(updatedUser)
                _editUiState.update {
                    it.copy(user = updatedUser, isEditMode = false, saveSuccess = true)
                }
                _saveProfileResult.value = RequestResult.Success(resourceProvider.getString(R.string.profile_update_success))
                loadUserProfile()
            }
        }
    }

    fun onCancel() {
        val user = _editUiState.value.user
        _editUiState.update {
            it.copy(
                isEditMode = false,
                name = ValidatedField(value = user?.firstName ?: "", isValid = true),
                lastName = ValidatedField(value = user?.lastName ?: "", isValid = true),
                city = ValidatedField(value = user?.city ?: "", isValid = true),
                address = ValidatedField(value = user?.location?.let { loc -> "Lat: ${loc.latitude}, Lon: ${loc.longitude}" } ?: ""),
                photoUri = user?.profileImageUrl?.let { Uri.parse(it) }
            )
        }
    }

    fun resetSaveProfileResult() {
        _saveProfileResult.value = null
    }

    fun clearSaveSuccess() {
        _editUiState.update { it.copy(saveSuccess = false) }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(languageCode)
        }
    }

    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "es" -> resourceProvider.getString(R.string.language_spanish)
            "en" -> resourceProvider.getString(R.string.language_english)
            else -> languageCode
        }
    }
}