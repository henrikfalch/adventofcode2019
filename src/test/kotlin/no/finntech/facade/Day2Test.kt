package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2Test {
    val day = Day2()

    @Test
    fun part1Example1() {
        assertEquals(listOf(2,0,0,0,99), day.runProgram(listOf(1,0,0,0,99)))
    }

    @Test
    fun part1Example2() {
        assertEquals(listOf(2,3,0,6,99), day.runProgram(listOf(2,3,0,3,99)))
    }

    @Test
    fun part1Example3() {
        assertEquals(listOf(2,4,4,5,99,9801), day.runProgram(listOf(2,4,4,5,99,0)))
    }

    @Test
    fun part1Example4() {
        assertEquals(listOf(30,1,1,4,2,5,6,0,99), day.runProgram(listOf(1,1,1,4,99,5,6,0,99)))
    }

    @Test
    fun part1Example5() {
        assertEquals(listOf(3500,9,10,70, 2,3,11,0, 99, 30,40,50), day.runProgram(listOf(1,9,10,3,2,3,11,0,99,30,40,50)))
    }

    @Test
    fun part2Example() {

    }
}
