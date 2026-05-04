package ui.screens.auth.main

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.PaymentHistory
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.BackgroundLight
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.SuccessGreen
import ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTariffsClick: () -> Unit,
    onServicesClick: () -> Unit,
    onTopUpClick: () -> Unit,
    viewModel: MainViewModel = viewModel(factory = MainViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var historyExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        MainTopBar(onProfileClick = onProfileClick, onChatClick = onChatClick)

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Spacer(Modifier.height(8.dp))

            // ── Статус подключения ─────────────────────────────────────────────
            Text(
                text = "Подключение: Активно",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // ── Карточка баланса ───────────────────────────────────────────────
            BalanceCard(
                balance = uiState.balance,
                onTopUpClick = onTopUpClick
            )

            Spacer(Modifier.height(12.dp))

            // ── Карточка информации ────────────────────────────────────────────
            InfoCard(uiState)

            Spacer(Modifier.height(12.dp))

            // ── История платежей (сворачиваемая) ──────────────────────────────
            PaymentHistoryCard(
                payments = uiState.recentPayments,
                expanded = historyExpanded,
                onToggle = { historyExpanded = !historyExpanded },
                onViewAll = onHistoryClick,
                onRefresh = { viewModel.loadData() }
            )
        }

        uiState.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(text = it, color = ErrorRed, fontSize = 12.sp)
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(12.dp))

        // ── Нижняя навигация ───────────────────────────────────────────────────
        AppBottomNav(
            activeTab = 0,
            onMainClick = { },
            onTariffsClick = onTariffsClick,
            onServicesClick = onServicesClick
        )
        Spacer(Modifier.height(16.dp))
    }
}

// ── Карточка Баланса ──────────────────────────────────────────────────────────
@Composable
private fun BalanceCard(balance: Double, onTopUpClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Баланс", fontSize = 13.sp, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "%.2f ₽".format(balance),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) Color.Black else ErrorRed
                )
            }
            Button(
                onClick = onTopUpClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.height(38.dp)
            ) {
                Text("Оплатить", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ── Информационная карточка ───────────────────────────────────────────────────
@Composable
private fun InfoCard(uiState: MainUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Скорость подключения:",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            InfoRow("Загрузка:", "15 Мбит/с")
            InfoRow("Отдача:", "5 Мбит/с")
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(8.dp))
            InfoRow("Трафик:", uiState.dataUsedFormatted)
            InfoRow("Лицевой счет:", uiState.user?.login ?: "—")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary, modifier = Modifier.weight(1f))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}

// ── История платежей ──────────────────────────────────────────────────────────
@Composable
private fun PaymentHistoryCard(
    payments: List<PaymentHistory>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onViewAll: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "История платежей",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
                Text(
                    text = if (expanded) "▼" else "▶",
                    fontSize = 13.sp,
                    color = Primary
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(Modifier.height(12.dp))

                if (payments.isEmpty()) {
                    Text(
                        text = "Нет данных",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                } else {
                    payments.forEach { p ->
                        PaymentRow(p)
                        Spacer(Modifier.height(10.dp))
                    }
                    TextButton(
                        onClick = onViewAll,
                        modifier = Modifier.align(Alignment.End),
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        Text(
                            text = "Вся история →",
                            color = Primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentRow(p: PaymentHistory) {
    val isPositive = p.amount >= 0
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(formatDate(p.paymentDate), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Primary)
            Text(p.paymentMethod, fontSize = 12.sp, color = TextSecondary)
        }
        Text(
            text = if (isPositive) "+%.2f ₽".format(p.amount) else "%.2f ₽".format(p.amount),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isPositive) SuccessGreen else ErrorRed
        )
    }
}

private fun formatDate(iso: String): String = try {
    val p = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(p.parse(iso)!!)
} catch (e: Exception) { iso.take(10) }
