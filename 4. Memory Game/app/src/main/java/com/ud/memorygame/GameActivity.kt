package com.ud.memorygame

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ud.memorygame.logic.Game

class GameActivity : AppCompatActivity() {
    var level: String = "L"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            level = bundle.getString("level").toString()
        }

        Log.d("levelcat", "level $level")

        var game = Game(level)
    }
}