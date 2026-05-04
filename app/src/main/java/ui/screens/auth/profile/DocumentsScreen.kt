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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
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
        DocumentItem("Договор №12345",          "01.01.2024", "PDF"),
        DocumentItem("Акт за декабрь 2025",     "20.12.2025", "PDF"),
        DocumentItem("Акт за ноябрь 2025",      "20.11.2025", "PDF"),
        DocumentItem("Акт за октябрь 2025",     "20.10.2025", "PDF"),
        DocumentItem("Счёт на оплату",          "01.12.2025", "PDF")
    )

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
            text = "Документы",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Договоры, акты и счета",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ── Список документов ─────────────────────────────────────────────────
        documents.forEach { doc ->
            DocumentCard(doc)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun DocumentCard(document: DocumentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: открыть/скачать */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    text = document.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = document.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "• ${document.type}",
                        fontSize = 12.sp,
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Text(text = "📄", fontSize = 22.sp)
        }
    }
}
