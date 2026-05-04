package ui.screens.auth.services

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
import ui.components.AppBottomNav
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.Primary

@Composable
fun ServicesScreen(
    onBackClick: () -> Unit,
    onMainClick: () -> Unit = onBackClick,
    onTariffsClick: () -> Unit = {}
) {
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
            text = "Дополнительные услуги",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        // ── Карточки услуг (Figma: тёмно-синий фон) ──────────────────────────
        ServiceCard(
            name = "VPN",
            description = "Защита вашего интернета",
            isActive = false,
            onConnect = {}
        )
        Spacer(Modifier.height(12.dp))

        ServiceCard(
            name = "Антивирус",
            description = "Защита от вирусов и угроз",
            isActive = false,
            onConnect = {}
        )
        Spacer(Modifier.height(12.dp))

        ServiceCard(
            name = "Родительский контроль",
            description = "Безопасность детей в сети",
            isActive = true,
            onConnect = {}
        )
        Spacer(Modifier.height(12.dp))

        ServiceCard(
            name = "Статический IP",
            description = "Постоянный адрес в сети",
            isActive = false,
            onConnect = {}
        )

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(12.dp))

        // ── Нижняя навигация ─────────────────────────────────────────────────
        AppBottomNav(
            activeTab = 2,
            onMainClick = onMainClick,
            onTariffsClick = onTariffsClick,
            onServicesClick = {}
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ServiceCard(
    name: String,
    description: String,
    isActive: Boolean,
    onConnect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Primary),
        elevation = CardDefaults.cardElevation(0.dp)
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
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Spacer(Modifier.width(12.dp))

            if (isActive) {
                Box(
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.25f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Активно",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = onConnect,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Подключить",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
