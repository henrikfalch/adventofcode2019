package no.finntech.facade

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.math.BigInteger

class Day9 {

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
                channels.input.send(startInput)
                while (isRunning) {
                    select<Unit> {
                        channels.exit.onReceive {
                            isRunning = false
                        }
                        channels.output.onReceive { output ->
                            result.add(output)
                        }
                    }
                }

            }
        }
        return result
    }
}

fun main() {
    println("Excercise1: ${Day9().excercise1(BigInteger.ONE)}") //2752191671
    println("Excercise2: ${Day9().excercise1(BigInteger.TWO)}") //87571
}