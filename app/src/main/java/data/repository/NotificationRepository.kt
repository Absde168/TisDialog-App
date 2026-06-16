package data.repository

import data.model.SystemNotification
import data.network.ApiService
import util.Result
import util.safeApiCall

class NotificationRepository(private val api: ApiService) {

    suspend fun getNotifications(): Result<List<SystemNotification>> =
        safeApiCall { api.getNotifications() }

    suspend fun markAsRead(id: Int): Result<Unit> =
        safeApiCall { api.deleteNotification(id); Unit }
}
