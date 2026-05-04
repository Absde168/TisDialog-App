package ui.screens.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.model.SystemNotification
import data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<SystemNotification> = emptyList(),
    val error: String? = null
)

class NotificationViewModel(private val notificationRepository: NotificationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState(isLoading = true))
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            when (val result = notificationRepository.getNotifications()) {
                is Result.Success -> _uiState.value = NotificationUiState(notifications = result.data)
                is Result.Error -> _uiState.value = NotificationUiState(error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(TisDialogApp.instance.notificationRepository) as T
            }
        }
    }
}
