package com.example.proyectofinaldisenomovil.features.userFlow.Profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.core.utils.ValidatedField
import com.example.proyectofinaldisenomovil.data.repository.Memory.UserRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.User.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
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
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = userRepository.getUserById(userId)
            if (user != null) {
                _uiState.update {
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
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "User not found") }
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = MockDataRepository.getLoggedInUser()
            if (currentUser != null) {
                val user = userRepository.getUserById(currentUser.uid)
                if (user != null) {
                    _uiState.update {
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
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "User not found") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "No user logged in") }
            }
        }
    }

    fun onEditModeToggle() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun onNameChange(value: String) {
        val error = if (value.isBlank()) "Name is required" else null
        _uiState.update {
            it.copy(name = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onLastNameChange(value: String) {
        val error = if (value.isBlank()) "Last name is required" else null
        _uiState.update {
            it.copy(lastName = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onCityChange(value: String) {
        val error = if (value.isBlank()) "City is required" else null
        _uiState.update {
            it.copy(city = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onAddressChange(value: String) {
        _uiState.update {
            it.copy(address = ValidatedField(value = value, isValid = true))
        }
    }

    fun onPhoneChange(value: String) {
        val error = if (value.isNotBlank() && !value.all { it.isDigit() || it == '-' || it == ' ' }) {
            "Invalid phone format"
        } else null
        _uiState.update {
            it.copy(phone = ValidatedField(value = value, error = error, isValid = error == null))
        }
    }

    fun onShowImagePicker() {
        _uiState.update { it.copy(showImagePickerSheet = true) }
    }

    fun onDismissImagePicker() {
        _uiState.update { it.copy(showImagePickerSheet = false) }
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri, showImagePickerSheet = false) }
    }

    fun onShowPasswordDialog() {
        _uiState.update { it.copy(showPasswordDialog = true) }
    }

    fun onDismissPasswordDialog() {
        _uiState.update { it.copy(showPasswordDialog = false) }
    }

    fun onSave() {
        val state = _uiState.value
        val nameValid = state.name.value.isNotBlank()
        val lastNameValid = state.lastName.value.isNotBlank()
        val cityValid = state.city.value.isNotBlank()

        if (!nameValid || !lastNameValid || !cityValid) {
            _uiState.update {
                it.copy(
                    name = it.name.copy(error = if (!nameValid) "Name required" else null, isValid = nameValid),
                    lastName = it.lastName.copy(error = if (!lastNameValid) "Last name required" else null, isValid = lastNameValid),
                    city = it.city.copy(error = if (!cityValid) "City required" else null, isValid = cityValid)
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
                _uiState.update {
                    it.copy(
                        user = updatedUser,
                        isEditMode = false,
                        saveSuccess = true
                    )
                }
            }
        }
    }

    fun onCancel() {
        val user = _uiState.value.user
        _uiState.update {
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

    fun clearSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}