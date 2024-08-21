package com.ud.evennumber

import kotlin.math.PI
import kotlin.math.pow

fun main(){
    try {
        println("Input the f radio:")
        val radio: Double = readln().toDouble()
        if (radio != null){
            val area = PI * radio.pow(2)
            print("The circle area is: $area")
        }
    } catch (e: NumberFormatException){
        print("Number is not valid")
    }
}