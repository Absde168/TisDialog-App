package data.model

import com.google.gson.annotations.SerializedName

// ─── Auth ────────────────────────────────────────────────────────────────────

data class LoginRequest(
    @SerializedName("Login") val login: String,
    @SerializedName("Password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String
)

data class RegisterRequest(
    @SerializedName("Login")     val login: String,
    @SerializedName("Password")  val password: String,
    @SerializedName("FirstName") val firstName: String,
    @SerializedName("LastName")  val lastName: String,
    @SerializedName("Email")     val email: String,
    @SerializedName("Phone")     val phone: String
)

data class RegisterResponse(
    @SerializedName("token")  val token: String,
    @SerializedName("userId") val userId: Int
)

// ─── User ────────────────────────────────────────────────────────────────────

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("passwordHash") val passwordHash: String? = null,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("registrationDate") val registrationDate: String
) {
    val fullName: String get() = "$lastName $firstName"
}

data class UpdateUserRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
)

// ─── Chat ────────────────────────────────────────────────────────────────────

data class ChatMessage(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("userId") val userId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("senderType") val senderType: String = "User",
    @SerializedName("timestamp") val timestamp: String = ""
) {
    val isFromUser: Boolean get() = senderType == "User"
}

// ─── Payment History ─────────────────────────────────────────────────────────

data class PaymentHistory(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("userId") val userId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("paymentDate") val paymentDate: String,
    @SerializedName("paymentMethod") val paymentMethod: String = "Card",
    @SerializedName("status") val status: String = "Success",
    @SerializedName("transactionId") val transactionId: String = ""
)

// ─── Connection History ───────────────────────────────────────────────────────

data class ConnectionHistory(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("userId") val userId: Int,
    @SerializedName("connectedAt") val connectedAt: String,
    @SerializedName("disconnectedAt") val disconnectedAt: String?,
    @SerializedName("dataUsedBytes") val dataUsedBytes: Long = 0
)

// ─── System Notification ─────────────────────────────────────────────────────

data class SystemNotification(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("type") val type: String = "Info",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("isRead") val isRead: Boolean = false,
    @SerializedName("userId") val userId: Int? = null
)
