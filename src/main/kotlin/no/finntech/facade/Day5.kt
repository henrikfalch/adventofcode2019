package no.finntech.facade

import java.lang.IllegalStateException

class Day5 {

    fun runProgram(program: List<Int>, input:Int): List<Int> {
        var current = 4 to program
        var index = 0
        var counter = 1
        while (current.first != -1) {
            //println("Iteration ${counter++}")
            current = calcIteration(current.second, index, input)
            index = current.first
        }
        return current.second
    }


    fun calcIteration(program: List<Int>, index: Int, input: Int): Pair<Int, List<Int>> {
        val completeInstruction = program[index]
        val instruction = completeInstruction % 100
        val paramMode = listOf((completeInstruction % 1000) / 100, (completeInstruction % 10000) / 1000, completeInstruction / 10000)

        return when (instruction) {
            1 -> index+4 to add(program, index, paramMode)
            2 -> index+4 to multiply(program, index, paramMode)
            3 -> index+2 to input(program, index, input)
            4 -> index+2 to output(program, index, paramMode)
            5 -> jumpIfTrue(program, index, paramMode)
            6 -> jumpIfFalse(program, index, paramMode)
            7 -> index+4 to lessThan(program, index, paramMode)
            8 -> index+4 to equals(program, index, paramMode)
            99 -> -1 to program
            else -> throw IllegalStateException("Illegal instruction: $instruction")
        }
    }

    private fun equals(program: List<Int>, index: Int, paramMode: List<Int>): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> if (v1 == v2) 1 else 0 }
        return executeFunction(program, index, function, paramMode)
    }

    private fun lessThan(program: List<Int>, index: Int, paramMode: List<Int>): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> if (v1 < v2) 1 else 0 }
        return executeFunction(program, index, function, paramMode)
    }

    private fun jumpIfFalse(program: List<Int>, index: Int, paramMode: List<Int>): Pair<Int, List<Int>> {
        val val1 = getValueFromMode(program, paramMode[0], index + 1)

        if (val1 != 0) {
            return index+3 to program
        }
        val val2 = getValueFromMode(program, paramMode[1], index + 2)
        return val2 to program
    }

    private fun jumpIfTrue(program: List<Int>, index: Int, paramMode: List<Int>): Pair<Int, List<Int>> {
        val val1 = getValueFromMode(program, paramMode[0], index + 1)

        if (val1 == 0) {
            return index+3 to program
        }
        val val2 = getValueFromMode(program, paramMode[1], index + 2)
        return val2 to program
    }

    private fun output(program: List<Int>, index: Int, paramMode: List<Int>): List<Int> {
        val output = getValueFromMode(program, paramMode[0],index+1)
        println("Output: $output")
        return program
    }

    private fun input(program: List<Int>, index: Int, input: Int): List<Int> {
        val copy = program.toMutableList()
        copy[program[index+1]] = input
        return copy
    }

    private fun add(program: List<Int>, index: Int, paramMode: List<Int>): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 + v2 }
        return executeFunction(program, index, function, paramMode)
    }

    private fun multiply(program: List<Int>, index: Int, paramMode: List<Int>): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 * v2 }
        return executeFunction(program, index, function, paramMode)
    }

    private fun executeFunction(program: List<Int>, index: Int, function: (Int, Int) -> Int, paramMode: List<Int>): List<Int> {
        val val1 = getValueFromMode(program, paramMode[0], index + 1)
        val val2 = getValueFromMode(program, paramMode[1], index + 2)
        val copy = program.toMutableList()
        copy[program[index + 3]] = function.invoke(val1, val2)

        return copy
    }

    private fun getValueFromMode(program: List<Int>, param: Int, index: Int): Int {
        return if (param == 0) program.getValue(index) else program[index]
    }

    private fun List<Int>.getValue(index: Int): Int {
        return this[this[index]]
    }

    fun excercise1() {
        val program = fileAsIntList("day5.txt").toMutableList()
        runProgram(program, 1)
    }

    fun excercise2() {
        val program = fileAsIntList("day5.txt").toMutableList()
        runProgram(program, 5)
    }

}

fun main() {
    //Day5().excercise1()
    Day5().excercise2()
}