package ui.screens.auth.balance

import androidx.compose.foundation.BorderStroke
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
import ui.theme.Primary
import ui.theme.TextSecondary

private data class BankItem(
    val name: String,
    val color: Color,
    val initial: String
)

private val russianBanks = listOf(
    BankItem("Сбербанк",      Color(0xFF21A038), "С"),
    BankItem("Т-Банк",        Color(0xFFFFDD2D), "Т"),
    BankItem("ВТБ",           Color(0xFF009FDF), "В"),
    BankItem("Альфа-Банк",    Color(0xFFEF3124), "А"),
    BankItem("Газпромбанк",   Color(0xFF003087), "Г"),
    BankItem("Почта Банк",    Color(0xFF005B9A), "П"),
    BankItem("Россельхозбанк",Color(0xFF009B3A), "Р"),
    BankItem("МТС Банк",      Color(0xFFE30611), "М"),
    BankItem("Совкомбанк",    Color(0xFF6D2077), "С"),
    BankItem("Райффайзен",    Color(0xFFFFEA00), "Р"),
)

@Composable
fun OtherBanksScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        DetailTopBar(onBackClick = onBackClick, title = "Другой банк")

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Выберите банк",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Оплата через приложение вашего банка",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            russianBanks.forEach { bank ->
                BankRow(bank = bank, onClick = { /* stub */ })
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BankRow(bank: BankItem, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // Цветной кружок с буквой
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(10.dp),
            color = bank.color
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = bank.initial,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Text(
            text = bank.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "›",
            fontSize = 20.sp,
            color = TextSecondary
        )
    }
}
