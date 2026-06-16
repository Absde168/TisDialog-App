package ui.screens.auth.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.SuccessGreen
import ui.theme.TextHint
import ui.theme.TextSecondary

@Composable
fun ContactsScreen(
    onBackClick: () -> Unit,
    viewModel: ContactsViewModel = viewModel(factory = ContactsViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        DetailTopBar(onBackClick = onBackClick, title = "Мои контакты")

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = ErrorRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            if (uiState.saveSuccess) {
                Text(
                    text = "✓  Данные успешно сохранены",
                    color = SuccessGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            ContactField(
                label = "Имя",
                value = uiState.firstName,
                onValueChange = viewModel::onFirstNameChange,
                enabled = !uiState.isSaving
            )
            Spacer(Modifier.height(16.dp))

            ContactField(
                label = "Фамилия",
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChange,
                enabled = !uiState.isSaving
            )
            Spacer(Modifier.height(16.dp))

            ContactField(
                label = "Телефон",
                value = uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                keyboardType = KeyboardType.Phone,
                enabled = !uiState.isSaving
            )
            Spacer(Modifier.height(16.dp))

            ContactField(
                label = "Email",
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                keyboardType = KeyboardType.Email,
                enabled = !uiState.isSaving
            )
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.save() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = Primary.copy(alpha = 0.5f)
            ),
            enabled = !uiState.isLoading && !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Сохранить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ContactField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(label, fontSize = 14.sp, color = TextHint) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            enabled = enabled,
            singleLine = true
        )
    }
}
