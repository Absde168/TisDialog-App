package ui.screens.auth.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import ui.components.AppBottomNav
import ui.components.DetailTopBar
import ui.theme.BackgroundLight
import ui.theme.ErrorRed
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
    onTariffsClick: () -> Unit = {},
    onServicesClick: () -> Unit = {},
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
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = "Выход из аккаунта",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Вы уверены, что хотите выйти?",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; viewModel.logout() }) {
                    Text("Выйти", color = ErrorRed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Отмена", color = Primary, fontSize = 14.sp)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Шапка ─────────────────────────────────────────────────────────────
        DetailTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(16.dp))

        // ── Карточка пользователя ─────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterVertically),
                        color = Primary,
                        strokeWidth = 3.dp
                    )
                } else {
                    // Аватар с инициалами
                    val initials = uiState.user?.let { u ->
                        val f = u.firstName?.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                        val l = u.lastName?.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                        (f + l).ifBlank { u.login.take(2).uppercase() }
                    } ?: "?"

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = uiState.user?.fullName ?: "—",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = uiState.user?.login ?: "—",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (!uiState.user?.phone.isNullOrBlank()) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = uiState.user?.phone ?: "",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        uiState.error?.let {
            Spacer(Modifier.height(6.dp))
            Text(text = it, color = ErrorRed, fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        // ── Меню профиля ─────────────────────────────────────────────────────
        ProfileMenuItem(title = "Мои контакты", onClick = onContactsClick)
        Spacer(Modifier.height(8.dp))
        ProfileMenuItem(title = "Обратиться в поддержку", onClick = onSupportClick)
        Spacer(Modifier.height(8.dp))
        ProfileMenuItem(
            title = "Документы",
            subtitle = "Договор, акты и т.п.",
            onClick = onDocumentsClick
        )
        Spacer(Modifier.height(8.dp))
        ProfileMenuItem(title = "Настройка уведомлений", onClick = onNotificationsClick)
        Spacer(Modifier.height(8.dp))
        ProfileMenuItem(title = "Помощь / FAQ", onClick = onHelpClick)

        Spacer(Modifier.height(24.dp))

        // ── Кнопка выхода ────────────────────────────────────────────────────
        OutlinedButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ErrorRed),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
        ) {
            Text(
                text = "Выйти из аккаунта",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(12.dp))

        // ── Нижняя навигация ─────────────────────────────────────────────────
        AppBottomNav(
            activeTab = -1,
            onMainClick = onBackClick,
            onTariffsClick = onTariffsClick,
            onServicesClick = onServicesClick
        )
        Spacer(Modifier.height(16.dp))
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
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
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
                if (subtitle != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
