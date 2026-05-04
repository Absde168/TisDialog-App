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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun TariffsScreen(
    onBackClick: () -> Unit,
    onTariffClick: (String) -> Unit
) {
    var showTariffList by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

            Spacer(modifier = Modifier.width(32.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Текущий тариф:",
            fontSize = 16.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "СуперЛайт 100",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "500 руб./мес",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* Оплатить тариф */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Оплатить", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Другие тарифы",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Text(
                text = "→",
                fontSize = 18.sp,
                color = Primary,
                modifier = Modifier.clickable { showTariffList = true }
            )
        }

        if (showTariffList) {
            Spacer(modifier = Modifier.height(16.dp))

            TariffItem(
                name = "СуперЛайт",
                speed = "100 Мбит",
                price = "500 руб./мес",
                onClick = { onTariffClick("СуперЛайт") }
            )

            TariffItem(
                name = "СуперСтандарт",
                speed = "300 Мбит",
                price = "800 руб./мес",
                onClick = { onTariffClick("СуперСтандарт") }
            )

            TariffItem(
                name = "СуперПро",
                speed = "500 Мбит",
                price = "1200 руб./мес",
                onClick = { onTariffClick("СуперПро") }
            )

            TariffItem(
                name = "СуперМакс",
                speed = "1 Гбит",
                price = "1500 руб./мес",
                onClick = { onTariffClick("СуперМакс") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        BottomNavigationTariffs(
            onMainClick = onBackClick,
            onTariffsClick = { },
            onServicesClick = { }
        )
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
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = speed,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = price,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Primary
            )
        }
    }
}

@Composable
private fun BottomNavigationTariffs(
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
        Text(
            text = "Главная",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .clickable { onMainClick() }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Тарифы",
                fontSize = 14.sp,
                color = Primary,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "Услуги",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .clickable { onServicesClick() }
        )
    }
}