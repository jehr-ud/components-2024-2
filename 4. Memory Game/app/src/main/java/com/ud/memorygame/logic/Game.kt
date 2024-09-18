package com.ud.memorygame.logic

import com.ud.memorygame.model.enums.EnumDificult
import com.ud.memorygame.model.enums.TypeMovement


class Game(var level: String) {
    var rows: Int = 0
    var cols: Int = 0
    var board: MutableList<TypeMovement> = mutableListOf()

    init {
        calculateShape()
        generateBoard()
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