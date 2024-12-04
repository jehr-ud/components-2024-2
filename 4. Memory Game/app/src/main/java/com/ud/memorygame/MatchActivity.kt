package com.ud.memorygame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.ud.memorygame.databinding.ActivityMatchBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.logic.Player

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        binding.btnMatch.setOnClickListener{
            if (binding.txtMatchAlias.text.isEmpty()){
                Toast.makeText(this, R.string.match_activity_empty_alias, Toast.LENGTH_SHORT).show()
            } else {
                storageMatchInDB()
            }
        }
    }

    private fun storageMatchInDB(){
        val userId = sharedPreferences.getString("userId", "")
        val email = sharedPreferences.getString("email", "")

        if (userId.isNullOrEmpty() || email.isNullOrEmpty()){
            goToLogin()
            return
        }

        val alias = binding.txtMatchAlias.text.toString()

        database.child("games").orderByChild("alias").equalTo(alias)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (childSnapshot in dataSnapshot.children) {
                            val gameId = childSnapshot.key
                            gameId?.let {
                                val gameSnapshot = childSnapshot.getValue(Game::class.java)
                                gameSnapshot?.let { game ->
                                    game.players.add(Player(email, userId))
                                    game.canStart = true
                                    database.child("games").child(it).setValue(game)
                                    goToGame(gameId)
                                }
                            }
                        }
                    } else {
                        val gameID = database.child("games").push().key
                        gameID?.let {
                            val players: MutableList<Player> = mutableListOf(Player(email,  userId))
                            val game = Game("1", alias, players)

                            Toast.makeText(this@MatchActivity, "eSTE ES EL A" + game.alias.toString(), Toast.LENGTH_SHORT).show()

                            database.child("games").child(it).setValue(game)
                            setupGameUpdateListener(gameID)

                            Toast.makeText(this@MatchActivity, "Waiting for second player..", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                    Toast.makeText(this@MatchActivity, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun setupGameUpdateListener(gameId: String) {
        val database = database.child("games").child(gameId)

        val gameUpdateListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data changes
                val game = dataSnapshot.getValue(Game::class.java)
                game?.let {
                    if (game.canStart){
                        goToGame(gameId)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.addValueEventListener(gameUpdateListener)
    }

    fun goToLogin(){
        var intent = Intent(this, LevelActivity::class.java)
        startActivity(intent)
    }

    fun goToGame(gameId: String){
        val intent = Intent(this@MatchActivity, GameActivity::class.java)
        intent.putExtra("gameId", gameId)
        startActivity(intent)
    }
}