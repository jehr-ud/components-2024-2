package com.ud.memorygame.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.ud.memorygame.R
import com.ud.memorygame.model.enums.EnumDificult

class LevelActivity : ComponentActivity() {
    private lateinit var btnLow: Button
    private lateinit var btnMedium: Button
    private lateinit var btnHard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_activity)

        addLogicToBtns()
    }

    private fun addLogicToBtns(){
        btnLow = findViewById(R.id.btnLow)
        btnMedium = findViewById(R.id.btnMedium)
        btnHard = findViewById(R.id.btnHard)

        btnLow.setOnClickListener {
            gotoMatch(EnumDificult.LOW.toString())
        }

        btnMedium.setOnClickListener{
            gotoMatch(EnumDificult.MEDIUM.toString())
        }

        btnHard.setOnClickListener{
            gotoMatch(EnumDificult.HARD.toString())
        }
    }

    private fun gotoMatch(level: String){
        val intent = Intent(this, MatchActivity::class.java)
        intent.putExtra("level", level)
        startActivity(intent)
    }
}