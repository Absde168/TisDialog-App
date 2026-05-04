package ui.screens.auth.tariffs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.Primary
import ui.theme.TextSecondary

// Простое статическое описание тарифов по имени
private data class TariffDetails(
    val speed: String,
    val traffic: String,
    val price: String,
    val description: String
)

private fun getTariffDetails(name: String): TariffDetails = when (name) {
    "СуперЛайт"     -> TariffDetails("до 100 Мбит/с", "Безлимитный", "500 ₽/мес",  "Идеально для повседневного использования")
    "СуперСтандарт" -> TariffDetails("до 300 Мбит/с", "Безлимитный", "800 ₽/мес",  "Оптимальный выбор для семьи")
    "СуперПро"      -> TariffDetails("до 500 Мбит/с", "Безлимитный", "1 200 ₽/мес","Для геймеров и стримеров")
    "СуперМакс"     -> TariffDetails("до 1 Гбит/с",   "Безлимитный", "1 500 ₽/мес","Максимальная скорость без ограничений")
    else            -> TariffDetails("до 100 Мбит/с", "Безлимитный", "500 ₽/мес",  "")
}

@Composable
fun TariffDetailScreen(
    tariffName: String,
    onBackClick: () -> Unit,
    onConnectClick: () -> Unit
) {
    val details = getTariffDetails(tariffName)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(16.dp))

        // ── Карточка тарифа ───────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = tariffName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(Modifier.height(4.dp))
                if (details.description.isNotBlank()) {
                    Text(
                        text = details.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(20.dp))

                TariffDetailRow("Скорость", details.speed)
                Spacer(Modifier.height(10.dp))
                TariffDetailRow("Трафик", details.traffic)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Стоимость",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = details.price,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Что входит ────────────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Что входит в тариф",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(12.dp))
                IncludeItem("✓  Безлимитный интернет")
                Spacer(Modifier.height(6.dp))
                IncludeItem("✓  Техподдержка 24/7")
                Spacer(Modifier.height(6.dp))
                IncludeItem("✓  Личный кабинет")
                Spacer(Modifier.height(6.dp))
                IncludeItem("✓  Статистика использования")
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))

        // ── Кнопка подключить ─────────────────────────────────────────────────
        Button(
            onClick = onConnectClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text(
                text = "Подключить тариф",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TariffDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun IncludeItem(text: String) {
    Text(text = text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
}
