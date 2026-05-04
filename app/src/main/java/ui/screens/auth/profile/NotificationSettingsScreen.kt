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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.SystemNotification
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var smsNotifications by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(false) }
    var pushNotifications by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "← Назад",
            fontSize = 14.sp,
            color = Primary,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Настройка уведомлений",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // === Настройки ===
        NotificationSwitchItem(
            title = "Уведомления о балансе",
            description = "SMS при низком балансе",
            checked = smsNotifications,
            onCheckedChange = { smsNotifications = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationSwitchItem(
            title = "Уведомления на почту",
            description = "Акты и документы на email",
            checked = emailNotifications,
            onCheckedChange = { emailNotifications = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationSwitchItem(
            title = "Push-уведомления",
            description = "Уведомления в приложении",
            checked = pushNotifications,
            onCheckedChange = { pushNotifications = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Сохранить", fontSize = 16.sp)
        }

        // === Системные уведомления от сервера ===
        if (!uiState.isLoading && uiState.notifications.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Системные уведомления",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            uiState.notifications.forEach { notification ->
                SystemNotificationCard(notification)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Primary
            )
        }
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 13.sp, color = Color.Gray)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Primary,
                    checkedTrackColor = Primary.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun SystemNotificationCard(notification: SystemNotification) {
    val typeColor = when (notification.type) {
        "Warning" -> Color(0xFFF57C00)
        "Error" -> Color.Red
        else -> Primary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = typeColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notification.body, fontSize = 13.sp, color = TextSecondary)
        }
    }
}
