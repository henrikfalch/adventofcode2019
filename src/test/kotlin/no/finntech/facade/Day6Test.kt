package no.finntech.facade

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day6Test {

    val day = Day6()

    @Test
    fun part1Example1() {
        assertEquals(42, day.numOrbits(listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")))
    }

    @Test
    fun part2Example1() {
        assertEquals(4, day.numOrbitsBetweenYouAndSanta(listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN")))
    }
}