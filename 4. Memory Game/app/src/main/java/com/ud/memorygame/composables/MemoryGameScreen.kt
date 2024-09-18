package com.ud.memorygame.composables

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ud.memorygame.logic.Game
import com.ud.memorygame.model.enums.TypeMovement
import kotlinx.coroutines.delay

@Composable
fun MemoryGameScreen(level: String) {
    val game = remember { Game(level) }
    var isPlayerTurn by remember { mutableStateOf(false) }
    var movements = game.movementSecuence

    var currentMovementIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(movements) {
        isPlayerTurn = false
        movements.forEachIndexed { index, movement ->
            currentMovementIndex = movement
            delay(1000L)
            currentMovementIndex = -1
            delay(500L)
        }
        isPlayerTurn = true
    }

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
                    val indexBoard = rowIndex * game.cols + colIndex
                    val movement = game.board[indexBoard]

                    val isHighlighted = currentMovementIndex == indexBoard
                    MovementCard(
                        movement = movement,
                        game = game,
                        indexBoard = indexBoard,
                        isClickable = isPlayerTurn,
                        isHighlighted = isHighlighted
                    )
                }
            }
        }
    }
}

@Composable
fun MovementCard(
    movement: TypeMovement,
    game: Game,
    indexBoard: Int,
    isClickable: Boolean,
    isHighlighted: Boolean
) {
    val cardColor = if (isHighlighted) Color.Yellow else Color.Blue

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color.White
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    if (isClickable) {
                        game.addPlayerMovement(indexBoard)
                        if (game.compareMovements(indexBoard)) {
                            Log.d("finish", "Finish the game")
                        } else if (game.playerMovements.size == game.movementSecuence.size) {
                            game.generateSecuence()
                            game.playerMovements.clear()
                        }
                    }
                },
                enabled = isClickable
            ) {
                Text(
                    text = movement.name,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
