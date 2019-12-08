package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day5Test {
    val day = Day5()

    @Test
    fun part2Example1() {
        println("Should output 0")
        day.runProgram(listOf(3,9,8,9,10,9,4,9,99,-1,8), 1)
        println("Should output 1")
        day.runProgram(listOf(3,9,8,9,10,9,4,9,99,-1,8), 8)
        println("Should output 0")
        day.runProgram(listOf(3,9,7,9,10,9,4,9,99,-1,8), 10)
        println("Should output 1")
        day.runProgram(listOf(3,9,7,9,10,9,4,9,99,-1,8), 1)

        println("Should output 0")
        day.runProgram(listOf(3,3,1108,-1,8,3,4,3,99), 1)
        println("Should output 1")
        day.runProgram(listOf(3,3,1108,-1,8,3,4,3,99), 8)
        println("Should output 0")
        day.runProgram(listOf(3,3,1107,-1,8,3,4,3,99), 10)
        println("Should output 1")
        day.runProgram(listOf(3,3,1107,-1,8,3,4,3,99), 1)
    }
}
