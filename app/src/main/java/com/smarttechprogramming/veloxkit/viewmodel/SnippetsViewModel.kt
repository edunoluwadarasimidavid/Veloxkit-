package com.smarttechprogramming.veloxkit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttechprogramming.veloxkit.data.models.SnippetEntity
import com.smarttechprogramming.veloxkit.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SnippetsViewModel(private val repository: AppRepository) : ViewModel() {

    val snippets: StateFlow<List<SnippetEntity>> = repository.allSnippets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addSnippet(title: String, language: String, code: String) {
        viewModelScope.launch {
            repository.insertSnippet(
                SnippetEntity(
                    title = title,
                    language = language,
                    code = code
                )
            )
        }
    }

    fun updateSnippet(snippet: SnippetEntity) {
        viewModelScope.launch {
            repository.updateSnippet(snippet)
        }
    }

    fun deleteSnippet(snippet: SnippetEntity) {
        viewModelScope.launch {
            repository.deleteSnippet(snippet)
        }
    }

    fun clearAllSnippets() {
        viewModelScope.launch {
            repository.clearAllSnippets()
        }
    }
}
