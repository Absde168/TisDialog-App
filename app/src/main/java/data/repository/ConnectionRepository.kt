package data.repository

import data.model.ConnectionHistory
import data.network.ApiService
import util.Result
import util.safeApiCall

class ConnectionRepository(private val api: ApiService) {

    suspend fun getConnectionHistory(): Result<List<ConnectionHistory>> =
        safeApiCall { api.getConnectionHistory() }

    /** Total bytes used across all sessions */
    suspend fun getTotalDataUsed(): Result<Long> {
        return when (val result = getConnectionHistory()) {
            is Result.Success -> Result.Success(result.data.sumOf { it.dataUsedBytes })
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
