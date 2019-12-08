package no.finntech.facade

import java.awt.Point

class Day4 {
    fun isValidPassword(password: String): Boolean {
        val hasEqualValues = password.filterIndexed { index, num -> num == password.getOrNull(index + 1) }.any()
        val isNotIncreasing = password.filterIndexed { index, num -> index < password.length-1 && num.toInt() > password[index + 1].toInt() }.any()
        return hasEqualValues && !isNotIncreasing
    }
    fun isValidPasswordPart2(password: String): Boolean {
        val hasEqualValues = password.groupBy { it }.filter { it.value.size == 2 }.any()
        val isNotIncreasing = password.filterIndexed { index, num -> index < password.length-1 && num.toInt() > password[index + 1].toInt() }.any()
        return hasEqualValues && !isNotIncreasing
    }

    fun excercise1(): Int {
        return (134564..585159).filter { isValidPassword(it.toString()) }.count()
    }

    fun excercise2(): Int {
        return (134564..585159).filter { isValidPasswordPart2(it.toString()) }.count()
    }

}


fun main() {
    println("Ex1: " + Day4().excercise1())
    println("Ex2: " + Day4().excercise2())
}