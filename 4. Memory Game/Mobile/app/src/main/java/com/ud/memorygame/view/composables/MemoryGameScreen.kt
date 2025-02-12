package com.ud.memorygame.view.composables

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ud.memorygame.model.logic.Game
import com.ud.memorygame.model.enums.TypeMovement
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ud.memorygame.model.enums.EnumDificult
import com.ud.memorygame.model.logic.Score
import com.ud.memorygame.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MemoryGameScreen(gameId: String, userId: String, viewModel: GameViewModel = viewModel()) {
    val game by viewModel.gameState.collectAsState()
    val currentPlayer by viewModel.currentPlayer.observeAsState()
    val gameMessage by viewModel.gameMessage.observeAsState()

    LaunchedEffect(gameId) {
        viewModel.getGameById(gameId)
        viewModel.listenToGameUpdates(gameId)
    }

    gameMessage?.let { message ->
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
        LaunchedEffect(message) {
            viewModel.clearGameMessage()
        }
    }

    if (game == null) {
        Text("Loading game.. ", modifier = Modifier.padding(16.dp))
    } else {
        val isPlayerTurn = remember { mutableStateOf(false) }
        val movements = game!!.movementSecuence
        val currentMovementIndex = remember { mutableStateOf(-1) }

        val movementDelayByLevel = when (game!!.level) {
            EnumDificult.LOW.toString() -> 2500L
            EnumDificult.MEDIUM.toString() -> 1000L
            EnumDificult.HARD.toString() -> 500L
            else -> 1000L
        }

        LaunchedEffect(movements) {
            isPlayerTurn.value = false
            delay(500L)
            movements.forEachIndexed { index, movement ->
                currentMovementIndex.value = movement
                delay(movementDelayByLevel)
                currentMovementIndex.value = -1
                delay(500L)
            }

            isPlayerTurn.value = currentPlayer!!.userId == userId
        }

        Column {
            Text("Playing the game: " + game!!.alias)
            if (currentPlayer != null) {
                Text(" Turn: ${currentPlayer!!.email}")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (rowIndex in 0 until game!!.rows) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (colIndex in 0 until game!!.cols) {
                        val indexBoard = rowIndex * game!!.cols + colIndex
                        val movement = game!!.board[indexBoard]

                        val isHighlighted = currentMovementIndex.value == indexBoard
                        MovementCard(
                            movement = movement,
                            game = game!!,
                            viewModel,
                            gameId,
                            indexBoard = indexBoard,
                            isClickable = isPlayerTurn.value,
                            isHighlighted = isHighlighted
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MovementCard(
    movement: TypeMovement,
    game: Game,
    viewModel: GameViewModel,
    gameId: String,
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

                        val score = Score(
                            player_uuid = game.turnPlayerId,
                            game_uuid = gameId,
                            score = "333"
                        )

                        if (game.compareMovements()) {
                            viewModel.gameMessage("Complete sequence the game")
                            viewModel.sendScore(score)
                        } else if (game.playerMovements.size == game.movementSecuence.size) {
                            viewModel.gameMessage("Sequence no completed")
                            game.addMovementInSecuence()
                            game.playerMovements.clear()

                            viewModel.updateTurnPlayerId(gameId, game, score)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameNavigationDrawer(gameId: String, userId: String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf("Inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerMenuContent(onMenuItemClick = { menuItem ->
                    selectedScreen = when (menuItem) {
                        "mis_partidas" -> "Mis partidas"
                        "jugar" -> "Jugar"
                        else -> "Inicio"
                    }
                    scope.launch { drawerState.close() }
                })
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(selectedScreen) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        when (selectedScreen) {
                            "Mis partidas" -> Text("Aquí se mostrarán tus partidas guardadas.", modifier = Modifier.padding(16.dp))
                            "Jugar" -> {
                                MemoryGameScreen(gameId, userId)
                            }
                            else -> MemoryGameScreen(gameId, userId)
                        }
                    }
                }
            )
        }
    )
}