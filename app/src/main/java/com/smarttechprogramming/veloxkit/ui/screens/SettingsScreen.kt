package com.smarttechprogramming.veloxkit.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttechprogramming.veloxkit.ui.theme.CyberBackground
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreen
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDark
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDim
import com.smarttechprogramming.veloxkit.ui.theme.CyberSurface
import com.smarttechprogramming.veloxkit.viewmodel.AiViewModel
import com.smarttechprogramming.veloxkit.viewmodel.SnippetsViewModel

@Composable
fun SettingsScreen(
    aiViewModel: AiViewModel,
    snippetsViewModel: SnippetsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentApiKey by aiViewModel.apiKeyFlow.collectAsState()

    var showKeyDialog by remember { mutableStateOf(false) }
    var showClearSnippetsDialog by remember { mutableStateOf(false) }
    var showClearChatDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Main Screen Header
        Text(
            text = "SETTINGS",
            color = CyberGreen,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Settings Rows Container
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberGreen.copy(alpha = 0.15f)) // Thin grid borders
        ) {
            // Row 1: Theme (Locked)
            SettingsRow(
                icon = Icons.Default.Lock,
                label = "Theme",
                valueText = "Dark Cyberpunk",
                onClick = {
                    Toast.makeText(context, "Veloxkit is permanently dark cyberpunk themed.", Toast.LENGTH_SHORT).show()
                }
            )

            // Row 2: AI API Key
            val keyDisplay = if (currentApiKey.trim().isEmpty()) "Not configured" else "••••••••••••"
            SettingsRow(
                icon = Icons.Default.VpnKey,
                label = "AI API Key",
                valueText = keyDisplay,
                onClick = { showKeyDialog = true }
            )

            // Row 3: Clear Snippets
            SettingsRow(
                icon = Icons.Default.DeleteForever,
                label = "Clear Snippet Vault",
                valueText = "Erase Room DB",
                onClick = { showClearSnippetsDialog = true }
            )

            // Row 4: Clear Chat History
            SettingsRow(
                icon = Icons.Default.Chat,
                label = "Clear Chat History",
                valueText = "Reset logs",
                onClick = { showClearChatDialog = true }
            )

            // Row 5: About
            SettingsRow(
                icon = Icons.Default.Info,
                label = "About",
                valueText = "System specifications",
                onClick = { showAboutDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(100.dp)) // Bottom padding navigation bar offset

        // Key Editor Dialog
        if (showKeyDialog) {
            var tempKey by remember { mutableStateOf(currentApiKey) }
            AlertDialog(
                onDismissRequest = { showKeyDialog = false },
                title = {
                    Text(
                        text = "GEMINI AI API KEY",
                        color = CyberGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Set your personal Google Gemini API Key. Keys are stored locally inside Encrypted DataStore nodes.",
                            color = CyberGreenDark,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = tempKey,
                            onValueChange = { tempKey = it },
                            placeholder = { Text("AI API KEY...", color = CyberGreenDark) },
                            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberGreen,
                                unfocusedBorderColor = CyberGreenDim,
                                cursorColor = CyberGreen,
                                focusedTextColor = CyberGreen,
                                unfocusedTextColor = CyberGreen
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                        onClick = {
                            aiViewModel.saveApiKey(tempKey)
                            showKeyDialog = false
                            Toast.makeText(context, "API Key saved successfully", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("SAVE", color = CyberBackground, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        border = BorderStroke(1.dp, CyberGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                        onClick = { showKeyDialog = false }
                    ) {
                        Text("CANCEL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = CyberSurface,
                modifier = Modifier.border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            )
        }

        // Clear Snippets Confirmation
        if (showClearSnippetsDialog) {
            AlertDialog(
                onDismissRequest = { showClearSnippetsDialog = false },
                title = {
                    Text(
                        text = "CLEAR VAULT?",
                        color = Color(0xFFFF3333),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        text = "This action will permanently delete ALL snippets stored inside your local Room Vault. This operation is completely irreversible.",
                        color = CyberGreen,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3333)),
                        onClick = {
                            snippetsViewModel.clearAllSnippets()
                            showClearSnippetsDialog = false
                            Toast.makeText(context, "Snippet Vault cleared", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("ERASE VAULT", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        border = BorderStroke(1.dp, CyberGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                        onClick = { showClearSnippetsDialog = false }
                    ) {
                        Text("CANCEL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = CyberSurface,
                modifier = Modifier.border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            )
        }

        // Clear Chat History Confirmation
        if (showClearChatDialog) {
            AlertDialog(
                onDismissRequest = { showClearChatDialog = false },
                title = {
                    Text(
                        text = "CLEAR CHAT LOGS?",
                        color = Color(0xFFFF3333),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to permanently delete all terminal messages with Veloxkit AI?",
                        color = CyberGreen,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3333)),
                        onClick = {
                            aiViewModel.clearChatHistory()
                            showClearChatDialog = false
                            Toast.makeText(context, "Chat history cleared", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("ERASE LOGS", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        border = BorderStroke(1.dp, CyberGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                        onClick = { showClearChatDialog = false }
                    ) {
                        Text("CANCEL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = CyberSurface,
                modifier = Modifier.border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            )
        }

        // About dialog
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = {
                    Text(
                        text = "VELOXKIT INTERNET CORE",
                        color = CyberGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "App: Veloxkit v1.0",
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Brand: Smart Tech Programming",
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Powered by: Veloxterm",
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Developer: Smart Tech Programming",
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                        
                        Divider(color = CyberGreen.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))
                        
                        Text(
                            text = "A modern terminal toolkit, code snippet utility, and intelligence-powered android developer companion optimized for offline-first terminal pipelines.",
                            color = CyberGreenDark,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                        onClick = { showAboutDialog = false }
                    ) {
                        Text("TERMINATE DIALOG", color = CyberBackground, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                },
                containerColor = CyberSurface,
                modifier = Modifier.border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    label: String,
    valueText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = CyberGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                color = CyberGreen,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = valueText,
                color = CyberGreenDark,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Expand item",
                tint = CyberGreenDark,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
