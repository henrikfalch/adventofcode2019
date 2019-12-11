package no.finntech.facade

import com.google.common.math.IntMath
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D
import java.awt.Point
import java.lang.Math.abs
import kotlin.Comparator
import kotlin.math.sign

class Day10 {
    fun getNodeWithHighestAstroidCount(program: List<String>): Pair<Point, Int> {
        val astroids = getAllAstroids(program)

        var highestCount = Point(-1,-1) to 0
        astroids.forEach { current ->
            val vectors = mutableSetOf<Point>()
            
            astroids.forEach { compare ->
                if (current != compare) {
                    vectors.add((current - compare).normalize())
                }
            }
            
            if(vectors.size > highestCount.second) {
                highestCount = current to vectors.size
            }
        }
        return highestCount
    }

    private fun getAllAstroids(program: List<String>): MutableList<Point> {
        val astroids = mutableListOf<Point>()
        program.forEachIndexed { y, line ->
            line.forEachIndexed { x, current ->
                if (current == '#') {
                    astroids.add(Point(x, y))
                }
            }
        }
        return astroids
    }

    fun getAnswer1(): Pair<Point, Int> {
        val input = fileLinesAsList("day10.txt")
        return getNodeWithHighestAstroidCount(input)
    }

    fun getAnswer2(): Int {
        val input = fileLinesAsList("day10.txt")
        val point = Point(11, 11)
        return get200thDestroyed(input, point)
    }

    fun get200thDestroyed(program: List<String>, point: Point): Int {
        val astroids = getAllAstroids(program)

        val vectors = mutableMapOf<Point, MutableList<Point>>()
        astroids.forEach { compare ->
            if (point != compare) {
                val diff = point - compare
                val normalize = Vector2D(diff.x.toDouble(), diff.y.toDouble()).normalize().let { Point((it.x * 1000).toInt(), (it.y * 1000).toInt()) }
                if (vectors.containsKey(normalize)) {
                    vectors[normalize]!!.add(compare)
                } else {
                    vectors[normalize] = mutableListOf(compare)
                }
            }
        }
        for(l in vectors.values) {
            l.sortBy { manDist(point, it) }
        }

        val vectorsSortedByAngle = vectors.entries.sortedWith(Comparator { a, b -> compareAngle(a.key, b.key) }).toMutableList()

        val destroyedAstroids = mutableListOf<Point>()

        var counter = 0
        var size = destroyedAstroids.size
        while(vectorsSortedByAngle.isNotEmpty()) {
            if (counter >= size) {
                counter = 0
            }
            val currentVector = vectorsSortedByAngle[counter]
            destroyedAstroids.add(currentVector.value.first())
            currentVector.value.removeAt(0)
            if (currentVector.value.isEmpty()) {
                vectorsSortedByAngle.remove(currentVector)
                size--
            } else {
                counter++
            }
        }

        val result = destroyedAstroids[199]
        return (result.x * 100) + result.y
    }

    private fun compareAngle(a: Point, b: Point): Int {
        (a.x < 0).compareTo(b.x < 0).let { if(it != 0) return it }
        if (a.x >= 0) {
            return a.y.compareTo(b.y)
        }
        return b.y.compareTo(a.y)
        //cross(b,a).sign.let { if(it != 0) return it }
        //return a.y.sign.compareTo(b.y.sign)
    }

    private fun cross(a: Point, b: Point) = a.x.toLong() * b.y - b.y.toLong() * b.x

    private fun manDist(a: Point, b: Point) = abs(a.x - b.x) + abs(a.y - b.y)

}

private fun Point.normalize(): Point {
    return IntMath.gcd(if (this.x < 0) this.x * -1 else this.x, if (this.y < 0) this.y * -1 else this.y).let { Point(this.x / it, this.y / it) }
}

private operator fun Point.minus(compare: Point): Point {
    return Point(this.x - compare.x, this.y - compare.y)
}

fun main() {
//    println("Answer1: ${Day10().getAnswer1()}") // 11,11
    println("Answer2: ${Day10().getAnswer2()}") // 11,11
}