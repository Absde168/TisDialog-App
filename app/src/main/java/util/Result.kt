package util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int = -1) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

inline fun <T> safeApiCall(call: () -> T): Result<T> {
    return try {
        Result.Success(call())
    } catch (e: retrofit2.HttpException) {
        val message = when (e.code()) {
            401 -> "Неверный логин или пароль"
            403 -> "Доступ запрещён"
            404 -> "Ресурс не найден"
            500 -> "Ошибка сервера"
            else -> "Ошибка: ${e.code()}"
        }
        Result.Error(message, e.code())
    } catch (e: java.net.UnknownHostException) {
        Result.Error("Нет соединения с сервером")
    } catch (e: java.net.SocketTimeoutException) {
        Result.Error("Превышено время ожидания")
    } catch (e: Exception) {
        Result.Error(e.message ?: "Неизвестная ошибка")
    }
}
