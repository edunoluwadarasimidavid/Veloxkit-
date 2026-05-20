package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FolderZip
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
import com.example.ui.theme.CyberGreenDim
import com.example.ui.theme.CyberSurface

data class ProjectTemplate(
    val name: String,
    val description: String,
    val setupScript: String,
    val buildScript: String
)

@Composable
fun ProjectsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val templates = listOf(
        ProjectTemplate(
            name = "KOTLIN COMPOSE STARTER",
            description = "Creates a lightweight clean Kotlin Jetpack Compose directory layout.",
            setupScript = "mkdir -p app/src/main/java/com/example/ui && touch app/build.gradle.kts MainActivity.kt",
            buildScript = "./gradlew assembleDebug"
        ),
        ProjectTemplate(
            name = "TERMUX PYTHON SERVER",
            description = "Deploys a lightweight Flask restful server running natively inside Termux environment.",
            setupScript = "pip install flask && echo -e 'from flask import Flask\\napp = Flask(__name__)\\n@app.route(\"/\")\\ndef run(): return \"OK\"\\napp.run(port=8080)' > server.py",
            buildScript = "python3 server.py"
        ),
        ProjectTemplate(
            name = "NODE EXPRESS API ENGINE",
            description = "Installs node packages & bootstraps express restful endpoints with responsive body parsers.",
            setupScript = "npm init -y && npm install express body-parser && echo -e 'const express = require(\"express\");\\nconst app = express();\\napp.get(\"/\", (req,res)=>res.send(\"OK\"));\\napp.listen(3000);' > index.js",
            buildScript = "node index.js"
        ),
        ProjectTemplate(
            name = "GIT REPO SCRATCHPAD",
            description = "Initializes repository branches, staging, committing, and connecting upstream origins.",
            setupScript = "git init && git checkout -b main && echo '# Dev' > README.md && git add README.md && git commit -m 'init'",
            buildScript = "git remote add origin [url] && git push -u origin main"
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
            text = "PROJECT LAUNCHER",
            color = CyberGreen,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Micro-environments & build automation script generation nodes.",
            color = CyberGreenDim,
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Templates LazyColumn
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(templates) { template ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurface)
                        .border(1.dp, CyberGreen, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = template.name,
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = "Project icon",
                            tint = CyberGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = template.description,
                        color = CyberGreenDim,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Setup Terminal script container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF050E05))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "# SETUP TERMINAL COMMANDS",
                            color = CyberGreenDim.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = template.setupScript,
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Launch run terminal script container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF050E05))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "# RUN PIPELINES",
                            color = CyberGreenDim.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = template.buildScript,
                            color = CyberGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Copy action button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                val combinedScript = template.setupScript + "\n" + template.buildScript
                                clipboardManager.setText(AnnotatedString(combinedScript))
                                Toast.makeText(context, "Full project launcher script copied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            border = BorderStroke(1.dp, CyberGreen),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy script icon",
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Copy Script",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // bottom padding
            }
        }
    }
}
