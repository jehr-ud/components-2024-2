package com.ud.memorygame.logic

import com.ud.memorygame.model.enums.TypeMovement

class Game (var level: String){
    var rows: Int = 0
    var cols: Int = 0
    var board: MutableList<TypeMovement> = mutableListOf()

    init {
        calculateShape()
        generateMovement()
    }

    fun calculateShape(){
        rows = when(this.level){
            "L" -> 2
            "M" -> 3
            "H" -> 4
            else -> 0
        }

        cols = when(this.level){
            "L" -> 2
            "M" -> 3
            "H" -> 4
            else -> 0
        }
    }

    fun generateMovement(){
        var movement = TypeMovement.values().get((0..TypeMovement.values().size).random())
        board.add(movement)
    }
}