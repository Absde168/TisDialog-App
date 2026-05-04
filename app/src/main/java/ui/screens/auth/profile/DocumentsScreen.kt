package ui.screens.auth.profile

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

data class DocumentItem(
    val name: String,
    val date: String,
    val type: String
)

@Composable
fun DocumentsScreen(
    onBackClick: () -> Unit
) {
    val documents = listOf(
        DocumentItem("Договор №12345", "01.01.2024", "PDF"),
        DocumentItem("Акт за декабрь 2025", "20.12.2025", "PDF"),
        DocumentItem("Акт за ноябрь 2025", "20.11.2025", "PDF"),
        DocumentItem("Акт за октябрь 2025", "20.10.2025", "PDF"),
        DocumentItem("Счёт на оплату", "01.12.2025", "PDF")
    )

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

        // === ЗАГОЛОВОК ===
        Text(
            text = "Документы",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Договоры, акты и счета",
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // === СПИСОК ДОКУМЕНТОВ ===
        documents.forEach { doc ->
            DocumentCard(doc)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === НИЖНЯЯ НАВИГАЦИЯ ===
        BottomNavigationProfile(
            onMainClick = onBackClick,
            onTariffsClick = { },
            onServicesClick = { }
        )
    }
}

@Composable
private fun DocumentCard(document: DocumentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { /* TODO: Открыть документ */ },
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
                    text = document.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = document.date,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${document.type}",
                        fontSize = 13.sp,
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = "📄",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun BottomNavigationProfile(
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

        Text(
            text = "Тарифы",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .clickable { onTariffsClick() }
        )

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