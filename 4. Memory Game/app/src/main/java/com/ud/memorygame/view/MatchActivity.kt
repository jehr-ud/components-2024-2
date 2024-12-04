package com.ud.memorygame.view


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ud.memorygame.databinding.ActivityMatchBinding
import com.ud.memorygame.R
import com.ud.memorygame.viewmodel.MatchViewModel

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private val viewModel: MatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigateToGame.observe(this) { gameId ->
            gameId?.let {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("gameId", it)
                startActivity(intent)
            }
        }

        viewModel.navigateToLogin.observe(this) {
            val intent = Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupListeners() {
        binding.btnMatch.setOnClickListener {
            val alias = binding.txtMatchAlias.text.toString()
            if (alias.isEmpty()) {
                Toast.makeText(this, R.string.match_activity_empty_alias, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.onMatchButtonClicked(alias)
            }
        }
    }
}