package com.smarttechprogramming.veloxkit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttechprogramming.veloxkit.data.models.ChatMessageEntity
import com.smarttechprogramming.veloxkit.ui.theme.CyberBackground
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreen
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDim
import com.smarttechprogramming.veloxkit.ui.theme.CyberSurface

@Composable
fun ChatBubble(
    message: ChatMessageEntity,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == "user"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        if (isUser) {
            // User Message Bubble
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(CyberGreen)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    color = CyberBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        } else {
            // AI Response Card
            val (command, explanation) = parseAiMessage(message.text)

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .background(CyberSurface)
                    .border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    if (command.isNotEmpty()) {
                        Text(
                            text = command,
                            color = CyberGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                        if (explanation.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                    if (explanation.isNotEmpty()) {
                        Text(
                            text = explanation,
                            color = CyberGreenDim,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

// Parses response from Gemini into a command portion and explanation portion
private fun parseAiMessage(text: String): Pair<String, String> {
    val cleanedText = text.replace("```bash", "").replace("```sh", "").replace("```", "").trim()
    
    // Split on first newline to distinguish between command line and description
    val lines = cleanedText.lines()
    if (lines.isEmpty()) return Pair("", "")

    val firstLine = lines.first().trim()

    // If there is only one line, determine if it's a command or explanation
    if (lines.size == 1) {
        return if (isCommand(firstLine)) {
            Pair(firstLine, "")
        } else {
            Pair("", firstLine)
        }
    }

    val remaining = lines.drop(1).joinToString("\n").trim()
    return Pair(firstLine, remaining)
}

private fun isCommand(line: String): Boolean {
    val l = line.lowercase()
    return l.startsWith("pkg") || l.startsWith("git") || l.startsWith("pip") ||
           l.startsWith("npm") || l.startsWith("node") || l.startsWith("python") ||
           l.startsWith("termux") || l.contains(" -")
}
