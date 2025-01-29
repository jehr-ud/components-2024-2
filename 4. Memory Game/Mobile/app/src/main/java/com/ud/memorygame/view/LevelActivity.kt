package com.ud.memorygame.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ud.memorygame.R
import com.ud.memorygame.viewmodel.LevelViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LevelActivity : ComponentActivity() {
    private lateinit var btnLow: Button
    private lateinit var btnMedium: Button
    private lateinit var btnHard: Button

    private val levelViewModel: LevelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_activity)

        btnLow = findViewById(R.id.btnLow)
        btnMedium = findViewById(R.id.btnMedium)
        btnHard = findViewById(R.id.btnHard)

        observeViewModel()

        // Llama al ViewModel para obtener el nivel del jugador
        levelViewModel.fetchPlayerLevel("abc38232")

        setupListeners()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            levelViewModel.levelState.collectLatest { level ->
                Log.d("LevelActivity", "Received Level: $level")
                level?.let { updateButtons(it) }
            }
        }
    }

    private fun updateButtons(level: String) {
        when (level) {
            "low" -> {
                btnLow.isEnabled = true
                btnMedium.isEnabled = false
                btnHard.isEnabled = false
            }
            "medium" -> {
                btnLow.isEnabled = true
                btnMedium.isEnabled = true
                btnHard.isEnabled = false
            }
            "hard" -> {
                btnLow.isEnabled = true
                btnMedium.isEnabled = true
                btnHard.isEnabled = true
            }
            else -> {
                btnLow.isEnabled = false
                btnMedium.isEnabled = false
                btnHard.isEnabled = false
            }
        }
    }

    private fun setupListeners() {
        btnLow.setOnClickListener { gotoMatch("low") }
        btnMedium.setOnClickListener { gotoMatch("medium") }
        btnHard.setOnClickListener { gotoMatch("hard") }
    }

    private fun gotoMatch(level: String) {
        val currentLevel = levelViewModel.levelState.value

        when {
            currentLevel == null -> {
                Toast.makeText(this, "No se pudo obtener tu nivel. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
                return
            }
            currentLevel == "low" && level != "low" -> {
                Toast.makeText(this, "No tienes acceso a este nivel aún.", Toast.LENGTH_SHORT).show()
                return
            }
            currentLevel == "medium" && level == "hard" -> {
                Toast.makeText(this, "No tienes acceso al nivel difícil aún.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                val intent = Intent(this, MatchActivity::class.java)
                intent.putExtra("level", level)
                startActivity(intent)
            }
        }
    }
}