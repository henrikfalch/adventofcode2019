package no.finntech.facade

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.math.BigInteger

class Day9 {

    fun excercise1(): String {
        val input = fileAsBigIntList("day9.txt")
        return startIntComputer(input).joinToString(", ")
    }

    fun startIntComputer(input: List<BigInteger>): List<BigInteger> {
        val result = mutableListOf<BigInteger>()
        val channels = IntComputerChannels()

        runBlocking {
            launch {
                IntComputer(input, channels).start()
            }
            launch {
                var isRunning = true
                channels.inputChannel.send(BigInteger.ONE)
                while (isRunning) {
                    select<Unit> {
                        channels.closeChannel.onReceive {
                            println("STOP")
                            isRunning = false
                        }
                        channels.outputChannel.onReceive { output ->
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
    println("Excercise1: ${Day9().excercise1()}")
}