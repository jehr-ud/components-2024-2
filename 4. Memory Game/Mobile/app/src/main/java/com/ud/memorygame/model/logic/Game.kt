package com.ud.memorygame.model.logic

import com.ud.memorygame.model.enums.EnumDificult
import com.ud.memorygame.model.enums.TypeMovement


class Game(
    var level: String = "",
    var alias: String = "",
    var players: MutableList<Player> = mutableListOf(),
    var rows: Int = 0,
    var cols: Int = 0,
    var canStart: Boolean = false,
    var board: MutableList<TypeMovement> = mutableListOf(),
    var movementSecuence: MutableList<Int> = mutableListOf(),
    var playerMovements: MutableList<Int> = mutableListOf(),
    var turnPlayerId: String = ""
) {

    init {
        calculateShape()
    }

    fun initGame(){
        generateBoard()
        generateSecuence()
    }

    fun generateSecuence() {
        val sequenceLength = when (level) {
            EnumDificult.LOW.toString() -> 3
            EnumDificult.MEDIUM.toString() -> 5
            EnumDificult.HARD.toString() -> 7
            else -> 5
        }

        if (board.isEmpty()) {
            throw IllegalStateException("El tablero está vacío, no se puede generar la secuencia.")
        }

        repeat(sequenceLength) {
            movementSecuence.add((0 until board.size).shuffled().first())
        }
    }

    fun addMovementInSecuence() {
        val sequenceLength = when (level) {
            EnumDificult.LOW.toString() -> 1
            EnumDificult.MEDIUM.toString() -> 2
            EnumDificult.HARD.toString() -> 3
            else -> 5
        }

        repeat(sequenceLength) {
            movementSecuence.add((0 until board.size).shuffled().first())
        }
    }


    fun addPlayerMovement(indexBoard: Int) {
        playerMovements.add(indexBoard)
    }

    fun compareMovements(): Boolean {
        return playerMovements == movementSecuence
    }

    private fun calculateShape() {
        rows = when (this.level) {
            EnumDificult.LOW.toString() -> 1
            EnumDificult.MEDIUM.toString() -> 2
            EnumDificult.HARD.toString() -> 3
            else -> 0
        }

        cols = when (this.level) {
            EnumDificult.LOW.toString() -> 2
            EnumDificult.MEDIUM.toString() -> 2
            EnumDificult.HARD.toString() -> 3
            else -> 0
        }
    }

    private fun generateBoard() {
        val totalCells = rows * cols
        if (totalCells % 2 != 0) {
            throw IllegalArgumentException("El número total de celdas debe ser par.")
        }

        val availableMovements = TypeMovement.values().toList().shuffled()

        for (i in 0 until totalCells / 2) {
            val movement = availableMovements[i % availableMovements.size]
            board.add(movement)
            board.add(movement)
        }

        board.shuffle()
    }
}
