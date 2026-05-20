package com.smarttechprogramming.veloxkit.ui.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.delay
import kotlin.random.Random

data class MatrixColumn(
    val x: Float,
    val y: Float,
    val speed: Float,
    val chars: List<Char>
)

@Composable
fun MatrixRainCanvas(modifier: Modifier = Modifier) {
    val characters = remember { 
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#@$%&<>*+".toCharArray() 
    }
    var columnStates by remember { mutableStateOf<List<MatrixColumn>>(emptyList()) }
    var canvasHeight by remember { mutableStateOf(1600f) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                if (size.width > 0 && size.height > 0 && columnStates.isEmpty()) {
                    val width = size.width.toFloat()
                    val height = size.height.toFloat()
                    canvasHeight = height
                    val charSize = 25f
                    val columnsCount = (width / charSize).toInt().coerceAtLeast(1)
                    val newList = mutableListOf<MatrixColumn>()
                    for (i in 0 until columnsCount) {
                        newList.add(
                            MatrixColumn(
                                x = i * charSize,
                                y = Random.nextFloat() * -height - 100f,
                                speed = Random.nextFloat() * 10f + 5f,
                                chars = List(12) { characters[Random.nextInt(characters.size)] }
                            )
                        )
                    }
                    columnStates = newList
                }
            }
    ) {
        val height = size.height
        // Render characters directly using Native Canvas for peak performance
        try {
            columnStates.forEach { col ->
                col.chars.forEachIndexed { index, char ->
                    val opacity = (col.chars.size - index).toFloat() / col.chars.size
                    val charY = col.y + (index * 30f)
                    
                    // Only render if it's visible on screen
                    if (charY in -50f..(height + 50f)) {
                        drawContext.canvas.nativeCanvas.drawText(
                            char.toString(),
                            col.x,
                            charY,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.argb(
                                    (opacity * 255).toInt().coerceIn(0, 255),
                                    0,
                                    255,
                                    65
                                )
                                textSize = 28f
                                typeface = android.graphics.Typeface.MONOSPACE
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MatrixRainCanvas", "Concurrent draw exception caught", e)
        }
    }

    LaunchedEffect(key1 = columnStates.isNotEmpty()) {
        if (columnStates.isEmpty()) return@LaunchedEffect
        val delayMillis = 32L // Steady 30 FPS VSync updates
        while (true) {
            delay(delayMillis)
            columnStates = columnStates.map { col ->
                var newY = col.y + col.speed
                val listSize = col.chars.size
                
                // Reset when column falls off screen
                if (newY - (listSize * 30f) > canvasHeight) {
                    newY = Random.nextFloat() * -200f
                }
                
                // 5% chance to mutate characters for biological glitch effect
                val updatedChars = if (Random.nextFloat() > 0.95f) {
                    List(listSize) { characters[Random.nextInt(characters.size)] }
                } else {
                    col.chars
                }
                
                col.copy(y = newY, chars = updatedChars)
            }
        }
    }
}
