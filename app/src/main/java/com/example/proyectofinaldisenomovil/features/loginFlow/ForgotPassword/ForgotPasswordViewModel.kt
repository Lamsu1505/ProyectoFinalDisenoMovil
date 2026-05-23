package com.example.proyectofinaldisenomovil.features.loginFlow.ForgotPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.core.utils.ValidatedField
import com.example.proyectofinaldisenomovil.core.utils.Validators
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _emailField = MutableStateFlow(ValidatedField())
    val emailField: StateFlow<ValidatedField> = _emailField.asStateFlow()

    private val _requestResult = MutableStateFlow<RequestResult?>(null)
    val requestResult: StateFlow<RequestResult?> = _requestResult.asStateFlow()

    fun onEmailChange(newEmail: String) {
        val errorKey = Validators.validateEmail(newEmail)
        val errorMessage = when (errorKey) {
            "validation_email_invalid" -> resourceProvider.getString(R.string.validation_email_invalid)
            else -> null
        }
        _emailField.value = ValidatedField(
            value = newEmail,
            error = errorMessage,
            isValid = errorKey == null && newEmail.isNotEmpty()
        )
        _requestResult.value = null
    }

    fun isFormValid(): Boolean {
        return _emailField.value.isValid
    }

    fun sendRecoveryCode() {
        val email = _emailField.value.value
        Log.i("Recovery code", "El email es " + email)

        if (!isFormValid()) return

        _requestResult.value = RequestResult.Loading
        viewModelScope.launch {
            val success = userRepository.resetPassword(email)
            if (success) {
                _requestResult.value = RequestResult.Success(resourceProvider.getString(R.string.success))
            } else {
                _requestResult.value = RequestResult.Failure(resourceProvider.getString(R.string.error_unknown))
            }
        }
    }

    fun resetState() {
        _requestResult.value = null
    }
}
