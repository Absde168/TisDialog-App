package util

import kotlinx.coroutines.CancellationException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int = -1) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
    return try {
        Result.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: retrofit2.HttpException) {
        val message = when (e.code()) {
            401 -> "Неверный логин или пароль"
            403 -> "Доступ запрещён"
            404 -> "Ресурс не найден"
            500 -> "Ошибка сервера"
            else -> "Ошибка: ${e.code()}"
        }
        Result.Error(message, e.code())
    } catch (e: java.net.ConnectException) {
        Result.Error("Не удалось подключиться к серверу. Проверьте, запущен ли бэкенд")
    } catch (e: java.net.UnknownHostException) {
        Result.Error("Сервер недоступен. Проверьте подключение к сети")
    } catch (e: java.net.SocketTimeoutException) {
        Result.Error("Превышено время ожидания. Сервер не отвечает")
    } catch (e: Exception) {
        Result.Error(e.message ?: "Неизвестная ошибка")
    }
}
