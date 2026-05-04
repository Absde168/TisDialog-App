package ui.screens.auth.profile

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
import ui.theme.Primary
import ui.theme.TextSecondary

data class FAQItem(
    val question: String,
    val answer: String
)

@Composable
fun HelpFAQScreen(
    onBackClick: () -> Unit
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
                text = "Помощь / FAQ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(32.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === СПИСОК ВОПРОСОВ ===
        faqItems.forEachIndexed { index, item ->
            FAQCard(
                item = item,
                isExpanded = expandedIndex == index,
                onExpand = { expandedIndex = if (expandedIndex == index) -1 else index }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === КНОПКА В ПОДДЕРЖКУ ===
        Button(
            onClick = { /* TODO: Открыть чат */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Написать в поддержку", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(24.dp))
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (isExpanded) "▼" else "▶",
                    fontSize = 14.sp,
                    color = Primary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.answer,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}