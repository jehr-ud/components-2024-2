package com.ud.memorygame.model.logic

import com.ud.memorygame.model.enums.EnumDificult
import com.ud.memorygame.model.enums.TypeMovement


class Game(var level: String, var players: MutableList<Player> = mutableListOf()) {
    var rows: Int = 0
    var cols: Int = 0
    var canStart: Boolean = false
    var board: MutableList<TypeMovement> = mutableListOf()
    var movementSecuence: MutableList<Int> = mutableListOf()
    var playerMovements: MutableList<Int> = mutableListOf()

    init {
        calculateShape()
        generateBoard()
        generateSecuence()
    }

    fun generateSecuence(){
        movementSecuence.add((0..board.size).shuffled().first())
    }

    fun addPlayerMovement(indexBoard: Int){
        playerMovements.add(indexBoard)
    }

    fun compareMovements(): Boolean{
        var errorMovement: MutableList<Int>  = mutableListOf()

        movementSecuence.forEachIndexed { index, d ->
            if (playerMovements[index] != d){
                errorMovement.add(index)
            }
        }
        return errorMovement.size > 0
    }

    private fun calculateShape() {
        rows = when (this.level) {
            EnumDificult.LOW.toString() -> 1
            EnumDificult.MEDIUM.toString() -> 2
            EnumDificult.HARD.toString() -> 3
            else -> 0
        }

        cols = when (this.level) {
            EnumDificult.LOW.toString(),
            EnumDificult.MEDIUM.toString(),
            EnumDificult.HARD.toString() -> 2
            else -> 0
        }
    }

    private fun generateBoard() {
        val totalCells = rows * cols
        val availableMovements = TypeMovement.values().toList().shuffled()

        for (i in 0 until totalCells / 2) {
            val movement = availableMovements[i % availableMovements.size]
            board.add(movement)
            board.add(movement)
        }

        board.shuffle()
    }
}