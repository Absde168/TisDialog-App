package ui.screens.auth.balance

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tisdialogfirst.R
import ui.components.DetailTopBar
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.TextHint

@Composable
fun TopUpBalanceScreen(
    onBackClick: () -> Unit,
    onOtherBanksClick: () -> Unit = {},
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
            // ── Карта МИР ─────────────────────────────────────────────────────
            PaymentMethodCard(
                label = "Карта",
                selected = selectedMethod == "card",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "card" }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mir_logo),
                    contentDescription = "Мир",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(28.dp)
                        .widthIn(max = 64.dp)
                )
            }

            // ── СБП ───────────────────────────────────────────────────────────
            PaymentMethodCard(
                label = "СБП",
                selected = selectedMethod == "sbp",
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = { selectedMethod = "sbp" }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sbp_logo),
                    contentDescription = "СБП",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(28.dp)
                        .widthIn(max = 64.dp)
                )
            }

            // ── Другой банк ───────────────────────────────────────────────────
            PaymentMethodCard(
                label = "Добавить",
                selected = false,
                enabled = !uiState.isLoading,
                modifier = Modifier.weight(1f),
                onClick = onOtherBanksClick
            ) {
                Text(
                    text = "+",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
            enabled = amount.isNotBlank() && !uiState.isLoading && selectedMethod != ""
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
private fun PaymentMethodCard(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .height(72.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Primary else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (selected) 0.dp else 1.dp),
        border = if (!selected) androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
