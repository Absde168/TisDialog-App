package data.repository

import android.util.Base64
import data.model.LoginRequest
import data.model.RegisterRequest
import data.network.ApiService
import org.json.JSONObject
import util.Result
import util.TokenManager
import util.safeApiCall

class AuthRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<Unit> {
        val result = safeApiCall { api.login(LoginRequest(login, password)) }
        return when (result) {
            is Result.Success -> {
                val userId = decodeUserIdFromToken(result.data.token) ?: 0
                tokenManager.saveToken(result.data.token, userId)
                Result.Success(Unit)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun register(
        login: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ): Result<Unit> {
        val result = safeApiCall {
            api.register(RegisterRequest(login, password, firstName, lastName, email, phone))
        }
        return when (result) {
            is Result.Success -> {
                val userId = decodeUserIdFromToken(result.data.token) ?: result.data.userId
                tokenManager.saveToken(result.data.token, userId)
                Result.Success(Unit)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    suspend fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    /**
     * Decodes the "Id" claim from a JWT payload.
     * Uses NO_PADDING flag so Android doesn't throw on missing '=' characters.
     */
    private fun decodeUserIdFromToken(token: String): Int? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payloadBytes = Base64.decode(
                parts[1],
                Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
            )
            val json = JSONObject(String(payloadBytes, Charsets.UTF_8))
            // JWT claim is "Id" (capital I) as set by C# GenerateJwt()
            json.optString("Id").toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
