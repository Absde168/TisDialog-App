package ui.screens.auth.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import data.model.ChatMessage
import ui.components.AppBottomNav
import ui.components.MainTopBar
import ui.theme.ErrorRed
import ui.theme.Primary
import ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

private val topicButtons = listOf(
    "О моем тарифе",
    "Отключить услугу",
    "Расходы и платежи"
)

@Composable
fun ChatSupportScreen(
    onProfileClick: () -> Unit,
    onMainClick: () -> Unit,
    onTariffsClick: () -> Unit,
    onServicesClick: () -> Unit,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.factory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Box(modifier = Modifier.fillMaxWidth().background(Primary)) {
            MainTopBar(
                onProfileClick = onProfileClick,
                onChatClick = { },
                contentColor = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Чат поддержки",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (uiState.messages.isEmpty()) {
                WelcomeSection(userName = uiState.userName)
                Spacer(Modifier.height(16.dp))
                topicButtons.forEach { topic ->
                    TopicButton(
                        label = topic,
                        onClick = { viewModel.sendMessage(topic) }
                    )
                    Spacer(Modifier.height(10.dp))
                }
                Spacer(Modifier.weight(1f))
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.messages, key = { it.id }) { message ->
                        MessageBubble(message)
                    }
                }
            }

            uiState.error?.let {
                Text(
                    text = it,
                    color = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f).height(56.dp),
                    placeholder = {
                        Text(
                            text = "Напишите сообщение...",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (messageText.isNotBlank()) {
                                viewModel.sendMessage(messageText)
                                messageText = ""
                            }
                        }
                    ),
                    singleLine = true,
                    enabled = !uiState.isSending
                )
                Spacer(Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    enabled = messageText.isNotBlank() && !uiState.isSending,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Primary)
                ) {
                    if (uiState.isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "➤", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppBottomNav(
                activeTab = 0,
                onMainClick = onMainClick,
                onTariffsClick = onTariffsClick,
                onServicesClick = onServicesClick
            )
        }
    }
}

@Composable
private fun WelcomeSection(userName: String) {
    val greeting = if (userName.isNotBlank()) "Добрый день, $userName!" else "Добрый день!"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Помощник Александра",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = greeting,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Выберите тему или кратко\nопишите вопрос:",
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TopicButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isFromUser = message.isFromUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isFromUser) 16.dp else 4.dp,
                bottomEnd = if (isFromUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isFromUser) Primary else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(if (isFromUser) 0.dp else 1.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = if (isFromUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatTime(message.timestamp),
                    fontSize = 11.sp,
                    color = if (isFromUser) Color.White.copy(alpha = 0.65f)
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatTime(timestamp: String): String {
    if (timestamp.isEmpty()) return ""
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(parser.parse(timestamp) ?: return "")
    } catch (e: Exception) {
        timestamp.take(5)
    }
}
