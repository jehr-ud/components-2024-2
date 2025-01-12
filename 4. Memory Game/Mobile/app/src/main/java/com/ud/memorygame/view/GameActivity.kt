package com.ud.memorygame.view

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ud.memorygame.view.composables.GameNavigationDrawer

class GameActivity : AppCompatActivity() {
    private var gameId: String = "L"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            gameId = bundle.getString("gameId").toString()
        }

        Log.d("gameIdcat", "gameId $gameId")

        setContent {
            MaterialTheme {
                Surface {
                    GameNavigationDrawer(gameId)
                }
            }
        }
    }
}