package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Primary

/** Главная шапка: аватар | ДИАЛОГ | чат */
@Composable
fun MainTopBar(
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onProfileClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.PersonOutline,
                contentDescription = "Профиль",
                tint = Primary,
                modifier = Modifier.size(26.dp)
            )
        }
        Text(text = "ДИАЛОГ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary)
        IconButton(onClick = onChatClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.ChatBubbleOutline,
                contentDescription = "Чат",
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/** Шапка для вложенных экранов: ← Назад | Заголовок | пустышка */
@Composable
fun DetailTopBar(
    onBackClick: () -> Unit,
    title: String = "ДИАЛОГ",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Назад",
                tint = Primary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(2.dp))
            Text(text = "Назад", fontSize = 14.sp, color = Primary)
        }
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary)
        Spacer(Modifier.width(72.dp))
    }
}

/**
 * Нижняя навигация. activeTab: 0=Главная, 1=Тарифы, 2=Услуги
 */
@Composable
fun AppBottomNav(
    activeTab: Int = 0,
    onMainClick: () -> Unit,
    onTariffsClick: () -> Unit,
    onServicesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Главная", "Тарифы", "Услуги")
    val clicks = listOf(onMainClick, onTariffsClick, onServicesClick)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Primary),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEachIndexed { i, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .then(
                        if (i == activeTab)
                            Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                        else
                            Modifier.clickable { clicks[i]() }
                    )
                    .then(if (i == activeTab) Modifier.clickable { clicks[i]() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (i == activeTab) Primary else Color.White
                )
            }
        }
    }
}
