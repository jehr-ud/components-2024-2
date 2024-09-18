package com.ud.memorygame.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ud.memorygame.logic.Game
import com.ud.memorygame.model.enums.TypeMovement

@Composable
fun MemoryGameScreen(level: String) {
    val game = remember { Game(level) }
    val board = game.board

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (rowIndex in 0 until game.rows) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (colIndex in 0 until game.cols) {
                    val movement = board[rowIndex * game.cols + colIndex]
                    MovementCard(movement)
                }
            }
        }
    }
}

@Composable
fun MovementCard(movement: TypeMovement) {
    Card (
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = movement.name,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
