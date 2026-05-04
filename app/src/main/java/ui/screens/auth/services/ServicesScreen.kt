package ui.screens.auth.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun ServicesScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            // Кнопка назад
            Text(
                text = "← Назад",
                fontSize = 14.sp,
                color = Primary,
                modifier = Modifier.clickable { onBackClick() }
            )

            // Логотип
            Text(
                text = "ДИАЛОГ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Spacer(modifier = Modifier.width(32.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Заголовок
        Text(
            text = "Дополнительные услуги",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // === СПИСОК УСЛУГ ===
        ServiceItem(
            name = "VPN",
            description = "Защита вашего интернета",
            isActive = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        ServiceItem(
            name = "Антивирус",
            description = "Защита от вирусов",
            isActive = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        ServiceItem(
            name = "Родительский контроль",
            description = "Безопасность детей в сети",
            isActive = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        ServiceItem(
            name = "Статический IP",
            description = "Постоянный адрес в сети",
            isActive = false
        )

        Spacer(modifier = Modifier.weight(1f))

        // === НИЖНЯЯ НАВИГАЦИЯ ===
        BottomNavigationServices(
            onMainClick = onBackClick,
            onTariffsClick = { },
            onServicesClick = { }
        )
    }
}

@Composable
private fun ServiceItem(
    name: String,
    description: String,
    isActive: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Switch(
                checked = isActive,
                onCheckedChange = { /* TODO */ },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Primary,
                    checkedTrackColor = Primary.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun BottomNavigationServices(
    onMainClick: () -> Unit,
    onTariffsClick: () -> Unit,
    onServicesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Primary),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Главная
        Text(
            text = "Главная",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .clickable { onMainClick() }
        )

        // Тарифы
        Text(
            text = "Тарифы",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .clickable { onTariffsClick() }
        )

        // Услуги (активная)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Услуги",
                fontSize = 14.sp,
                color = Primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}