package com.ud.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.logic.Player
import com.ud.memorygame.model.logic.Score
import com.ud.memorygame.repositories.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import com.google.gson.Gson

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

    fun updateTurnPlayerId(gameId: String, game: Game, score: Score) {
        val nextPlayerId = getNextPlayerId(game)
        game.turnPlayerId = nextPlayerId
        repository.updateGame(gameId, game)

        sendScore(score)
    }

    fun sendScore(score: Score) {
        val client = OkHttpClient()
        val gson = Gson()

        val json = gson.toJson(score)
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url("http://127.0.0.1:3000/api/v1/scores")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            println("Error al enviar el score: ${response.code}")
        } else {
            println("Score enviado correctamente")
        }
    }

    fun listenToGameUpdates(gameId: String) {
        repository.listenToGameUpdates(gameId) { updatedGame ->
            _gameState.value = updatedGame
            val player = updatedGame?.players?.find { it.userId == updatedGame.turnPlayerId }
            _currentPlayer.value = player
        }
    }
}