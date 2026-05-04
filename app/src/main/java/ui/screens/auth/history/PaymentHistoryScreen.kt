package ui.screens.auth.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.PaymentHistory
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.SuccessGreen
import ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PaymentHistoryScreen(
    onBackClick: () -> Unit,
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(4.dp))

        Text(
            text = "История платежей",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = ErrorRed,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadPayments() },
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Повторить", fontSize = 14.sp)
                    }
                }
            }

            uiState.payments.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет истории платежей",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.payments, key = { it.id }) { payment ->
                        PaymentHistoryCard(payment)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentHistoryCard(payment: PaymentHistory) {
    val isPositive = payment.amount >= 0
    val isSuccess = payment.status.equals("Success", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatDate(payment.paymentDate),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = payment.paymentMethod,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = if (isSuccess) "Успешно" else payment.status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSuccess) SuccessGreen else ErrorRed
                )
            }

            Text(
                text = if (isPositive) "+%.2f ₽".format(payment.amount)
                       else "%.2f ₽".format(payment.amount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) SuccessGreen else ErrorRed
            )
        }
    }
}

private fun formatDate(isoDate: String): String = try {
    val p = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(p.parse(isoDate)!!)
} catch (e: Exception) { isoDate.take(10) }
