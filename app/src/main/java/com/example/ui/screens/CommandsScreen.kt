package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CommandCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberGreenDark
import com.example.ui.theme.CyberSurface
import com.example.viewmodel.CommandsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandsScreen(
    viewModel: CommandsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val commands by viewModel.filteredCommands.collectAsState()

    val categories = listOf("All", "Termux", "Git", "Python", "Node")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberSurface),
            textStyle = LocalTextStyle.current.copy(
                color = CyberGreen,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            ),
            placeholder = {
                Text(
                    text = "Search commands...",
                    color = CyberGreenDark,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = CyberGreen
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberGreen,
                unfocusedBorderColor = CyberGreen,
                focusedContainerColor = CyberSurface,
                unfocusedContainerColor = CyberSurface,
                cursorColor = CyberGreen
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isActive = category.equals(selectedCategory, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isActive) CyberGreen else CyberSurface)
                        .border(
                            width = 1.dp,
                            color = CyberGreen,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.setSelectedCategory(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isActive) CyberBackground else CyberGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable Commands List
        if (commands.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No commands found",
                    color = CyberGreenDark,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(commands) { command ->
                    CommandCard(command = command)
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Nav bar offset padding
                }
            }
        }
    }
}
