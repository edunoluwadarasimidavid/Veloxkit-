package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
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
    val columnStates = remember { mutableStateListOf<MatrixColumn>() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        if (width > 0 && height > 0 && columnStates.isEmpty()) {
            val charSize = 25f
            val columnsCount = (width / charSize).toInt().coerceAtLeast(1)
            for (i in 0 until columnsCount) {
                columnStates.add(
                    MatrixColumn(
                        x = i * charSize,
                        y = Random.nextFloat() * -height - 100f,
                        speed = Random.nextFloat() * 10f + 5f,
                        chars = List(12) { characters[Random.nextInt(characters.size)] }
                    )
                )
            }
        }

        // Render characters directly using Native Canvas for peak performance
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
    }

    LaunchedEffect(key1 = columnStates) {
        val delayMillis = 24L
        while (true) {
            delay(delayMillis)
            for (i in columnStates.indices) {
                val col = columnStates[i]
                var newY = col.y + col.speed
                val listSize = col.chars.size
                
                // Reset when column falls off screen
                if (newY - (listSize * 30f) > 1600f) {
                    newY = Random.nextFloat() * -200f
                }
                
                // 5% chance to mutate characters for biological glitch effect
                val updatedChars = if (Random.nextFloat() > 0.95f) {
                    List(listSize) { characters[Random.nextInt(characters.size)] }
                } else {
                    col.chars
                }
                
                columnStates[i] = col.copy(y = newY, chars = updatedChars)
            }
        }
    }
}
