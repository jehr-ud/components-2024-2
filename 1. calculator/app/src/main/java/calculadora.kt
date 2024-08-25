package com.repo.calculadora

class Calculadora {

    fun main() {
        println("Bienvenido a la calculadora simple en Kotlin")

        // Solicitar al usuario que ingrese dos números
        print("Ingrese el primer número: ")
        val num1 = readLine()!!.toDouble()

        print("Ingrese el segundo número: ")
        val num2 = readLine()!!.toDouble()

        // Mostrar el menú de operaciones
        println("Seleccione la operación:")
        println("1. Sumar")
        println("2. Restar")
        println("3. Multiplicar")
        println("4. Dividir")

        print("Ingrese el número de la operación deseada (1-4): ")
        val opcion = readLine()!!.toInt()

        // Realizar la operación seleccionada
        when (opcion) {
            1 -> println("Resultado: ${num1 + num2}")
            2 -> println("Resultado: ${num1 - num2}")
            3 -> println("Resultado: ${num1 * num2}")
            4 -> {
                if (num2 != 0.0) {
                    println("Resultado: ${num1 / num2}")
                } else {
                    println("Error: No se puede dividir entre cero.")
                }
            }
            else -> println("Opción no válida. Por favor, seleccione una opción entre 1 y 4.")
        }
    }

}