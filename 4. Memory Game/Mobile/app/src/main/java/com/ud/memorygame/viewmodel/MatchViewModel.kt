package com.ud.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.logic.Player
import com.ud.memorygame.repositories.GameRepository

class MatchViewModel(private val repository: GameRepository = GameRepository()) : ViewModel() {

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _navigateToGame = MutableLiveData<String?>()
    val navigateToGame: LiveData<String?> get() = _navigateToGame

    private val _navigateToLogin = MutableLiveData<Unit>()
    val navigateToLogin: LiveData<Unit> get() = _navigateToLogin

    fun onMatchButtonClicked(alias: String, level: String, email: String, userId: String) {
        repository.getGameByAlias(alias) { dataSnapshot ->
            if (dataSnapshot?.exists() == true) {
                for (childSnapshot in dataSnapshot.children) {
                    val gameId = childSnapshot.key
                    val game = childSnapshot.getValue(Game::class.java)
                    game?.let {
                        // Verificar si el jugador tiene el nivel adecuado
                        val currentPlayer = it.players.find { player -> player.userId == userId }
                        if (currentPlayer?.level == level) {
                            it.players.add(Player(email, userId, level)) // Agregar el nivel aquí también
                            it.canStart = true
                            gameId?.let { id ->
                                repository.updateGame(id, it)
                                _navigateToGame.postValue(id)
                            }
                        } else {
                            _toastMessage.postValue("No tienes acceso a este nivel.")
                        }
                    }
                }
            } else {
                _toastMessage.postValue("Partida no encontrada.")
            }
        }
    }
}