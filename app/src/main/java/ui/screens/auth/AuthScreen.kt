package ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.TextHint
import ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory())
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))

        // ── Заголовок ──────────────────────────────────────────────────────────
        Text(
            text = "Авторизация",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // ── Ошибка API ─────────────────────────────────────────────────────────
        uiState.error?.let { error ->
            Text(
                text = error,
                color = ErrorRed,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // ── Поле логин (rounded outline стиль) ────────────────────────────────
        AuthTextField(
            value = login,
            onValueChange = {
                login = it
                loginError = null
                if (uiState.error != null) authViewModel.clearError()
            },
            label = "Логин",
            placeholder = "user@example.com",
            isError = loginError != null,
            errorText = loginError,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Поле пароль ────────────────────────────────────────────────────────
        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                if (uiState.error != null) authViewModel.clearError()
            },
            label = "Пароль",
            placeholder = "••••••••",
            isError = passwordError != null,
            errorText = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Кнопка Войти ───────────────────────────────────────────────────────
        Button(
            onClick = {
                if (validate(login, password, { loginError = it }, { passwordError = it })) {
                    authViewModel.login(login, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = Primary.copy(alpha = 0.6f)
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Войти",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Primary)
        ) {
            Text(
                text = "Зарегистрироваться",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { }) {
            Text(
                text = "Забыли пароль?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label, fontSize = 13.sp) },
            placeholder = { Text(text = placeholder, fontSize = 14.sp, color = TextHint) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            visualTransformation = visualTransformation,
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color(0xFFDDE3EF),
                errorBorderColor = ErrorRed,
                focusedLabelColor = Primary,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = Primary
            )
        )
        if (errorText != null) {
            Text(
                text = errorText,
                color = ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

private fun validate(
    login: String,
    password: String,
    onLoginError: (String) -> Unit,
    onPasswordError: (String) -> Unit
): Boolean {
    var valid = true
    if (login.isBlank()) { onLoginError("Введите логин"); valid = false }
    if (password.isEmpty()) { onPasswordError("Введите пароль"); valid = false }
    else if (password.length < 6) { onPasswordError("Минимум 6 символов"); valid = false }
    return valid
}
