package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.MatrixRainCanvas
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberGreenDim
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.cyberGlow

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        // Core cyberpunk back layer
        MatrixRainCanvas(modifier = Modifier.fillMaxSize())

        // Semi-transparent overlay to keep content readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xE60A0A0A))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text(
                    text = "Veloxkit",
                    color = CyberGreen,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your Android Dev Companion",
                    color = CyberGreenDim,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }

            // Central Bento Grid of feature tiles
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(vertical = 24.dp)
            ) {
                // Row 1: Command Library (1x1) & Snippet Vault (1x1)
                item {
                    FeatureTile(
                        icon = Icons.Default.Terminal,
                        label = "Command Library",
                        onClick = { navController.navigate("commands") },
                        height = 110.dp
                    )
                }
                item {
                    FeatureTile(
                        icon = Icons.Default.Code,
                        label = "Snippet Vault",
                        onClick = { navController.navigate("snippets") },
                        height = 110.dp
                    )
                }
                // Row 2: Project Launcher (2x1, Wide highlighted tile)
                item(span = { GridItemSpan(2) }) {
                    FeatureTile(
                        icon = Icons.Default.Folder,
                        label = "Project Launcher",
                        onClick = { navController.navigate("projects") },
                        height = 90.dp,
                        description = "Templates & script generation nodes",
                        isWide = true
                    )
                }
                // Row 3: Setup Wizard (1x1) & AI Helper (1x1)
                item {
                    FeatureTile(
                        icon = Icons.Default.Build,
                        label = "Setup Wizard",
                        onClick = { navController.navigate("setup_wizard") },
                        height = 110.dp
                    )
                }
                item {
                    FeatureTile(
                        icon = Icons.Default.Psychology,
                        label = "AI Helper",
                        onClick = { navController.navigate("ai") },
                        height = 110.dp
                    )
                }
                // Row 4: Settings (2x1, Wide card)
                item(span = { GridItemSpan(2) }) {
                    FeatureTile(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        onClick = { navController.navigate("settings") },
                        height = 80.dp,
                        description = "Configure api keys & clear cache",
                        isWide = true
                    )
                }
            }

            // Bottom Footer Info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Powered by Veloxterm",
                    color = CyberGreenDim,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Smart Tech Programming ©",
                    color = CyberGreenDim,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FeatureTile(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    height: Dp,
    description: String? = null,
    isWide: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .cyberGlow(color = CyberGreen, borderRadius = 12.dp, glowRadius = 3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (isWide) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberGreen.copy(alpha = 0.1f))
                        .border(1.dp, CyberGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = CyberGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = label,
                        color = CyberGreen,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    if (description != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = description,
                            color = CyberGreenDim,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = CyberGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = label,
                    color = CyberGreen,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
