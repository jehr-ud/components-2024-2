package com.ud.memorygame

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ud.memorygame.composables.GameNavigationDrawer

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
            MaterialTheme {
                Surface {
                    GameNavigationDrawer(level)
                }
            }
        }
    }
}