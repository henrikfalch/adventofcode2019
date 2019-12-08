package no.finntech.facade

import no.finntech.facade.State.CONTINUE
import no.finntech.facade.State.HALT

class Day2 {

    fun runProgram(program: List<Int>): List<Int> {
        var current = CONTINUE to program
        var index = 0
        while (current.first == CONTINUE) {
            current = calcIteration(current.second, index)
            index += 4
        }
        return current.second
    }


    fun calcIteration(program: List<Int>, index: Int): Pair<State, List<Int>> {
        return when (program[index]) {
            1 -> CONTINUE to add(program, index)
            2 -> CONTINUE to multiply(program, index)
            99 -> HALT to program
            else -> throw IllegalStateException()
        }
    }

    private fun add(program: List<Int>, index: Int): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 + v2 }
        return executeFunction(program, index, function)
    }

    private fun multiply(program: List<Int>, index: Int): List<Int> {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 * v2 }
        return executeFunction(program, index, function)
    }

    private fun executeFunction(program: List<Int>, index: Int, function: (Int, Int) -> Int): List<Int> {
        val val1 = program.getValue(index + 1)
        val val2 = program.getValue(index + 2)
        val copy = program.toMutableList()
        copy[program[index + 3]] = function.invoke(val1, val2)

        return copy
    }

    private fun List<Int>.getValue(index: Int): Int {
        return this[this[index]]
    }

    fun excercise1(): Int {
        val program = fileAsIntList("day2.txt").toMutableList()
        return getOutput(program, 12, 2)
    }

    private fun getOutput(program: MutableList<Int>, noun: Int, verb: Int): Int {
        program[1] = noun
        program[2] = verb
        return runProgram(program)[0]
    }

    fun excercise2(): Int {
        val program = fileAsIntList("day2.txt").toMutableList()
        val wantedOutput = 19690720
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                if (getOutput(program, noun, verb) == wantedOutput) {
                    return (100 * noun) + verb
                }
            }
        }
        return 0
    }

}

enum class State { HALT, CONTINUE }

fun main() {
    //println("Excercise 1: ${Day2().excercise1()}")
    println("Excercise 2: ${Day2().excercise2()}")
}