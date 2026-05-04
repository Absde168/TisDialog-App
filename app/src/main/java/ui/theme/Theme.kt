package ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    onPrimary        = WhiteText,
    primaryContainer = Primary,
    secondary        = Secondary,
    onSecondary      = WhiteText,
    background       = BackgroundLight,
    onBackground     = TextPrimary,
    surface          = SurfaceWhite,
    onSurface        = TextPrimary,
    error            = ErrorRed,
    onError          = WhiteText,
    errorContainer   = ErrorRed.copy(alpha = 0.12f),
    onErrorContainer = ErrorRed,
    outline          = TextHint
)

private val DarkColorScheme = darkColorScheme(
    primary             = Primary,
    onPrimary           = WhiteText,
    primaryContainer    = Secondary,
    onPrimaryContainer  = WhiteText,
    secondary           = Secondary,
    onSecondary         = WhiteText,
    background          = DarkBg,
    onBackground        = TextOnDark,
    surface             = DarkSurface,
    onSurface           = TextOnDark,
    surfaceVariant      = DarkSurfaceVariant,
    onSurfaceVariant    = TextSecondaryDark,
    error               = ErrorRed,
    onError             = WhiteText,
    outline             = Color(0xFF555555),
    outlineVariant      = Color(0xFF3A3A3A)
)

@Composable
fun TisDialogFirstTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = Primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
