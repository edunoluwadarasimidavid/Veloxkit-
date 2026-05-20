package com.example.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Forced Dark Scheme for Cyberpunk aesthetics
private val CyberColorScheme = darkColorScheme(
    primary = CyberGreen,
    secondary = CyberGreenDim,
    background = CyberBackground,
    surface = CyberSurface,
    error = CyberError,
    onPrimary = CyberBackground,
    onSecondary = CyberBackground,
    onBackground = CyberGreenDim,
    onSurface = CyberGreen
)

@Composable
fun VeloxkitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}

// Custom glow modifier
fun Modifier.cyberGlow(
    color: Color = CyberGreen,
    borderRadius: Dp = 8.dp,
    glowRadius: Dp = 4.dp
): Modifier = this
    .drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                val frameworkPaint = asFrameworkPaint()
                frameworkPaint.color = color.copy(alpha = 0.3f).toArgb()
                frameworkPaint.setShadowLayer(
                    glowRadius.toPx(),
                    0f,
                    0f,
                    color.copy(alpha = 0.6f).toArgb()
                )
            }
            canvas.drawRoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint = paint
            )
        }
    }
    .border(1.dp, color, RoundedCornerShape(borderRadius))
