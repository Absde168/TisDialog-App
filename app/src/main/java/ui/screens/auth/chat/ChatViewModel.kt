package ui.screens.auth.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.model.ChatMessage
import data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val error: String? = null
)

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(isLoading = true))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            when (val result = chatRepository.getMessages()) {
                is Result.Success -> _uiState.value = ChatUiState(messages = result.data)
                is Result.Error -> _uiState.value = ChatUiState(
                    messages = defaultWelcomeMessages(),
                    error = null
                )
                is Result.Loading -> Unit
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            when (val result = chatRepository.sendMessage(text)) {
                is Result.Success -> {
                    val updated = _uiState.value.messages + result.data
                    _uiState.value = _uiState.value.copy(messages = updated, isSending = false)
                }
                is Result.Error -> {
                    // Optimistically add message locally even if API fails
                    val local = ChatMessage(
                        userId = 0,
                        content = text,
                        senderType = "User",
                        timestamp = java.time.Instant.now().toString()
                    )
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + local,
                        isSending = false,
                        error = result.message
                    )
                }
                is Result.Loading -> Unit
            }
        }
    }

    private fun defaultWelcomeMessages() = listOf(
        ChatMessage(userId = 0, content = "Здравствуйте! Чем могу помочь?", senderType = "Support", timestamp = "")
    )

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(TisDialogApp.instance.chatRepository) as T
            }
        }
    }
}
