package com.ud.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ud.memorygame.view.composables.GameNavigationDrawer

class GameActivity : AppCompatActivity() {
    private var gameId: String = "L"
    private var userId: String = "L"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            gameId = bundle.getString("gameId").toString()
        }

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.run {
            userId = getString("userId", "") ?: return@run
        }

        setContent {
            MaterialTheme {
                Surface {
                    GameNavigationDrawer(gameId, userId)
                }
            }
        }
    }
}