package ui.screens.auth.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.model.ChatMessage
import data.repository.ChatRepository
import data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val error: String? = null,
    val userName: String = ""
)

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(isLoading = true))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
        loadUserName()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            when (val result = userRepository.getProfile()) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    userName = result.data.firstName
                )
                else -> Unit
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            when (val result = chatRepository.getMessages()) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    messages = result.data
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    messages = emptyList()
                )
                is Result.Loading -> Unit
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true, error = null)
            when (val result = chatRepository.sendMessage(text)) {
                is Result.Success -> {
                    // result.data — список [userMessage, botMessage]
                    val updated = _uiState.value.messages + result.data
                    _uiState.value = _uiState.value.copy(
                        messages = updated,
                        isSending = false
                    )
                }
                is Result.Error -> {
                    // Офлайн-fallback: показываем сообщение пользователя локально
                    val local = ChatMessage(
                        userId = 0,
                        content = text,
                        senderType = "User",
                        timestamp = java.time.Instant.now().toString()
                    )
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + local,
                        isSending = false,
                        error = "Нет соединения с сервером"
                    )
                }
                is Result.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(
                    TisDialogApp.instance.chatRepository,
                    TisDialogApp.instance.userRepository
                ) as T
            }
        }
    }
}
