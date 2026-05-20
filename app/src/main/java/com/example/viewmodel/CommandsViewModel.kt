package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.Command
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CommandsViewModel : ViewModel() {

    private val preloadedCommands = listOf(
        // Termux
        Command("pkg update", "Update Termux packages", "Termux"),
        Command("pkg upgrade", "Upgrade all installed packages", "Termux"),
        Command("pkg install python", "Install Python", "Termux"),
        Command("pkg install nodejs", "Install Node.js", "Termux"),
        Command("pkg install git", "Install Git", "Termux"),
        Command("termux-setup-storage", "Setup storage access", "Termux"),

        // Git
        Command("git init", "Initialize a new repository", "Git"),
        Command("git clone [url]", "Clone a repository", "Git"),
        Command("git add .", "Stage all changes", "Git"),
        Command("git commit -m \"\"", "Commit with message", "Git"),
        Command("git push", "Push to remote", "Git"),
        Command("git pull", "Pull latest changes", "Git"),

        // Python
        Command("python3 --version", "Check Python version", "Python"),
        Command("pip install flask", "Install Flask", "Python"),
        Command("python3 -m venv env", "Create virtual environment", "Python"),
        Command("pip freeze", "List installed packages", "Python"),

        // Node
        Command("npm init", "Initialize Node project", "Node"),
        Command("npm install express", "Install Express", "Node"),
        Command("node index.js", "Run Node app", "Node"),
        Command("npm run start", "Start npm script", "Node")
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredCommands: StateFlow<List<Command>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        preloadedCommands.filter { cmd ->
            val matchesCategory = category == "All" || cmd.category.equals(category, ignoreCase = true)
            val matchesQuery = cmd.command.contains(query, ignoreCase = true) || 
                               cmd.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = preloadedCommands
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
}
