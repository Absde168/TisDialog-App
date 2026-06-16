package ui.screens.auth.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.model.PaymentHistory
import data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class PaymentUiState(
    val isLoading: Boolean = false,
    val payments: List<PaymentHistory> = emptyList(),
    val error: String? = null
)

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState(isLoading = true))
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        loadPayments()
    }

    fun loadPayments() {
        viewModelScope.launch {
            _uiState.value = PaymentUiState(isLoading = true)
            when (val result = paymentRepository.getPaymentHistory()) {
                is Result.Success -> _uiState.value = PaymentUiState(
                    payments = result.data.sortedByDescending { it.paymentDate }
                )
                is Result.Error -> _uiState.value = PaymentUiState(error = result.message)
                is Result.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PaymentViewModel(TisDialogApp.instance.paymentRepository) as T
            }
        }
    }
}
