package no.finntech.facade

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day8Test {

    val day = Day8()

    @Test
    fun part2Example1() {
        val input = listOf(0,2,2,2,1,1,2,2,2,2,1,2,0,0,0,0)
        assertEquals("0110", day.getOutputImage(input, 2, 2).joinToString(""))
    }
}