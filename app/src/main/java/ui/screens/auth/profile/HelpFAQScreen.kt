package ui.screens.auth.profile

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
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.Primary
import ui.theme.TextSecondary

data class FAQItem(
    val question: String,
    val answer: String
)

@Composable
fun HelpFAQScreen(
    onBackClick: () -> Unit,
    onSupportClick: () -> Unit = {}
) {
    val faqItems = listOf(
        FAQItem(
            "Как оплатить услуги?",
            "Оплатить можно в приложении через кнопку «Оплатить» на главном экране. Доступны карты, СБП и счёт."
        ),
        FAQItem(
            "Как сменить тариф?",
            "Перейдите в раздел «Тарифы», выберите подходящий и нажмите «Подключить». Новый тариф активируется со следующего месяца."
        ),
        FAQItem(
            "Как подключить дополнительные услуги?",
            "В разделе «Услуги» вы можете включить или отключить дополнительные опции: VPN, Антивирус, Родительский контроль."
        ),
        FAQItem(
            "Что делать если нет интернета?",
            "Проверьте баланс, перезагрузите роутер. Если проблема не решена — напишите в чат поддержки."
        ),
        FAQItem(
            "Как получить документы?",
            "Все документы доступны в разделе «Документы». Их можно скачать в формате PDF."
        )
    )

    var expandedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick, title = "Помощь / FAQ")

        Spacer(Modifier.height(8.dp))

        // ── Список вопросов ───────────────────────────────────────────────────
        faqItems.forEachIndexed { index, item ->
            FAQCard(
                item = item,
                isExpanded = expandedIndex == index,
                onExpand = { expandedIndex = if (expandedIndex == index) -1 else index }
            )
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))

        // ── Кнопка в поддержку ────────────────────────────────────────────────
        Button(
            onClick = onSupportClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text(
                text = "Написать в поддержку",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FAQCard(
    item: FAQItem,
    isExpanded: Boolean,
    onExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpand() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.question,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (isExpanded) "▼" else "▶",
                    fontSize = 12.sp,
                    color = Primary
                )
            }

            if (isExpanded) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = item.answer,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
