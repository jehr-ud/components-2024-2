package com.ud.memorygame.repositories

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.ud.memorygame.model.logic.Game

class MatchRepository {
    private val database = Firebase.database.reference

    fun getGameByAlias(alias: String, callback: (DataSnapshot?) -> Unit) {
        database.child("games").orderByChild("alias").equalTo(alias)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    callback(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun createGame(game: Game, callback: (String?) -> Unit) {
        val gameId = database.child("games").push().key
        if (gameId != null) {
            database.child("games").child(gameId).setValue(game)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) callback(gameId) else callback(null)
                }
        } else {
            callback(null)
        }
    }

    fun updateGame(gameId: String, game: Game) {
        database.child("games").child(gameId).setValue(game)
    }

    fun listenToGameUpdates(gameId: String, callback: (Game?) -> Unit) {
        database.child("games").child(gameId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val game = dataSnapshot.getValue(Game::class.java)
                callback(game)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}