package com.smarttechprogramming.veloxkit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttechprogramming.veloxkit.data.models.ChatMessageEntity
import com.smarttechprogramming.veloxkit.data.repository.AppRepository
import com.smarttechprogramming.veloxkit.data.repository.GeminiRestClient
import com.smarttechprogramming.veloxkit.data.repository.PreferenceRepository
import com.smarttechprogramming.veloxkit.data.repository.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AiViewModel(
    private val repository: AppRepository,
    private val preferenceRepository: PreferenceRepository,
    private val context: Context
) : ViewModel() {

    val chatFlow: StateFlow<List<ChatMessageEntity>> = repository.allMessages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val apiKeyFlow: StateFlow<String> = preferenceRepository.apiKeyFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val systemInstruction = "You are Veloxkit AI, an Android developer assistant specializing in Termux, Git, Python, Node.js, and mobile coding. Always respond with the exact command first in monospace format, then a short plain explanation. Keep responses concise and practical."

    init {
        checkConnectivity()
    }

    fun checkConnectivity() {
        viewModelScope.launch {
            _isOffline.value = !isNetworkAvailable(context)
        }
    }

    fun sendMessage(promptText: String) {
        val trimmed = promptText.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            // Check connectivity immediately
            val offline = !isNetworkAvailable(context)
            _isOffline.value = offline
            if (offline) {
                // If offline, store user message & store offline sys error message
                repository.insertMessage(ChatMessageEntity(role = "user", text = trimmed))
                repository.insertMessage(ChatMessageEntity(role = "model", text = "Offline Mode — AI unavailable. Connection was not detected."))
                return@launch
            }

            // Save user message to database
            repository.insertMessage(ChatMessageEntity(role = "user", text = trimmed))

            // Fetch current API Key from flow
            val apiKey = apiKeyFlow.value

            _isLoading.value = true

            // Call API
            val aiResponse = GeminiRestClient.generateContent(
                apiKey = apiKey,
                systemInstruction = systemInstruction,
                prompt = trimmed
            )

            // Save response to DB
            repository.insertMessage(ChatMessageEntity(role = "model", text = aiResponse))

            _isLoading.value = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearAllMessages()
        }
    }

    fun saveApiKey(apiKey: String) {
        viewModelScope.launch {
            preferenceRepository.saveApiKey(apiKey)
        }
    }
}
