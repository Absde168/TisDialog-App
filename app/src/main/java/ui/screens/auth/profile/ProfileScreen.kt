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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.theme.Primary
import ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onContactsClick: () -> Unit,
    onSupportClick: () -> Unit,
    onDocumentsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) onLogoutClick()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выход") },
            text = { Text("Вы уверены, что хотите выйти из аккаунта?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                }) {
                    Text("Выйти", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

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
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === КАРТОЧКА ПОЛЬЗОВАТЕЛЯ ===
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        color = Primary
                    )
                } else {
                    val user = uiState.user
                    Text(
                        text = user?.fullName ?: "—",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Логин: ${user?.login ?: "—"}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Телефон: ${user?.phone ?: "—"}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Email: ${user?.email ?: "—"}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === МЕНЮ ПРОФИЛЯ ===
        ProfileMenuItem(title = "Мои контакты", onClick = onContactsClick)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileMenuItem(title = "Обратиться в поддержку", onClick = onSupportClick)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileMenuItem(title = "Документы", subtitle = "Договор, акты и т.п.", onClick = onDocumentsClick)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileMenuItem(title = "Настройка уведомлений", onClick = onNotificationsClick)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileMenuItem(title = "Помощь / FAQ", onClick = onHelpClick)

        Spacer(modifier = Modifier.weight(1f))

        // === КНОПКА ВЫЙТИ ===
        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Выйти из аккаунта", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        BottomNavigationProfile(
            onMainClick = onBackClick,
            onTariffsClick = { },
            onServicesClick = { }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (subtitle != null) 80.dp else 60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle, fontSize = 13.sp, color = TextSecondary)
                }
            }
            Text(text = "→", fontSize = 18.sp, color = TextSecondary)
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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onMainClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Главная", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onTariffsClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Тарифы", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onServicesClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("Услуги", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}
