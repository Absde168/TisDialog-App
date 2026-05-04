package ui.screens.auth.tariffs

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.AppBottomNav
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun TariffsScreen(
    onBackClick: () -> Unit,
    onTariffClick: (String) -> Unit,
    onMainClick: () -> Unit = onBackClick,
    onServicesClick: () -> Unit = {}
) {
    var showTariffList by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Тарифы",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // ── Текущий тариф ─────────────────────────────────────────────────────
        Text(
            text = "Текущий тариф",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "СуперЛайт 100",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "500 ₽/мес",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Скорость до 100 Мбит/с",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Оплатить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Другие тарифы ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTariffList = !showTariffList }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Другие тарифы",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (showTariffList) "▼" else "▶",
                fontSize = 13.sp,
                color = Primary
            )
        }

        if (showTariffList) {
            Spacer(Modifier.height(12.dp))

            TariffItem("СуперЛайт", "до 100 Мбит/с", "500 ₽/мес") { onTariffClick("СуперЛайт") }
            Spacer(Modifier.height(8.dp))
            TariffItem("СуперСтандарт", "до 300 Мбит/с", "800 ₽/мес") { onTariffClick("СуперСтандарт") }
            Spacer(Modifier.height(8.dp))
            TariffItem("СуперПро", "до 500 Мбит/с", "1 200 ₽/мес") { onTariffClick("СуперПро") }
            Spacer(Modifier.height(8.dp))
            TariffItem("СуперМакс", "до 1 Гбит/с", "1 500 ₽/мес") { onTariffClick("СуперМакс") }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(12.dp))

        // ── Нижняя навигация ─────────────────────────────────────────────────
        AppBottomNav(
            activeTab = 1,
            onMainClick = onMainClick,
            onTariffsClick = {},
            onServicesClick = onServicesClick
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TariffItem(
    name: String,
    speed: String,
    price: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
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
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(text = speed, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = price,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
            )
        }
    }
}
