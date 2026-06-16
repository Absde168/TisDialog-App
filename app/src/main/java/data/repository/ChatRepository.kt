package data.repository

import data.model.ChatMessage
import data.network.ApiService
import util.Result
import util.TokenManager
import util.safeApiCall

class ChatRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getMessages(): Result<List<ChatMessage>> {
        val userId = tokenManager.getUserId()
        return safeApiCall { api.getChatMessages(userId) }
    }

    suspend fun sendMessage(content: String): Result<List<ChatMessage>> {
        val userId = tokenManager.getUserId() ?: 0
        return safeApiCall {
            api.sendChatMessage(
                ChatMessage(userId = userId, content = content, senderType = "User")
            )
        }
    }
}
