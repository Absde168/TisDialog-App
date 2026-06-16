package ui.screens.auth.tariffs

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.Primary
import ui.theme.TextSecondary

private data class TariffDetails(
    val speed: String,
    val traffic: String,
    val price: String,
    val description: String
)

private fun getTariffDetails(name: String): TariffDetails = when (name) {
    "СуперЛайт"     -> TariffDetails("до 100 Мбит/с", "Безлимитный", "500 руб./мес",  "Скорость 100 Мбит/с\nТехнология xPON до дома\nв квартиру FTTB")
    "СуперСтандарт" -> TariffDetails("до 300 Мбит/с", "Безлимитный", "800 руб./мес",  "Скорость 300 Мбит/с\nТехнология xPON до дома\nв квартиру FTTB")
    "СуперПро"      -> TariffDetails("до 500 Мбит/с", "Безлимитный", "1 200 руб./мес","Скорость 500 Мбит/с\nТехнология xPON до дома\nв квартиру FTTB")
    "СуперМакс"     -> TariffDetails("до 1 Гбит/с",   "Безлимитный", "1 500 руб./мес","Скорость 1 Гбит/с\nТехнология xPON до дома\nв квартиру FTTB")
    else            -> TariffDetails("до 100 Мбит/с", "Безлимитный", "500 руб./мес",  "Скорость 100 Мбит/с\nТехнология xPON до дома\nв квартиру FTTB")
}

@Composable
fun TariffDetailScreen(
    tariffName: String,
    onBackClick: () -> Unit,
    onConnectClick: () -> Unit
) {
    val details = getTariffDetails(tariffName)

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Primary)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                MainTopBar(
                    onProfileClick = {},
                    onChatClick = {},
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

                // Детальная карточка тарифа
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"$tariffName\"",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            textAlign = TextAlign.Center
                        )
                        if (details.description.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = details.description,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = details.price,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
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
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Примечание к тарифу",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 16.dp)
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = "Заключить договор",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConnectClick() }
                    .padding(vertical = 16.dp)
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(Modifier.height(12.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = 1,
                onMainClick = onBackClick,
                onTariffsClick = onBackClick,
                onServicesClick = {}
            )
        }
    }
}
