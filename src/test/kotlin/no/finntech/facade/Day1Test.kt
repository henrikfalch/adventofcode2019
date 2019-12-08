package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class Day1Test {

    @Test
    fun part1Example() {
        assertEquals(2, Day1().calcFuel(12))
        assertEquals(2, Day1().calcFuel(14))
        assertEquals(654, Day1().calcFuel(1969))
        assertEquals(33583, Day1().calcFuel(100756))
    }

    @Test
    fun part2Example() {
        assertEquals(2, Day1().calcFuelWithFuelWeight(14))
        assertEquals(966, Day1().calcFuelWithFuelWeight(1969))
        assertEquals(50346, Day1().calcFuelWithFuelWeight(100756))
    }
}
