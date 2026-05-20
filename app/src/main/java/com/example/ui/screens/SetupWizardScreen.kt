package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberGreenDark
import com.example.ui.theme.CyberGreenDim
import com.example.ui.theme.CyberSurface

data class SetupStep(
    val title: String,
    val description: String,
    val command: String
)

@Composable
fun SetupWizardScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val steps = listOf(
        SetupStep(
            title = "UPDATE REPOSITORIES",
            description = "Ensure Termux repository lists and local database maps are synchronized to fetch the latest builds.",
            command = "pkg update -y && pkg upgrade -y"
        ),
        SetupStep(
            title = "GRANT FILE ACCESS",
            description = "Bridges permission layers between Termux sandboxes and physical internal phone storage systems.",
            command = "termux-setup-storage"
        ),
        SetupStep(
            title = "INSTALL DEVELOPER ENGINES",
            description = "Installs core programming frameworks (Git control node, Python interpreter, and NodeJS runtimes) in one batch.",
            command = "pkg install git python nodejs -y"
        ),
        SetupStep(
            title = "ACTIVATE PYTHON VERTICAL SANDBOX",
            description = "Prevents globally distributed scripting libraries from clashing inside core package structures.",
            command = "python3 -m venv ~/dev_env && source ~/dev_env/bin/activate"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Screen Header
        Text(
            text = "SETUP WIZARD",
            color = CyberGreen,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Sequential guidelines facilitating standard sandbox setups on virtual targets.",
            color = CyberGreenDark,
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Steps List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(steps) { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left numerical node indicator
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CyberSurface)
                                .border(1.dp, CyberGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = CyberGreen,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        
                        // Line spacer between indicators (simulated line)
                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(110.dp)
                                    .background(CyberGreenDark.copy(alpha = 0.3f))
                            )
                        }
                    }

                    // Step Detail card
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberSurface)
                            .border(1.dp, CyberGreen, RoundedCornerShape(8.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = step.title,
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.description,
                            color = CyberGreenDim,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Clickable command terminal box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF020902))
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(step.command))
                                    Toast.makeText(context, "Command copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$ " + step.command,
                                color = CyberGreen,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy command icon",
                                tint = CyberGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // navigation spacer offset
            }
        }
    }
}
