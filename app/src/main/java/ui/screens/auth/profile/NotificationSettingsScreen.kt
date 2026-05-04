package ui.screens.auth.profile

import androidx.compose.foundation.background
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.SystemNotification
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var smsEnabled  by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(false) }
    var pushEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick, title = "Уведомления")

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Настройка уведомлений",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        // ── Переключатели ─────────────────────────────────────────────────────
        NotificationSwitchItem(
            title = "Уведомления о балансе",
            description = "SMS при низком балансе",
            checked = smsEnabled,
            onCheckedChange = { smsEnabled = it }
        )
        Spacer(Modifier.height(10.dp))

        NotificationSwitchItem(
            title = "Уведомления на почту",
            description = "Акты и документы на email",
            checked = emailEnabled,
            onCheckedChange = { emailEnabled = it }
        )
        Spacer(Modifier.height(10.dp))

        NotificationSwitchItem(
            title = "Push-уведомления",
            description = "Уведомления в приложении",
            checked = pushEnabled,
            onCheckedChange = { pushEnabled = it }
        )

        Spacer(Modifier.height(24.dp))

        // ── Сохранить ─────────────────────────────────────────────────────────
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text(
                text = "Сохранить",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // ── Системные уведомления от сервера ─────────────────────────────────
        if (uiState.isLoading) {
            Spacer(Modifier.height(24.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Primary
            )
        } else if (uiState.notifications.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Системные уведомления",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(10.dp))
            uiState.notifications.forEach { notification ->
                SystemNotificationCard(notification)
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun NotificationSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
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
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCCCCCC)
                )
            )
        }
    }
}

@Composable
private fun SystemNotificationCard(notification: SystemNotification) {
    val typeColor = when (notification.type) {
        "Warning" -> Color(0xFFF57C00)
        "Error"   -> ErrorRed
        else      -> Primary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = notification.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = typeColor
            )
            Spacer(Modifier.height(4.dp))
            Text(text = notification.body, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
