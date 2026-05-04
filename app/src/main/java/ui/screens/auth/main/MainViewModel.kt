package ui.screens.auth.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import data.model.PaymentHistory
import data.model.User
import data.repository.ConnectionRepository
import data.repository.PaymentRepository
import data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.Result

data class MainUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val balance: Double = 0.0,
    val recentPayments: List<PaymentHistory> = emptyList(),
    val totalDataUsedBytes: Long = 0L,
    val isConnected: Boolean = true,
    val error: String? = null
) {
    val dataUsedFormatted: String
        get() = when {
            totalDataUsedBytes >= 1_073_741_824L ->
                "%.1f ГБ".format(totalDataUsedBytes / 1_073_741_824.0)
            totalDataUsedBytes >= 1_048_576L ->
                "%.1f МБ".format(totalDataUsedBytes / 1_048_576.0)
            totalDataUsedBytes > 0 ->
                "%.0f КБ".format(totalDataUsedBytes / 1024.0)
            else -> "— ГБ"
        }
}

class MainViewModel(
    private val userRepository: UserRepository,
    private val paymentRepository: PaymentRepository,
    private val connectionRepository: ConnectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(isLoading = true))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = MainUiState(isLoading = true)

            val userDeferred = async { userRepository.getProfile() }
            val paymentsDeferred = async { paymentRepository.getPaymentHistory() }
            val connectionDeferred = async { connectionRepository.getTotalDataUsed() }

            val userResult = userDeferred.await()
            val paymentsResult = paymentsDeferred.await()
            val connectionResult = connectionDeferred.await()

            val payments = (paymentsResult as? Result.Success)?.data ?: emptyList()
            // Balance = sum of all payments (positive = deposit, negative = charge)
            val balance = payments.sumOf { it.amount }

            _uiState.value = MainUiState(
                isLoading = false,
                user = (userResult as? Result.Success)?.data,
                balance = balance,
                recentPayments = payments.sortedByDescending { it.paymentDate }.take(5),
                totalDataUsedBytes = (connectionResult as? Result.Success)?.data ?: 0L,
                error = (userResult as? Result.Error)?.message
            )
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = TisDialogApp.instance
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(
                    app.userRepository,
                    app.paymentRepository,
                    app.connectionRepository
                ) as T
            }
        }
    }
}
