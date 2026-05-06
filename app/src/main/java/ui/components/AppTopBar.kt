package ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tisdialogfirst.R
import ui.theme.Primary

/**
 * Главная шапка: профиль | Лого | чат
 * Синий фон  → белый логотип (без фильтра)
 * Белый фон  → синий логотип (ColorFilter.tint Primary)
 */
@Composable
fun MainTopBar(
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = Primary
) {
    val onDarkBg = contentColor == Color.White
    val logoTint = if (onDarkBg) ColorFilter.tint(Color.White) else ColorFilter.tint(Primary)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onProfileClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.PersonOutline,
                contentDescription = "Профиль",
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.dialog_logo),
            contentDescription = "ДИАЛОГ",
            contentScale = ContentScale.Fit,
            colorFilter = logoTint,
            modifier = Modifier
                .height(36.dp)
                .widthIn(max = 130.dp)
        )

        IconButton(onClick = onChatClick, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.ChatBubbleOutline,
                contentDescription = "Чат",
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Шапка вложенных экранов: ← Назад | Лого синий | отступ
 * Всегда на белом фоне → логотип синий
 */
@Composable
fun DetailTopBar(
    onBackClick: () -> Unit,
    title: String = "ДИАЛОГ",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
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

        if (title == "ДИАЛОГ") {
            Image(
                painter = painterResource(id = R.drawable.dialog_logo),
                contentDescription = "ДИАЛОГ",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Primary),
                modifier = Modifier
                    .height(32.dp)
                    .widthIn(max = 120.dp)
            )
        } else {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }

        Spacer(Modifier.width(72.dp))
    }
}

/**
 * Нижняя навигация. activeTab: 0=Главная, 1=Тарифы, 2=Услуги, -1=нет активной
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
            .navigationBarsPadding()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Primary),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEachIndexed { i, label ->
            val isActive = i == activeTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .then(
                        if (isActive)
                            Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.background)
                        else
                            Modifier.clickable { clicks[i]() }
                    )
                    .then(if (isActive) Modifier.clickable { clicks[i]() } else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) Primary else Color.White
                )
            }
        }
    }
}
