package ui.screens.auth.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Полноэкранный синий фон по Figma
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Логотип из drawable на синем фоне
            Image(
                painter = painterResource(id = R.drawable.dialog_logo),
                contentDescription = "ДИАЛОГ",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Название бренда под логотипом
            Text(
                text = "ДИАЛОГ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 3.sp
            )
        }
    }
}
