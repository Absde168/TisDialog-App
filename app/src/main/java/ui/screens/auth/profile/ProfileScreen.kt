package ui.screens.auth.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import ui.components.MainTopBar
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
    onChatClick: () -> Unit = {},
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
        LogoutConfirmDialog(
            onConfirm = { showLogoutDialog = false; viewModel.logout() },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        MainTopBar(
            onProfileClick = {},
            onChatClick = onChatClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Primary),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            text = uiState.user?.fullName ?: "—",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "№ ЛС: ${uiState.user?.login ?: "—"}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        if (!uiState.user?.phone.isNullOrBlank()) {
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "Телефон: ${uiState.user?.phone}",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        if (!uiState.user?.email.isNullOrBlank()) {
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "Email: ${uiState.user?.email}",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            uiState.error?.let {
                Spacer(Modifier.height(6.dp))
                Text(text = it, color = ErrorRed, fontSize = 12.sp)
            }

            Spacer(Modifier.height(20.dp))

            ProfileMenuItem(title = "Мои Контакты", onClick = onContactsClick)
            ProfileMenuItem(title = "Обратиться в поддержку", onClick = onSupportClick)
            ProfileMenuItem(
                title = "Документы",
                subtitle = "Договор, акты и т.п",
                onClick = onDocumentsClick
            )
            ProfileMenuItem(title = "Настройка уведомлений", onClick = onNotificationsClick)
            ProfileMenuItem(title = "Помощь / FAQ", onClick = onHelpClick)

            Spacer(Modifier.height(28.dp))

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.5.dp, Primary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
            ) {
                Text(
                    text = "Выйти из аккаунта",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = -1,
                onMainClick = onBackClick,
                onTariffsClick = onTariffsClick,
                onServicesClick = onServicesClick
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 14.dp),
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
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
