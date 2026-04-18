package com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword

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

data class CodeDigitField(
    val value: String = "",
    val isError: Boolean = false
)

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var email: String = ""
    private var expectedCode: String = ""

    private val _codeDigits = MutableStateFlow(List(5) { CodeDigitField() })
    val codeDigits: StateFlow<List<CodeDigitField>> = _codeDigits.asStateFlow()

    private val _newPasswordField = MutableStateFlow(ValidatedField())
    val newPasswordField: StateFlow<ValidatedField> = _newPasswordField.asStateFlow()

    private val _confirmPasswordField = MutableStateFlow(ValidatedField())
    val confirmPasswordField: StateFlow<ValidatedField> = _confirmPasswordField.asStateFlow()

    private val _requestResult = MutableStateFlow<RequestResult?>(null)
    val requestResult: StateFlow<RequestResult?> = _requestResult.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

    private val _isConfirmPasswordVisible = MutableStateFlow(false)
    val isConfirmPasswordVisible: StateFlow<Boolean> = _isConfirmPasswordVisible.asStateFlow()

    fun initialize(email: String, code: String) {
        this.email = email
        this.expectedCode = code
    }

    fun onDigitChange(index: Int, value: String) {
        if (value.length > 1) return
        
        val digitOnly = value.filter { it.isDigit() }
        val newDigits = _codeDigits.value.toMutableList()
        newDigits[index] = CodeDigitField(value = digitOnly, isError = false)
        _codeDigits.value = newDigits

        if (digitOnly.isNotEmpty() && index < 4) {
        }
    }

    fun onDigitFocusChange(index: Int) {
        val currentValue = _codeDigits.value[index].value
        if (currentValue.isEmpty()) {
            val newDigits = _codeDigits.value.toMutableList()
            newDigits[index] = CodeDigitField(value = "", isError = true)
            _codeDigits.value = newDigits
        }
    }

    fun getFullCode(): String {
        return _codeDigits.value.joinToString("") { it.value }
    }

    fun isCodeComplete(): Boolean {
        return _codeDigits.value.all { it.value.isNotEmpty() }
    }

    fun onNewPasswordChange(password: String) {
        val errorKey = Validators.validatePassword(password)
        val errorMessage = when (errorKey) {
            "validation_password_short" -> resourceProvider.getString(R.string.validation_password_short)
            else -> null
        }
        _newPasswordField.value = ValidatedField(
            value = password,
            error = errorMessage,
            isValid = errorKey == null && password.isNotEmpty()
        )
        _requestResult.value = null
        
        if (_confirmPasswordField.value.value.isNotEmpty()) {
            validatePasswordsMatch()
        }
    }

    fun onConfirmPasswordChange(password: String) {
        val error = if (password.isNotEmpty() && password != _newPasswordField.value.value) {
            resourceProvider.getString(R.string.validation_passwords_not_match)
        } else null
        
        _confirmPasswordField.value = ValidatedField(
            value = password,
            error = error,
            isValid = error == null && password.isNotEmpty()
        )
        _requestResult.value = null
    }

    private fun validatePasswordsMatch(): Boolean {
        val confirmPassword = _confirmPasswordField.value.value
        val newPassword = _newPasswordField.value.value
        
        if (confirmPassword.isNotEmpty()) {
            val error = if (confirmPassword != newPassword) {
                resourceProvider.getString(R.string.validation_passwords_not_match)
            } else null
            
            _confirmPasswordField.value = _confirmPasswordField.value.copy(
                error = error,
                isValid = error == null
            )
            return error == null
        }
        return true
    }

    fun isFormValid(): Boolean {
        return isCodeComplete() && 
               _newPasswordField.value.isValid && 
               _confirmPasswordField.value.isValid &&
               _newPasswordField.value.value == _confirmPasswordField.value.value
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = !_isConfirmPasswordVisible.value
    }

    fun resetPassword() {
        if (!isFormValid()) return

        val enteredCode = getFullCode()
        if (enteredCode != expectedCode) {
            _requestResult.value = RequestResult.Failure(
                resourceProvider.getString(R.string.recover_password_invalid_code)
            )
            return
        }

        _requestResult.value = RequestResult.Loading

        viewModelScope.launch {
            try {
                val success = userRepository.resetPassword(email, _newPasswordField.value.value)
                if (success) {
                    _requestResult.value = RequestResult.Success(
                        resourceProvider.getString(R.string.recover_password_success)
                    )
                } else {
                    _requestResult.value = RequestResult.Failure(
                        resourceProvider.getString(R.string.error_unknown)
                    )
                }
            } catch (e: Exception) {
                _requestResult.value = RequestResult.Failure(
                    resourceProvider.getString(R.string.error_unknown)
                )
            }
        }
    }

    fun resetState() {
        _requestResult.value = null
    }
}
