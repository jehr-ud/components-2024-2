package com.ud.memorygame.view


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ud.memorygame.databinding.ActivityMatchBinding
import com.ud.memorygame.R
import com.ud.memorygame.viewmodel.MatchViewModel

class MatchActivity : AppCompatActivity() {
    private var level: String = "L"
    private lateinit var binding: ActivityMatchBinding
    private val viewModel: MatchViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        level = intent.extras?.getString("level", "L") ?: "L"
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.toastMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.navigateToGame.observe(this) { gameId ->
            gameId?.let {
                startActivity(Intent(this, GameActivity::class.java).apply {
                    putExtra("gameId", it)
                })
            }
        }

        viewModel.navigateToLogin.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupListeners() {
        binding.btnMatch.setOnClickListener {
            val alias = binding.txtMatchAlias.text.toString()
            if (alias.isEmpty()) {
                Toast.makeText(this, R.string.match_activity_empty_alias, Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.run {
                    val userId = getString("userId", "") ?: return@run
                    val email = getString("email", "") ?: return@run
                    viewModel.onMatchButtonClicked(alias, level, email, userId)
                }
            }
        }
    }
}
