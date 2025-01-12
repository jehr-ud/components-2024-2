package com.ud.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.logic.Player
import com.ud.memorygame.repositories.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository = GameRepository()) : ViewModel() {
    private val _gameState = MutableStateFlow<Game?>(null)
    val gameState: StateFlow<Game?> = _gameState

    private val _currentPlayer = MutableLiveData<Player?>()
    val currentPlayer: LiveData<Player?> get() = _currentPlayer

    private val _gameMessage = MutableLiveData<String?>()
    val gameMessage: LiveData<String?> get() = _gameMessage

    fun gameMessage(message: String) {
        _gameMessage.value = message
    }

    fun clearGameMessage() {
        _gameMessage.value = null
    }

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

    private fun getNextPlayerId(game: Game): String {
        val currentPlayerIndex = game.players.indexOfFirst { it.userId == game.turnPlayerId }

        return if (currentPlayerIndex != -1) {
            val nextPlayerIndex = (currentPlayerIndex + 1) % game.players.size
            game.players[nextPlayerIndex].userId
        } else {
            throw IllegalArgumentException("Player with id ${game.turnPlayerId} not found.")
        }
    }

    fun updateTurnPlayerId(gameId: String, game: Game) {
        val nextPlayerId = getNextPlayerId(game)
        game.turnPlayerId = nextPlayerId
        repository.updateGame(gameId, game)
    }

    fun updateScorePlayer(gameId: String, game: Game) {
    }

    fun listenToGameUpdates(gameId: String) {
        repository.listenToGameUpdates(gameId) { updatedGame ->
            _gameState.value = updatedGame
            val player = updatedGame?.players?.find { it.userId == updatedGame.turnPlayerId }
            _currentPlayer.value = player
        }
    }
}