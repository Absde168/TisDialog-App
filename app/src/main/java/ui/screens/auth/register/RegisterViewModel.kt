package ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        login: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            when (val result = authRepository.register(login, password, firstName, lastName, email, phone)) {
                is Result.Success -> _uiState.value = RegisterUiState(isSuccess = true)
                is Result.Error   -> _uiState.value = RegisterUiState(error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(TisDialogApp.instance.authRepository) as T
            }
        }
    }
}
