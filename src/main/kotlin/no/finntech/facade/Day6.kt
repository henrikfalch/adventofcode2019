package no.finntech.facade

import com.google.common.collect.Maps
import java.util.*

class Day6 {
    fun numOrbits(input: List<String>): Int {
        val orbitMap = input.map { toDirectOrbitPair(it) }.toMap()
        return orbitMap.map { calcLength(orbitMap, it.value) }
                .sum()
    }

    private fun calcLength(orbitMap: Map<String, String>, current: String): Int {
        return orbitMap[current]?.let { calcLength(orbitMap, it) + 1 } ?: 1
    }

    private fun toDirectOrbitPair(it: String): Pair<String, String> {
        val split = it.split(")")
        return split[1] to split[0]
    }

    fun excercise1(): Int {
        val lines = fileLinesAsList("day6.txt")
        return numOrbits(lines)
    }

    fun numOrbitsBetweenYouAndSanta(input: List<String>): Int {
        val orbitMap = input.map { toDirectOrbitPair(it) }.toMap()
        val orbitsFromYou = orbitsFrom(orbitMap, "YOU")
        val orbitsFromSan = orbitsFrom(orbitMap, "SAN")
        return orbitsFromYou.size + orbitsFromSan.size - (2 * orbitsFromSan.intersect(orbitsFromYou).size) - 2
    }

    private fun orbitsFrom(orbitMap: Map<String, String>, current: String) : List<String> {
        return orbitMap[current]?.let { orbitsFrom(orbitMap, it) + it } ?: listOf(current)
    }


    fun excercise2(): Int {
        val lines = fileLinesAsList("day6.txt")
        return numOrbitsBetweenYouAndSanta(lines)
    }


}

fun main() {
    //println("${Day6().excercise1()}")
    println("${Day6().excercise2()}")
}