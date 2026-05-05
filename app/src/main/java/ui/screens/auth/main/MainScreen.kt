package ui.screens.auth.main

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.PaymentHistory
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.ErrorRed
import ui.theme.Primary
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

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Синяя шапка (фиксированная) ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Primary)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                MainTopBar(
                    onProfileClick = onProfileClick,
                    onChatClick = onChatClick,
                    contentColor = Color.White
                )

                // Статус подключения
                Text(
                    text = "Подключение: Активно",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Баланс + кнопка Оплатить
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "%.2f ₽".format(uiState.balance),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Button(
                        onClick = onTopUpClick,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Primary
                        ),
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Оплатить",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )
                    }
                }
            }
        }

        // ── Прокручиваемый контент ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
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
                Spacer(Modifier.height(16.dp))

                // ── Карточка информации ────────────────────────────────────────
                InfoCard(uiState)

                Spacer(Modifier.height(12.dp))

                // ── История платежей ───────────────────────────────────────────
                PaymentHistoryCard(
                    payments = uiState.recentPayments,
                    expanded = historyExpanded,
                    onToggle = { historyExpanded = !historyExpanded },
                    onViewAll = onHistoryClick
                )

                Spacer(Modifier.height(12.dp))
            }

            uiState.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(text = it, color = ErrorRed, fontSize = 12.sp)
            }
        }

        // ── Нижняя навигация (фиксированная) ─────────────────────────────────
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = 0,
                onMainClick = { },
                onTariffsClick = onTariffsClick,
                onServicesClick = onServicesClick
            )
        }
    }
}

// ── Информационная карточка ───────────────────────────────────────────────────
@Composable
private fun InfoCard(uiState: MainUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Скорость подключения
            Text(
                text = "Скорость подключения",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Загрузка: 15 Мбит/с", fontSize = 13.sp, color = TextSecondary)
            Text(text = "Отдача: 5 Мбит/с", fontSize = 13.sp, color = TextSecondary)

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(14.dp))

            // Трафик | Лицевой счет
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Трафик",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = uiState.dataUsedFormatted,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Лицевой счет",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = uiState.user?.login ?: "—",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// ── История платежей ──────────────────────────────────────────────────────────
@Composable
private fun PaymentHistoryCard(
    payments: List<PaymentHistory>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
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
                    text = if (expanded) "∨" else ">",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(12.dp))

                if (payments.isEmpty()) {
                    Text(
                        text = "Нет данных",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                } else {
                    payments.forEachIndexed { index, p ->
                        PaymentRow(p)
                        if (index < payments.lastIndex) {
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentRow(p: PaymentHistory) {
    val isPositive = p.amount >= 0
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = formatDate(p.paymentDate),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = p.paymentMethod,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = "Сумма: ${if (isPositive) "+%.0f".format(p.amount) else "%.0f".format(p.amount)} руб.",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

private fun formatDate(iso: String): String = try {
    val p = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(p.parse(iso)!!)
} catch (e: Exception) { iso.take(10) }
