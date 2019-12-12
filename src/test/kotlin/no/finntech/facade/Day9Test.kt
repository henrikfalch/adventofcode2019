package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day9Test {
    val day = Day9()

    @Test
    fun part1Example1() {
        val program = listOf(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99).map { it.toBigInteger() }
        assertEquals(program, day.startIntComputer(program))
    }

    @Test
    fun part1Example2() {
        val program = listOf(1102,34915192,34915192,7,4,7,99,0).map { it.toBigInteger() }
        assertEquals(program[1]*program[2], day.startIntComputer(program)[0])
    }

    @Test
    fun part1Example3() {
        val program = listOf(104,1125899906842624,99).map { it.toBigInteger() }
        assertEquals(program[1], day.startIntComputer(program)[0])
    }
}
