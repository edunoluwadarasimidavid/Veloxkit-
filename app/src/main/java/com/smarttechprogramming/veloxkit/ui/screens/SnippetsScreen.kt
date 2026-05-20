package com.smarttechprogramming.veloxkit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.smarttechprogramming.veloxkit.data.models.SnippetEntity
import com.smarttechprogramming.veloxkit.ui.components.SnippetCard
import com.smarttechprogramming.veloxkit.ui.theme.CyberBackground
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreen
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDark
import com.smarttechprogramming.veloxkit.ui.theme.CyberGreenDim
import com.smarttechprogramming.veloxkit.ui.theme.CyberSurface
import com.smarttechprogramming.veloxkit.viewmodel.SnippetsViewModel

@Composable
fun SnippetsScreen(
    viewModel: SnippetsViewModel,
    modifier: Modifier = Modifier
) {
    val snippets by viewModel.snippets.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var editingSnippet by remember { mutableStateOf<SnippetEntity?>(null) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var deletingSnippet by remember { mutableStateOf<SnippetEntity?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Screen Title block
            Text(
                text = "SNIPPET VAULT",
                color = CyberGreen,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (snippets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Vault is empty. Add snippets with global +",
                        color = CyberGreenDark,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(snippets) { snippet ->
                        SnippetCard(
                            snippet = snippet,
                            onEdit = {
                                editingSnippet = snippet
                                showEditDialog = true
                            },
                            onDelete = {
                                deletingSnippet = snippet
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Nav bar spacing
                    }
                }
            }
        }

        // FAB Bottom Right
        FloatingActionButton(
            onClick = {
                editingSnippet = null
                showEditDialog = true
            },
            containerColor = CyberGreen,
            contentColor = CyberBackground,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 24.dp) // Offset for bottom navigation bar
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Snippet",
                tint = CyberBackground,
                modifier = Modifier.size(28.dp)
            )
        }

        // Add/Edit Dialog
        if (showEditDialog) {
            AddEditSnippetDialog(
                snippet = editingSnippet,
                onDismiss = { showEditDialog = false },
                onSave = { title, language, code ->
                    if (editingSnippet == null) {
                        viewModel.addSnippet(title, language, code)
                    } else {
                        viewModel.updateSnippet(
                            editingSnippet!!.copy(
                                title = title,
                                language = language,
                                code = code,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                    showEditDialog = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirmDialog && deletingSnippet != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = {
                    Text(
                        text = "DELETE SNIPPET?",
                        color = Color(0xFFFF3333),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to permanently erase \"${deletingSnippet?.title}\" from your Vault?",
                        color = CyberGreen,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3333)),
                        onClick = {
                            deletingSnippet?.let { viewModel.deleteSnippet(it) }
                            showDeleteConfirmDialog = false
                        }
                    ) {
                        Text(text = "DELETE", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        border = BorderStroke(1.dp, CyberGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                        onClick = { showDeleteConfirmDialog = false }
                    ) {
                        Text(text = "CANCEL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = CyberSurface,
                modifier = Modifier.border(1.dp, CyberGreen, RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun AddEditSnippetDialog(
    snippet: SnippetEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(snippet?.title ?: "") }
    var language by remember { mutableStateOf(snippet?.language ?: "") }
    var code by remember { mutableStateOf(snippet?.code ?: "") }

    var errorMsg by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberSurface)
                .border(2.dp, CyberGreen, RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Text(
                text = if (snippet == null) "ADD SNIPPET" else "EDIT SNIPPET",
                color = CyberGreen,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = Color(0xFFFF3333),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", fontFamily = FontFamily.Monospace) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberGreen,
                    unfocusedBorderColor = CyberGreenDim,
                    focusedLabelColor = CyberGreen,
                    unfocusedLabelColor = CyberGreenDim,
                    cursorColor = CyberGreen,
                    focusedTextColor = CyberGreen,
                    unfocusedTextColor = CyberGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Language field
            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                label = { Text("Language (e.g. Python, JS, Kotlin)", fontFamily = FontFamily.Monospace) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberGreen,
                    unfocusedBorderColor = CyberGreenDim,
                    focusedLabelColor = CyberGreen,
                    unfocusedLabelColor = CyberGreenDim,
                    cursorColor = CyberGreen,
                    focusedTextColor = CyberGreen,
                    unfocusedTextColor = CyberGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Code editor multiline field
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Code block", fontFamily = FontFamily.Monospace) },
                minLines = 4,
                maxLines = 8,
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberGreen,
                    unfocusedBorderColor = CyberGreenDim,
                    focusedLabelColor = CyberGreen,
                    unfocusedLabelColor = CyberGreenDim,
                    cursorColor = CyberGreen,
                    focusedTextColor = CyberGreen,
                    unfocusedTextColor = CyberGreen
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    border = BorderStroke(1.dp, CyberGreen),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("CANCEL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        if (title.trim().isEmpty() || language.trim().isEmpty() || code.trim().isEmpty()) {
                            errorMsg = "Error: All fields are required."
                        } else {
                            onSave(title, language, code)
                        }
                    }
                ) {
                    Text(
                        text = "SAVE",
                        color = CyberBackground,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
