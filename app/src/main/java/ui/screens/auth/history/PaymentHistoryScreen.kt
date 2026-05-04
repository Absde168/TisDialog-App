package ui.screens.auth.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.PaymentHistory
import ui.theme.Primary
import ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PaymentHistoryScreen(
    onBackClick: () -> Unit,
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        // === ВЕРХНЯЯ ПАНЕЛЬ ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                text = "ДИАЛОГ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Text(
            text = "История платежей",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadPayments() },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Повторить")
                    }
                }
            }

            uiState.payments.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет истории платежей", color = TextSecondary, fontSize = 16.sp)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.payments, key = { it.id }) { payment ->
                        PaymentHistoryCard(payment)
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }

        // === НИЖНЯЯ НАВИГАЦИЯ ===
        BottomNavigationHistory(
            onMainClick = onBackClick,
            onTariffsClick = { },
            onServicesClick = { }
        )
    }
}

@Composable
private fun PaymentHistoryCard(payment: PaymentHistory) {
    val isPositive = payment.amount >= 0
    val amountStr = if (isPositive) "+%.2f руб.".format(payment.amount)
    else "%.2f руб.".format(payment.amount)
    val statusColor = if (payment.status == "Success") Color(0xFF2E7D32) else Color.Red

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = payment.paymentMethod,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (payment.status == "Success") "Успешно" else payment.status,
                    fontSize = 12.sp,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = amountStr,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) Color(0xFF2E7D32) else Color.Red
            )
        }
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.format(parser.parse(isoDate) ?: return isoDate)
    } catch (e: Exception) {
        isoDate.take(10)
    }
}

@Composable
private fun BottomNavigationHistory(
    onMainClick: () -> Unit,
    onTariffsClick: () -> Unit,
    onServicesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Primary)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onMainClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Главная", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onTariffsClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Тарифы", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onServicesClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Услуги", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}
