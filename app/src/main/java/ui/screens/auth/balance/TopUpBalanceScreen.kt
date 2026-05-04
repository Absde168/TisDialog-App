package ui.screens.auth.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import ui.theme.TextHint
import ui.theme.TextSecondary

@Composable
fun TopUpBalanceScreen(
    onBackClick: () -> Unit,
    viewModel: TopUpViewModel = viewModel(factory = TopUpViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var amount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("card") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick, title = "Пополнить баланс")

        Spacer(Modifier.height(24.dp))

        // ── Ошибка ────────────────────────────────────────────────────────────
        uiState.error?.let { error ->
            Text(
                text = error,
                color = ErrorRed,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        // ── Поле суммы ────────────────────────────────────────────────────────
        Text(
            text = "Сумма пополнения",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                if (uiState.error != null) viewModel.clearError()
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0.00", fontSize = 14.sp, color = TextHint) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = ErrorRed,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            prefix = { Text("₽ ", color = Primary, fontWeight = FontWeight.Medium) },
            isError = uiState.error != null,
            enabled = !uiState.isLoading,
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        // ── Способ оплаты ─────────────────────────────────────────────────────
        Text(
            text = "Способ оплаты",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PaymentMethodButton(
                text = "Карта",
                icon = "💳",
                selected = selectedMethod == "card",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "card" }
            )
            PaymentMethodButton(
                text = "СБП",
                icon = "⚡",
                selected = selectedMethod == "sbp",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "sbp" }
            )
            PaymentMethodButton(
                text = "Счёт",
                icon = "🏦",
                selected = selectedMethod == "account",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "account" }
            )
        }

        Spacer(Modifier.weight(1f))

        // ── Кнопка оплаты ─────────────────────────────────────────────────────
        Button(
            onClick = { viewModel.pay(amount, selectedMethod) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = Primary.copy(alpha = 0.5f)
            ),
            enabled = amount.isNotBlank() && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Оплатить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun PaymentMethodButton(
    text: String,
    icon: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(64.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Primary else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (selected) 0.dp else 1.dp),
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                 else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
