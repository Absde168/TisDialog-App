package ui.screens.auth.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class TopUpUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class TopUpViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TopUpUiState())
    val uiState: StateFlow<TopUpUiState> = _uiState.asStateFlow()

    fun pay(amount: String, method: String) {
        val amountDouble = amount.toDoubleOrNull()
        if (amountDouble == null || amountDouble <= 0) {
            _uiState.value = TopUpUiState(error = "Введите корректную сумму")
            return
        }
        viewModelScope.launch {
            _uiState.value = TopUpUiState(isLoading = true)
            when (val result = paymentRepository.createPayment(amountDouble, mapMethod(method))) {
                is Result.Success -> _uiState.value = TopUpUiState(isSuccess = true)
                is Result.Error -> _uiState.value = TopUpUiState(error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetSuccess() {
        _uiState.value = TopUpUiState()
    }

    private fun mapMethod(key: String) = when (key) {
        "sbp" -> "SBP"
        "account" -> "Account"
        else -> "Card"
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TopUpViewModel(TisDialogApp.instance.paymentRepository) as T
            }
        }
    }
}
