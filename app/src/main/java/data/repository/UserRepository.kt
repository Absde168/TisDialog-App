package data.repository

import data.model.UpdateUserRequest
import data.model.User
import data.network.ApiService
import util.Result
import util.safeApiCall

class UserRepository(private val api: ApiService) {

    suspend fun getProfile(): Result<User> = safeApiCall { api.getProfile() }

    suspend fun getUsers(): Result<List<User>> = safeApiCall { api.getUsers() }

    suspend fun updateProfile(userId: Int, firstName: String, lastName: String, phone: String, email: String): Result<User> =
        safeApiCall {
            api.updateUser(
                id = userId,
                request = UpdateUserRequest(firstName, lastName, phone, email)
            )
        }
}
