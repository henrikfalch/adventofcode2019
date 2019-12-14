package no.finntech.facade

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.math.BigInteger

class Day11 {

    fun excercise1(bigInteger: BigInteger): String {
        val input = fileAsBigIntList("day9.txt")
        return startIntComputer(input, bigInteger).joinToString(", ")
    }

    fun startIntComputer(input: List<BigInteger>, startInput: BigInteger): List<BigInteger> {
        val result = mutableListOf<BigInteger>()
        val channels = IntComputerChannels()

        runBlocking {
            launch {
                IntComputer(input, channels).start()
            }
            launch {
                var isRunning = true
                channels.input.send(BigInteger.ZERO)
                while (isRunning) {
                    select<Unit> {
                        channels.exit.onReceive {
                            isRunning = false
                        }
                        channels.output.onReceive { color -> //1== paint white, 0 == paint black
                            val turn = channels.output.receive() //0 turn left, 1 turn right


                            val currentValue = BigInteger.ZERO //color under the robot
                            channels.input.send(currentValue)
                        }
                    }
                }

            }
        }
        return result
    }


}

fun main() {
    println("Excercise1: ${Day9().excercise1(BigInteger.ONE)}")
    println("Excercise2: ${Day9().excercise1(BigInteger.TWO)}")
}