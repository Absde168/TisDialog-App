package ui.screens.auth.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tisdialogfirst.R
import ui.theme.Primary

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    onAlreadyLoggedIn: () -> Unit = onTimeout,
    viewModel: SplashViewModel = viewModel(factory = SplashViewModel.factory())
) {
    val alphaAnim = remember { Animatable(0f) }
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(targetValue = 1f, animationSpec = tween(900))
    }

    LaunchedEffect(destination) {
        when (destination) {
            is SplashDestination.Auth -> onTimeout()
            is SplashDestination.Main -> onAlreadyLoggedIn()
            is SplashDestination.None -> Unit
        }
    }

    // Синий фон + прозрачный логотип — никакого белого прямоугольника
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dialog_logo),
            contentDescription = "ДИАЛОГ",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .fillMaxWidth(0.65f)
        )
    }
}
