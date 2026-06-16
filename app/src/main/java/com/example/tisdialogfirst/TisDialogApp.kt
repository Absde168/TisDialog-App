package com.example.tisdialogfirst

import android.app.Application
import data.network.ApiClient
import data.repository.AuthRepository
import data.repository.ChatRepository
import data.repository.ConnectionRepository
import data.repository.NotificationRepository
import data.repository.PaymentRepository
import data.repository.UserRepository
import util.TokenManager

class TisDialogApp : Application() {

    val tokenManager by lazy { TokenManager(this) }

    private val apiService by lazy { ApiClient.create(tokenManager) }

    val authRepository by lazy { AuthRepository(apiService, tokenManager) }
    val userRepository by lazy { UserRepository(apiService) }
    val chatRepository by lazy { ChatRepository(apiService, tokenManager) }
    val paymentRepository by lazy { PaymentRepository(apiService, tokenManager) }
    val notificationRepository by lazy { NotificationRepository(apiService) }
    val connectionRepository by lazy { ConnectionRepository(apiService) }

    companion object {
        lateinit var instance: TisDialogApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
