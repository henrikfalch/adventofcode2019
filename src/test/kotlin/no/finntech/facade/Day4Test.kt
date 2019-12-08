package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day4Test {
    val day = Day4()

    @Test
    fun part1Example1() {
        assertEquals(true, day.isValidPassword("111111"))
    }

    @Test
    fun part1Example2() {
        assertEquals(true, day.isValidPassword("122345"))
    }

    @Test
    fun part1Example3() {
        assertEquals(true, day.isValidPassword("111123"))
    }

    @Test
    fun part1Example4() {
        assertEquals(false, day.isValidPassword("123789"))
    }

    @Test
    fun part1Example5() {
        assertEquals(false, day.isValidPassword("223450"))
    }

    @Test
    fun part1Example6() {
        assertEquals(true, day.isValidPassword("123455"))
    }
}
