package com.example.data.repository

import com.example.data.database.SnippetDao
import com.example.data.database.ChatDao
import com.example.data.models.SnippetEntity
import com.example.data.models.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val snippetDao: SnippetDao,
    private val chatDao: ChatDao
) {
    val allSnippets: Flow<List<SnippetEntity>> = snippetDao.getAllSnippets()
    val allMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    suspend fun insertSnippet(snippet: SnippetEntity): Long {
        return snippetDao.insertSnippet(snippet)
    }

    suspend fun updateSnippet(snippet: SnippetEntity) {
        snippetDao.updateSnippet(snippet)
    }

    suspend fun deleteSnippet(snippet: SnippetEntity) {
        snippetDao.deleteSnippet(snippet)
    }

    suspend fun clearAllSnippets() {
        snippetDao.clearAllSnippets()
    }

    suspend fun insertMessage(message: ChatMessageEntity): Long {
        return chatDao.insertMessage(message)
    }

    suspend fun clearAllMessages() {
        chatDao.clearAllMessages()
    }
}
