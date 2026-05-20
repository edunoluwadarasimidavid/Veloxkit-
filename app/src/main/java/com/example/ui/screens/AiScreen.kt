package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChatBubble
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberGreenDark
import com.example.ui.theme.CyberSurface
import com.example.viewmodel.AiViewModel

@Composable
fun AiScreen(
    viewModel: AiViewModel,
    modifier: Modifier = Modifier
) {
    val chatFlow by viewModel.chatFlow.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val apiKey by viewModel.apiKeyFlow.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Blinking cursor infinite transition
    val infiniteTransition = rememberInfiniteTransition(label = "BlinkingCursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CursorAlpha"
    )

    // Trigger connectivity checks periodically or on layout loads
    LaunchedEffect(key1 = chatFlow.size) {
        viewModel.checkConnectivity()
        if (chatFlow.isNotEmpty()) {
            listState.animateScrollToItem(chatFlow.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .statusBarsPadding()
    ) {
        // Offline Warning Banner
        if (isOffline) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE6A100))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚠ Offline Mode — AI unavailable",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }

        // Header Row with blinking cursor
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "VELOXKIT AI",
                color = CyberGreen,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 20.dp)
                    .alpha(cursorAlpha)
                    .background(CyberGreen)
            )
        }

        // Main Conversation Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            if (chatFlow.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TERMINAL READY",
                        color = CyberGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Initialize conversation query parameters. Specialize in Termux environments, Android, Git actions, Python setups, and Node.js runtimes.",
                        color = CyberGreenDark,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    if (apiKey.trim().isEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "⚠ Warning: Gemini API key resides unconfigured. Update setting nodes to activate companion responses.",
                            color = Color(0xFFFF3333),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(chatFlow) { message ->
                        ChatBubble(message = message)
                    }

                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                LoadingDots()
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp)) // Floating Bottom Row spacing
                    }
                }
            }
        }

        // Bottom input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 76.dp), // Nav bar offset padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CyberSurface),
                textStyle = LocalTextStyle.current.copy(
                    color = CyberGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                placeholder = {
                    Text(
                        text = "Ask AI...",
                        color = CyberGreenDark,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberGreen,
                    unfocusedBorderColor = CyberGreen,
                    focusedContainerColor = CyberSurface,
                    unfocusedContainerColor = CyberSurface,
                    cursorColor = CyberGreen
                ),
                maxLines = 4,
                singleLine = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Round Send Button
            val canSend = textInput.trim().isNotEmpty() && !isLoading
            IconButton(
                onClick = {
                    viewModel.sendMessage(textInput)
                    textInput = ""
                },
                enabled = canSend,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (canSend) CyberGreen else CyberGreenDark)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send prompt",
                    tint = CyberBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "DotsLoading")
    val dotCount = 3
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "AI thinking",
            color = CyberGreen,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
        for (i in 0 until dotCount) {
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 600, delayMillis = i * 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "DotScale$i"
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(CyberGreen.copy(alpha = scale))
            )
        }
    }
}
