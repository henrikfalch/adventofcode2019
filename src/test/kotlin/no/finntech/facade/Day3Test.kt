package no.finntech.facade

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day3Test {
    val day = Day3()

    @Test
    fun part1Example1() {
        assertEquals(159, day.shortestDistanceCross("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"))
    }

    @Test
    fun part1Example2() {
        assertEquals(135, day.shortestDistanceCross("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"))
    }

    @Test
    fun part1Example3() {
        assertEquals(6, day.shortestDistanceCross("R8,U5,L5,D3", "U7,R6,D4,L4"))
    }



    @Test
    fun part2Example1() {
        assertEquals(610, day.shortestStepWise("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"))
    }

    @Test
    fun part2Example2() {
        assertEquals(410, day.shortestStepWise("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"))
    }


    @Test
    fun part2Example3() {
        assertEquals(30, day.shortestStepWise("R8,U5,L5,D3", "U7,R6,D4,L4"))
    }
}
