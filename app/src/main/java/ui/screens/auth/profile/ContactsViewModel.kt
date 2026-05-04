package ui.screens.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result
import util.TokenManager

data class ContactsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val error: String? = null,
    val saveSuccess: Boolean = false
)

class ContactsViewModel(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState(isLoading = true))
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ContactsUiState(isLoading = true)
            when (val result = userRepository.getProfile()) {
                is Result.Success -> {
                    val user = result.data
                    _uiState.value = ContactsUiState(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        phone = user.phone,
                        email = user.email
                    )
                }
                is Result.Error -> _uiState.value = ContactsUiState(error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    fun onFirstNameChange(v: String) { _uiState.value = _uiState.value.copy(firstName = v, saveSuccess = false) }
    fun onLastNameChange(v: String) { _uiState.value = _uiState.value.copy(lastName = v, saveSuccess = false) }
    fun onPhoneChange(v: String) { _uiState.value = _uiState.value.copy(phone = v, saveSuccess = false) }
    fun onEmailChange(v: String) { _uiState.value = _uiState.value.copy(email = v, saveSuccess = false) }

    fun save() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId() ?: return@launch
            val s = _uiState.value
            _uiState.value = s.copy(isSaving = true, error = null)
            when (val result = userRepository.updateProfile(userId, s.firstName, s.lastName, s.phone, s.email)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
                is Result.Error -> _uiState.value = _uiState.value.copy(isSaving = false, error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = TisDialogApp.instance
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(app.userRepository, app.tokenManager) as T
            }
        }
    }
}
