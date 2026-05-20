package com.smarttechprogramming.veloxkit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smarttechprogramming.veloxkit.ui.theme.CyberBackground
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreen
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDark
import com.smarttechprogramming.veloxkit.ui.theme.CyberSurface

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Commands : NavigationItem("commands", Icons.Default.Terminal, "Commands")
    object Snippets : NavigationItem("snippets", Icons.Default.Code, "Snippets")
    object Projects : NavigationItem("projects", Icons.Default.Folder, "Projects")
    object AI : NavigationItem("ai", Icons.Default.Psychology, "AI")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Commands,
        NavigationItem.Snippets,
        NavigationItem.Projects,
        NavigationItem.AI
    )

    NavigationBar(
        containerColor = CyberSurface,
        contentColor = CyberGreen,
        modifier = Modifier.drawBehind {
            drawLine(
                color = CyberGreen,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
        }
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (selected) CyberGreen else CyberGreenDark
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (selected) CyberGreen else CyberGreenDark,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0x1F00FF41),
                    selectedIconColor = CyberGreen,
                    unselectedIconColor = CyberGreenDark,
                    selectedTextColor = CyberGreen,
                    unselectedTextColor = CyberGreenDark
                )
            )
        }
    }
}
