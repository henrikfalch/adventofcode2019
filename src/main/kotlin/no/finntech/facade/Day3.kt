package no.finntech.facade

import java.awt.Point

class Day3 {
    fun shortestDistanceCross(path1: String, path2: String): Int {
        val path1Points = findAllPoints(path1)
        val path2Points = findAllPoints(path2)

        return path1Points.intersect(path2Points)
                .map { it.distanceToCenter() }
                .min()!!
    }

    fun shortestStepWise(path1: String, path2:String): Int {
        val path1Points = findAllPoints(path1)
        val path2Points = findAllPoints(path2)
        val crosses = path1Points.intersect(path2Points)

        return crosses.map {
            path1Points.indexOf(it) + path2Points.indexOf(it) + 2
        }.min()!!
    }

    private fun findAllPoints(path: String): List<Point> {
        val pathList = path.split(",")
        var currentState = listOf<Point>() to Point()
        pathList.forEach{
            currentState = findNextPoints(currentState, it)
        }
        return currentState.first
    }


    private fun findNextPoints(path: Pair<List<Point>, Point>, instruction: String) : Pair<List<Point>, Point> {
        val direction = instruction[0]
        val steps = instruction.substring(1).toInt()
        val points = path.first.toMutableList()

        val yStart = path.second.y
        val xStart = path.second.x

        val currentPoint = path.second

        when(direction) {
            'U' -> {
                (1..steps).forEach { points.add(Point(xStart, yStart+it)) }
                currentPoint.y += steps
            }
            'D' -> {
                (1..steps).forEach { points.add(Point(xStart, yStart-it)) }
                currentPoint.y -= steps
            }
            'R' -> {
                (1..steps).forEach { points.add(Point(xStart+it, yStart)) }
                currentPoint.x += steps
            }
            'L' -> {
                (1..steps).forEach { points.add(Point(xStart-it, yStart)) }
                currentPoint.x -= steps
            }
        }

        return points to currentPoint
    }


    private fun Point.distanceToCenter(): Int {
        var x1 = this.x
        if (x1 < 0) {
            x1 *= -1
        }
        var y1 = this.y
        if (y1 < 0) {
            y1 *= -1
        }
        return x1+y1
    }

    fun excercise1() : Int {
        val instructions = fileLinesAsList("day3.txt")
        return shortestDistanceCross(instructions[0], instructions[1])
    }

    fun excercise2() : Int {
        val instructions = fileLinesAsList("day3.txt")
        return shortestStepWise(instructions[0], instructions[1])
    }
}


fun main() {
    println(Day3().excercise2())
}