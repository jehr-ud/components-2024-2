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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ud.memorygame.logic.Game
import com.ud.memorygame.model.enums.TypeMovement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameNavigationDrawer(level: String) {
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
                                MemoryGameScreen(level)
                            }
                            else -> Text("Bienvenido", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            )
        }
    )
}