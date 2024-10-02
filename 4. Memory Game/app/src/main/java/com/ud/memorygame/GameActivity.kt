package com.ud.memorygame

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ud.memorygame.composables.MemoryGameScreen

class GameActivity : AppCompatActivity() {
    var level: String = "L"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            level = bundle.getString("level").toString()
        }

        Log.d("levelcat", "level $level")

        setContent {
            MemoryGameScreen(level)
        }
    }
}