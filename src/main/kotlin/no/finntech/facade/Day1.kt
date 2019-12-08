package no.finntech.facade

class Day1 {

    fun getAnswer1(): Int {
        return fileLinesAsList("day1.txt").map { calcFuel(it.toInt()) }.sum()
    }

    fun getAnswer2(): Int {
        return fileLinesAsList("day1.txt").map { calcFuelWithFuelWeight(it.toInt()) }.sum()
    }

    fun calcFuel(mass :Int)  : Int{
        return (mass / 3) - 2
    }

    fun calcFuelWithFuelWeight(mass: Int): Int {
        val fuel = calcFuel(mass)
        if (fuel > 0) {
            return fuel + calcFuelWithFuelWeight(fuel)
        }
        return 0
    }
}

fun main() {
    println("Answer1: ${Day1().getAnswer1()}")
    println("Answer2: ${Day1().getAnswer2()}")
}