package ui.screens.auth.services

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
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.Primary

@Composable
fun ServicesScreen(
    onBackClick: () -> Unit,
    onMainClick: () -> Unit = onBackClick,
    onTariffsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    var vpnActive by remember { mutableStateOf(false) }
    var antivirusActive by remember { mutableStateOf(false) }
    var parentalActive by remember { mutableStateOf(true) }
    var staticIpActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        MainTopBar(
            onProfileClick = onProfileClick,
            onChatClick = onChatClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Дополнительные услуги",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ServiceCard(
                name = "VPN",
                description = "Защита вашего интернета",
                isActive = vpnActive,
                onToggle = { vpnActive = !vpnActive }
            )
            Spacer(Modifier.height(12.dp))

            ServiceCard(
                name = "Антивирус",
                description = "Защита от вирусов",
                isActive = antivirusActive,
                onToggle = { antivirusActive = !antivirusActive }
            )
            Spacer(Modifier.height(12.dp))

            ServiceCard(
                name = "Родительский контроль",
                description = "Безопасность детей в сети",
                isActive = parentalActive,
                onToggle = { parentalActive = !parentalActive }
            )
            Spacer(Modifier.height(12.dp))

            ServiceCard(
                name = "Статический IP",
                description = "Постоянный адрес в сети",
                isActive = staticIpActive,
                onToggle = { staticIpActive = !staticIpActive }
            )

            Spacer(Modifier.height(12.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = 2,
                onMainClick = onMainClick,
                onTariffsClick = onTariffsClick,
                onServicesClick = {}
            )
        }
    }
}

@Composable
private fun ServiceCard(
    name: String,
    description: String,
    isActive: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Primary),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onToggle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(
                    text = if (isActive) "Отключить" else "Подключить",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}
