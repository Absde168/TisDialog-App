package ui.screens.auth.tariffs

import androidx.compose.foundation.BorderStroke
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
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun TariffsScreen(
    onBackClick: () -> Unit,
    onTariffClick: (String) -> Unit,
    onMainClick: () -> Unit = onBackClick,
    onServicesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onPayClick: () -> Unit = {}
) {
    var showTariffList by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

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

                Text(
                    text = "Текущий тариф:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Карточка текущего тарифа внутри синей шапки
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"СуперГига 100\"",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "500 руб./мес",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onPayClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(
                                text = "Оплатить",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

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
                    color = Primary
                )
                Text(
                    text = if (showTariffList) "∨" else ">",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }

            if (showTariffList) {
                Spacer(Modifier.height(8.dp))
                TariffItem("СуперЛайт", "100 Мбит", "500 руб./мес") { onTariffClick("СуперЛайт") }
                TariffItem("СуперСтандарт", "300 Мбит", "800 руб./мес") { onTariffClick("СуперСтандарт") }
                TariffItem("СуперПро", "500 Мбит", "1 200 руб./мес") { onTariffClick("СуперПро") }
                TariffItem("СуперМакс", "1 Гбит", "1 500 руб./мес") { onTariffClick("СуперМакс") }
            }

            Spacer(Modifier.height(12.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = 1,
                onMainClick = onMainClick,
                onTariffsClick = {},
                onServicesClick = onServicesClick
            )
        }
    }
}

@Composable
private fun TariffItem(
    name: String,
    speed: String,
    price: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "\"$name\"",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = speed,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = price,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Primary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.height(34.dp)
        ) {
            Text(
                text = "Подробнее",
                fontSize = 13.sp,
                color = Primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}
