package no.finntech.facade

import org.junit.jupiter.api.Test
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.test.assertEquals

internal class Day10Test {
    val day = Day10()

    @Test
    fun part1Example1() {
        val program = ("......#.#.\n" +
                "#..#.#....\n" +
                "..#######.\n" +
                ".#.#.###..\n" +
                ".#..#.....\n" +
                "..#....#.#\n" +
                "#..#....#.\n" +
                ".##.#..###\n" +
                "##...#..#.\n" +
                ".#....####").split("\n")
        val best = day.getNodeWithHighestAstroidCount(program)
        assertEquals(Point(5,8), best.first)
        assertEquals(33, best.second)
    }

    @Test
    fun part1Example2() {
        val program = ("#.#...#.#.\n" +
                ".###....#.\n" +
                ".#....#...\n" +
                "##.#.#.#.#\n" +
                "....#.#.#.\n" +
                ".##..###.#\n" +
                "..#...##..\n" +
                "..##....##\n" +
                "......#...\n" +
                ".####.###.").split("\n")
        val best = day.getNodeWithHighestAstroidCount(program)
        assertEquals(Point(1,2), best.first)
        assertEquals(35, best.second)
    }

    @Test
    fun part1Example3() {
        val program = (".#..#..###\n" +
                "####.###.#\n" +
                "....###.#.\n" +
                "..###.##.#\n" +
                "##.##.#.#.\n" +
                "....###..#\n" +
                "..#.#..#.#\n" +
                "#..#.#.###\n" +
                ".##...##.#\n" +
                ".....#.#..").split("\n")
        val best = day.getNodeWithHighestAstroidCount(program)
        assertEquals(Point(6,3), best.first)
        assertEquals(41, best.second)
    }

    @Test
    fun part1Example4() {
        val program = (".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##").split("\n")
        val best = day.getNodeWithHighestAstroidCount(program)
        assertEquals(Point(11,13), best.first)
        assertEquals(210, best.second)
    }

    @Test
    fun part2Example1() {
        val program = (".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##").split("\n")
        val best = day.get200thDestroyed(program, Point(11,13))
        assertEquals(802, best)
    }
}
