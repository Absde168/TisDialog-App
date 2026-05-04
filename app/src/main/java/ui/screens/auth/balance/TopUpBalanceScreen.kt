package ui.screens.auth.balance

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
import ui.theme.Primary

@Composable
fun TopUpBalanceScreen(
    onBackClick: () -> Unit,
    viewModel: TopUpViewModel = viewModel(factory = TopUpViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var amount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("card") }

    // Navigate back after successful payment
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // === ВЕРХНЯЯ ПАНЕЛЬ ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "← Назад",
                fontSize = 14.sp,
                color = Primary,
                modifier = Modifier.clickable { onBackClick() }
            )
            Text(
                text = "Пополнить баланс",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // === ОШИБКА ===
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 14.sp
                )
            }
        }

        // === ПОЛЕ СУММЫ ===
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                if (uiState.error != null) viewModel.clearError()
            },
            label = { Text("Сумма, руб.") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Gray
            ),
            prefix = { Text("₽", color = Primary) },
            isError = uiState.error != null,
            enabled = !uiState.isLoading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // === СПОСОБЫ ОПЛАТЫ ===
        Text(
            text = "Способ оплаты",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodButton(
                text = "Карта",
                selected = selectedMethod == "card",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "card" }
            )
            PaymentMethodButton(
                text = "СБП",
                selected = selectedMethod == "sbp",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "sbp" }
            )
            PaymentMethodButton(
                text = "Счёт",
                selected = selectedMethod == "account",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "account" }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // === КНОПКА ОПЛАТИТЬ ===
        Button(
            onClick = { viewModel.pay(amount, selectedMethod) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = amount.isNotBlank() && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Оплатить", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PaymentMethodButton(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Primary else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else Color.Black
            )
        }
    }
}
