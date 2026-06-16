package data.network

import data.model.ChatMessage
import data.model.ConnectionHistory
import data.model.LoginRequest
import data.model.LoginResponse
import data.model.RegisterRequest
import data.model.RegisterResponse
import data.model.PaymentHistory
import data.model.SystemNotification
import data.model.UpdateUserRequest
import data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("api/users/profile")
    suspend fun getProfile(): User

    @GET("api/users")
    suspend fun getUsers(): List<User>

    @POST("api/users")
    suspend fun createUser(@Body user: User): User

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: UpdateUserRequest): User

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)

    @GET("api/chatmessages")
    suspend fun getChatMessages(@Query("userId") userId: Int? = null): List<ChatMessage>

    @POST("api/chatmessages")
    suspend fun sendChatMessage(@Body message: ChatMessage): List<ChatMessage>

    @DELETE("api/chatmessages/{id}")
    suspend fun deleteChatMessage(@Path("id") id: Int)

    @GET("api/paymenthistory")
    suspend fun getPaymentHistory(): List<PaymentHistory>

    @POST("api/paymenthistory")
    suspend fun createPayment(@Body payment: PaymentHistory): PaymentHistory

    @DELETE("api/paymenthistory/{id}")
    suspend fun deletePayment(@Path("id") id: Int)

    @GET("api/connectionhistory")
    suspend fun getConnectionHistory(): List<ConnectionHistory>

    @POST("api/connectionhistory")
    suspend fun createConnectionRecord(@Body record: ConnectionHistory): ConnectionHistory

    @GET("api/systemnotifications")
    suspend fun getNotifications(): List<SystemNotification>

    @POST("api/systemnotifications")
    suspend fun createNotification(@Body notification: SystemNotification): SystemNotification

    @DELETE("api/systemnotifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int)
}
