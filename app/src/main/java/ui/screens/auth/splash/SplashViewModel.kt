package ui.screens.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tisdialogfirst.TisDialogApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.TokenManager

sealed class SplashDestination {
    object None : SplashDestination()
    object Auth : SplashDestination()
    object Main : SplashDestination()
}

class SplashViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.None)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        checkLoginState()
    }

    private fun checkLoginState() {
        viewModelScope.launch {
            delay(1500) // минимальное время показа сплэша
            _destination.value = if (tokenManager.isLoggedIn())
                SplashDestination.Main
            else
                SplashDestination.Auth
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(TisDialogApp.instance.tokenManager) as T
            }
        }
    }
}
