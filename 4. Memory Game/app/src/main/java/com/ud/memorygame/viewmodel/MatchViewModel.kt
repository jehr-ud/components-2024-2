package com.ud.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.logic.Player
import com.ud.memorygame.repositories.MatchRepository

class MatchViewModel(private val repository: MatchRepository = MatchRepository()) : ViewModel() {

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _navigateToGame = MutableLiveData<String?>()
    val navigateToGame: LiveData<String?> get() = _navigateToGame

    private val _navigateToLogin = MutableLiveData<Unit>()
    val navigateToLogin: LiveData<Unit> get() = _navigateToLogin

    fun onMatchButtonClicked(alias: String) {
        repository.getGameByAlias(alias) { dataSnapshot ->
            if (dataSnapshot?.exists() == true) {
                for (childSnapshot in dataSnapshot.children) {
                    val gameId = childSnapshot.key
                    val game = childSnapshot.getValue(Game::class.java)
                    game?.let {
                        it.players.add(Player("userEmail", "userId"))
                        it.canStart = true
                        gameId?.let { id ->
                            repository.updateGame(id, it)
                            _navigateToGame.postValue(id)
                        }
                    }
                }
            } else {
                val newGame = Game("1", alias, mutableListOf(Player("userEmail", "userId")))
                repository.createGame(newGame) { gameId ->
                    if (gameId != null) {
                        _toastMessage.postValue("Waiting for second player...")
                        repository.listenToGameUpdates(gameId) { game ->
                            game?.let {
                                if (it.canStart) {
                                    _navigateToGame.postValue(gameId)
                                }
                            }
                        }
                    } else {
                        _toastMessage.postValue("Error creating game")
                    }
                }
            }
        }
    }
}
