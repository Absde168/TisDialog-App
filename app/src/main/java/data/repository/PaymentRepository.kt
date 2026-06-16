package data.repository

import data.model.PaymentHistory
import data.network.ApiService
import util.Result
import util.TokenManager
import util.safeApiCall

class PaymentRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getPaymentHistory(): Result<List<PaymentHistory>> =
        safeApiCall { api.getPaymentHistory() }

    suspend fun createPayment(amount: Double, method: String): Result<PaymentHistory> {
        val userId = tokenManager.getUserId() ?: 0
        return safeApiCall {
            api.createPayment(
                PaymentHistory(
                    userId = userId,
                    amount = amount,
                    paymentDate = java.time.Instant.now().toString(),
                    paymentMethod = method
                )
            )
        }
    }
}
