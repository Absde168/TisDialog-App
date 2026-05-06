package ui.screens.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
import ui.components.MainTopBar
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.TextHint
import ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    vm: RegisterViewModel = viewModel(factory = RegisterViewModel.factory())
) {
    var firstName       by remember { mutableStateOf("") }
    var lastName        by remember { mutableStateOf("") }
    var login           by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var firstNameError       by remember { mutableStateOf<String?>(null) }
    var lastNameError        by remember { mutableStateOf<String?>(null) }
    var loginError           by remember { mutableStateOf<String?>(null) }
    var emailError           by remember { mutableStateOf<String?>(null) }
    var passwordError        by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRegisterSuccess()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Шапка ─────────────────────────────────────────────────────────────
        MainTopBar(
            onProfileClick = {},
            onChatClick = {}
        )

        // ── Прокручиваемый контент ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Строка «< Регистрация договора» ───────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Назад",
                        tint = Primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Регистрация",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(Modifier.height(20.dp))

            uiState.error?.let {
                Text(
                    text = it,
                    color = ErrorRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            RegField(
                value = firstName,
                onValueChange = { firstName = it; firstNameError = null },
                label = "Имя",
                placeholder = "Иван",
                isError = firstNameError != null,
                errorText = firstNameError,
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = lastName,
                onValueChange = { lastName = it; lastNameError = null },
                label = "Фамилия",
                placeholder = "Иванов",
                isError = lastNameError != null,
                errorText = lastNameError,
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = login,
                onValueChange = { login = it; loginError = null; if (uiState.error != null) vm.clearError() },
                label = "Логин",
                placeholder = "ivan_ivanov",
                isError = loginError != null,
                errorText = loginError,
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = "Email",
                placeholder = "ivan@example.com",
                isError = emailError != null,
                errorText = emailError,
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = phone,
                onValueChange = { phone = it },
                label = "Телефон (необязательно)",
                placeholder = "+7 999 123-45-67",
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = "Пароль",
                placeholder = "минимум 6 символов",
                isError = passwordError != null,
                errorText = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !uiState.isLoading
            )
            Spacer(Modifier.height(12.dp))

            RegField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = null },
                label = "Подтвердите пароль",
                placeholder = "••••••••",
                isError = confirmPasswordError != null,
                errorText = confirmPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !uiState.isLoading
            )

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    if (validateRegister(
                            firstName, lastName, login, email, password, confirmPassword,
                            { firstNameError = it }, { lastNameError = it }, { loginError = it },
                            { emailError = it }, { passwordError = it }, { confirmPasswordError = it }
                        )
                    ) {
                        vm.register(login, password, firstName, lastName, email, phone)
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
                        "Зарегистрироваться",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onBackToLogin) {
                Text("Уже есть аккаунт? Войти", fontSize = 14.sp, color = Primary)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegField(
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
            label = { Text(label, fontSize = 13.sp) },
            placeholder = { Text(placeholder, fontSize = 14.sp, color = TextHint) },
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

private fun validateRegister(
    firstName: String, lastName: String, login: String, email: String,
    password: String, confirmPassword: String,
    onFirstNameError: (String) -> Unit, onLastNameError: (String) -> Unit,
    onLoginError: (String) -> Unit, onEmailError: (String) -> Unit,
    onPasswordError: (String) -> Unit, onConfirmPasswordError: (String) -> Unit
): Boolean {
    var valid = true
    if (firstName.isBlank())              { onFirstNameError("Введите имя"); valid = false }
    if (lastName.isBlank())               { onLastNameError("Введите фамилию"); valid = false }
    if (login.isBlank())                  { onLoginError("Введите логин"); valid = false }
    if (email.isBlank() || "@" !in email) { onEmailError("Введите корректный email"); valid = false }
    if (password.length < 6)             { onPasswordError("Минимум 6 символов"); valid = false }
    if (password != confirmPassword)      { onConfirmPasswordError("Пароли не совпадают"); valid = false }
    return valid
}
