package com.ud.memorygame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.repositories.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository = GameRepository()) : ViewModel() {
    private val _gameState = MutableStateFlow<Game?>(null)
    val gameState: StateFlow<Game?> = _gameState

    fun getGameById(id: String) {
        viewModelScope.launch {
            repository.getGameById(id) { dataSnapshot ->
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    val game = dataSnapshot.getValue(Game::class.java)
                    _gameState.value = game // Actualizar el estado observable
                } else {
                    _gameState.value = null // No encontrado
                }
            }
        }
    }
}