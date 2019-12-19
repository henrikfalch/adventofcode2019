package no.finntech.facade

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.lang.IllegalStateException
import java.math.BigInteger

class Day11 {

    fun excercise1() {
        val input = fileAsBigIntList("day11.txt")
        val result = startIntComputer(input)
        println("Exercise 1 answer: ${result.size}")
    }

    private fun startIntComputer(input: List<BigInteger>): Map<Point, BigInteger> {
        var result = mapOf<Point, BigInteger>()
        var currentPosition = Point(0,0)
        var currentDirection = UP
        val channels = IntComputerChannels()

        runBlocking {
            launch {
                IntComputer(input, channels).start()
            }
            launch {
                var isRunning = true
                channels.input.send(BLACK)
                while (isRunning) {
                    select<Unit> {
                        channels.exit.onReceive {
                            isRunning = false
                        }
                        channels.output.onReceive { color -> //1== paint white, 0 == paint black
                            val turn = channels.output.receive() //0 turn left, 1 turn right

                            result = result + Pair(currentPosition, color)

                            currentDirection = currentDirection.turn(turn)
                            currentPosition = currentPosition.move(currentDirection)

                            val currentColor = result[currentPosition] ?: BigInteger.ZERO
                            channels.input.send(currentColor)
                        }
                    }
                }

            }
        }
        return result
    }

    companion object {
        val BLACK = BigInteger.ZERO
        val WHITE = BigInteger.ONE
        val TURN_LEFT = BigInteger.ZERO
        val TURN_RIGHT = BigInteger.ONE
        val UP = Direction(0,1)
        val DOWN = Direction(0,-1)
        val LEFT = Direction(-1,0)
        val RIGHT = Direction(1,0)
    }

    data class Direction(var x: Int, var y: Int) {
        fun turn(turn: BigInteger): Direction {
            return when(this) {
                UP -> if (turn == TURN_LEFT) LEFT else RIGHT
                DOWN -> if (turn == TURN_LEFT) RIGHT else LEFT
                RIGHT -> if (turn == TURN_LEFT) UP else DOWN
                LEFT -> if (turn == TURN_LEFT) DOWN else UP
                else -> throw IllegalStateException("something wrong with the direction")
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun move(currentDirection: Direction): Point {
            return Point(this.x + currentDirection.x, this.y + currentDirection.y)
        }
    }

}

fun main() {
    Day11().excercise1()
    //println("Excercise2: ${Day9().excercise1(BigInteger.TWO)}")
}